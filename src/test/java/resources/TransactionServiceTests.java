package resources;

import dev.codescreen.*;
import dev.codescreen.Service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankLedgerApplication.class)
public class TransactionServiceTests {
    @Autowired
    private TransactionService transactionService;

    @Test
    public void testLoadFunds() {
        // Setup - create a LoadRequest with expected values using the parameterized constructor
        TransactionEvent transactionEvent = new TransactionEvent(
                "user1",
                "msg123",
                new BigDecimal("100.00"),
                "USD",
                "CREDIT",
                LocalDateTime.now()
        );

        LoadRequest request = new LoadRequest();
        request.setUserId("user1");
        request.setMessageId("msg123");
        request.setTransactionAmount(transactionEvent);

        // Action - process the load request
        TransactionResponse response = transactionService.processLoad(request);

        // Assertion - verify that the response contains the expected balance
        BigDecimal expectedBalance = new BigDecimal("100.00"); // Assuming this is the initial load
        Assertions.assertEquals(0, expectedBalance.compareTo(response.getBalance().getAmount()), "Balance should be 100.00 after load");
    }

    @Test
    public void testAuthorizeTransactionDenied() {
        // Setup - create an AuthorizationRequest with expected values using the parameterized constructor
        TransactionEvent transactionEvent = new TransactionEvent(
                "user1",
                "msg124",
                new BigDecimal("50.00"),
                "USD",
                "DEBIT",
                LocalDateTime.now()
        );

        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId("user1");
        request.setMessageId("msg124");
        request.setTransactionAmount(transactionEvent);

        // Action - process the authorization request
        TransactionResponse response = transactionService.processAuthorization(request);

        // Assertion - verify that the response has the expected response code
        String expectedResponseCode = "DENIED"; // Assuming the user has no balance
        Assertions.assertEquals(expectedResponseCode, response.getResponseCode());
    }

    private void processLoad(String messageId, String userId, BigDecimal amount, String expectedResponseCode, BigDecimal expectedBalance) {
        TransactionEvent transactionEvent = new TransactionEvent(userId, messageId, amount, "USD", "CREDIT", LocalDateTime.now());
        LoadRequest request = new LoadRequest();
        request.setUserId(userId);
        request.setMessageId(messageId);
        request.setTransactionAmount(transactionEvent);
        TransactionResponse response = transactionService.processLoad(request);
        Assertions.assertEquals(expectedResponseCode, response.getResponseCode());
        Assertions.assertEquals(0, expectedBalance.compareTo(response.getBalance().getAmount()));
    }

    @Test
    public void testTransactions() {
        // All these tests are from the sample_tests file
        // Test 1: Load action, user 1
        processLoad("1", "1", new BigDecimal("100"), "APPROVED", new BigDecimal("100.00"));

        // Test 2: Load action, user 1, small amount
        processLoad("2", "1", new BigDecimal("3.23"), "APPROVED", new BigDecimal("103.23"));

        // Test 3: Authorization action, user 1, exact available amount
        processAuthorization("3", "1", new BigDecimal("100"), "APPROVED", new BigDecimal("3.23"));

        // Test 4: Authorization action, user 1, denied due to insufficient funds
        processAuthorization("4", "1", new BigDecimal("10"), "DENIED", new BigDecimal("3.23"));

        // Test 5: Authorization action, user 2, no funds to begin with
        processAuthorization("5", "2", new BigDecimal("50.01"), "DENIED", new BigDecimal("0.00"));

        // Test 6: Load action, user 3, sets up exact balance
        processLoad("6", "3", new BigDecimal("50.01"), "APPROVED", new BigDecimal("50.01"));

        // Test 7: Attempted authorization without prior load (this test setup assumes user 2 has no initial credit)
        processAuthorization("7", "2", new BigDecimal("50.01"), "DENIED", new BigDecimal("0.00"));
    }

    private void processAuthorization(String messageId, String userId, BigDecimal amount, String expectedResponseCode, BigDecimal expectedBalance) {
        TransactionEvent transactionEvent = new TransactionEvent(userId, messageId, amount, "USD", "DEBIT", LocalDateTime.now());
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId(userId);
        request.setMessageId(messageId);
        request.setTransactionAmount(transactionEvent);
        TransactionResponse response = transactionService.processAuthorization(request);
        Assertions.assertEquals(expectedResponseCode, response.getResponseCode());
        Assertions.assertEquals(0, expectedBalance.compareTo(response.getBalance().getAmount()));
    }
}
