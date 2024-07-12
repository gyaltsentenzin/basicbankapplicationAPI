package dev.codescreen.Service;

import dev.codescreen.AuthorizationRequest;
import dev.codescreen.LoadRequest;
import dev.codescreen.TransactionEvent;
import dev.codescreen.TransactionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final List<TransactionEvent> eventStore = new ArrayList<>();

    public TransactionResponse processLoad(LoadRequest loadRequest) {
        // Create a transaction event from the load request using the new constructor
        TransactionEvent transactionEvent = new TransactionEvent(
                loadRequest.getUserId(),
                loadRequest.getMessageId(),
                loadRequest.getTransactionAmount().getAmount(),
                loadRequest.getTransactionAmount().getCurrency(),
                "CREDIT",
                LocalDateTime.now()
        );

        // Add event to the eventStore
        eventStore.add(transactionEvent);

        // Calculate new balance
        BigDecimal newBalance = calculateBalance(loadRequest.getUserId());

        // Create and return TransactionResponse
        TransactionResponse response = new TransactionResponse();
        response.setUserId(loadRequest.getUserId());
        response.setMessageId(loadRequest.getMessageId());
        response.setResponseCode("APPROVED");
        response.setBalance(new TransactionEvent(
                loadRequest.getUserId(),
                loadRequest.getMessageId(),
                newBalance,
                loadRequest.getTransactionAmount().getCurrency(),
                "CREDIT",
                LocalDateTime.now()
        ));
        return response;
    }


    public TransactionResponse processAuthorization(AuthorizationRequest authorizationRequest) {
        // Calculate current balance to check for sufficient funds
        BigDecimal currentBalance = calculateBalance(authorizationRequest.getUserId());
        BigDecimal transactionAmount = authorizationRequest.getTransactionAmount().getAmount();

        // Check if the balance is sufficient and cannot below 0
        if (currentBalance.compareTo(transactionAmount) >= 0) {
            // Sufficient funds, process the transaction
            TransactionEvent transactionEvent = new TransactionEvent(
                    authorizationRequest.getUserId(),
                    authorizationRequest.getMessageId(),
                    transactionAmount,
                    authorizationRequest.getTransactionAmount().getCurrency(),
                    "DEBIT",
                    LocalDateTime.now()
            );

            // Add event to the eventStore
            eventStore.add(transactionEvent);

            // Calculate new balance
            BigDecimal newBalance = calculateBalance(authorizationRequest.getUserId());

            // Create and return TransactionResponse
            TransactionResponse response = new TransactionResponse();
            response.setUserId(authorizationRequest.getUserId());
            response.setMessageId(authorizationRequest.getMessageId());
            response.setResponseCode("APPROVED");
            response.setBalance(new TransactionEvent(
                    authorizationRequest.getUserId(),
                    authorizationRequest.getMessageId(),
                    newBalance,
                    authorizationRequest.getTransactionAmount().getCurrency(),
                    "DEBIT",
                    LocalDateTime.now()
            ));
            return response;
        } else {
            // Insufficient funds, decline the transaction
            TransactionResponse response = new TransactionResponse();
            response.setUserId(authorizationRequest.getUserId());
            response.setMessageId(authorizationRequest.getMessageId());
            response.setResponseCode("DENIED");
            response.setBalance(new TransactionEvent(
                    authorizationRequest.getUserId(),
                    authorizationRequest.getMessageId(),
                    currentBalance,
                    authorizationRequest.getTransactionAmount().getCurrency(),
                    "DEBIT",
                    LocalDateTime.now()
            ));
            return response;
        }
    }

    private BigDecimal calculateBalance(String userId) {
        return eventStore.stream()
                .filter(e -> e.getUserId().equals(userId))
                .map(e -> e.getDebitOrCredit().equals("CREDIT") ? e.getAmount() : e.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
