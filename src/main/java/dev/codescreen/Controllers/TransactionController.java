package dev.codescreen.Controllers;

import dev.codescreen.AuthorizationRequest;
import dev.codescreen.LoadRequest;
import dev.codescreen.Service.TransactionService;
import dev.codescreen.TransactionResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);


    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate = now.format(formatter);
        logger.info("Ping method called, returning time: {}", formattedDate);
        return ResponseEntity.ok(new PingResponse(formattedDate));
    }

    public static class PingResponse {
        private String serverTime;

        public PingResponse(String serverTime) {
            this.serverTime = serverTime;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }
    }

    @PostMapping("/load")
    public ResponseEntity<TransactionResponse> loadFunds(@RequestBody LoadRequest loadRequest) {
        BigDecimal amount = loadRequest.getTransactionAmount().getAmount();
        logger.info("Attempting to load funds for user ID: {} with amount: {}", loadRequest.getUserId(), amount);
        TransactionResponse response = new TransactionResponse();

        // Check if the amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Disallowed to load non-positive amount for user ID: {}", loadRequest.getUserId());
            response.setUserId(loadRequest.getUserId());
            response.setMessageId(loadRequest.getMessageId());
            response.setResponseCode("FAILED");

            return ResponseEntity.badRequest().body(response);
        }

        response = transactionService.processLoad(loadRequest);
        if (response == null) {
            logger.error("Failed to load funds for user ID: {}", loadRequest.getUserId());
            return ResponseEntity.badRequest().build();
        }

        logger.info("Funds loaded successfully for user ID: {}", loadRequest.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authorization")
    public ResponseEntity<TransactionResponse> authorizeTransaction(@RequestBody AuthorizationRequest authorizationRequest) {
        logger.info("Authorizing transaction for user ID: {} with amount: {}", authorizationRequest.getUserId(), authorizationRequest.getTransactionAmount().getAmount());
        TransactionResponse response = transactionService.processAuthorization(authorizationRequest);
        if (response == null) {
            logger.error("Authorization failed for user ID: {}", authorizationRequest.getUserId());
            return ResponseEntity.badRequest().build();
        }
        logger.info("Transaction authorized for user ID: {}", authorizationRequest.getUserId());
        return ResponseEntity.ok(response);
    }
}