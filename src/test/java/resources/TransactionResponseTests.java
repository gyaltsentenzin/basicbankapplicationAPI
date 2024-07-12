package resources;

import dev.codescreen.BankLedgerApplication;
import dev.codescreen.TransactionEvent;
import dev.codescreen.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = BankLedgerApplication.class)
public class TransactionResponseTests {

    @Test
    public void testTransactionResponseGettersAndSetters() {
        // Create a TransactionEvent for balance testing
        TransactionEvent balance = new TransactionEvent(
                "user1", "msg123", new BigDecimal("1000.00"), "USD", "CREDIT", LocalDateTime.now()
        );

        // Instantiate TransactionResponse and set values
        TransactionResponse response = new TransactionResponse();
        response.setUserId("user123");
        response.setMessageId("message001");
        response.setResponseCode("APPROVED");
        response.setBalance(balance);

        // Test getters to confirm setters worked correctly
        assertEquals("user123", response.getUserId());
        assertEquals("message001", response.getMessageId());
        assertEquals("APPROVED", response.getResponseCode());
        assertEquals(balance, response.getBalance());

        // Test setting new values
        TransactionEvent newBalance = new TransactionEvent(
                "user2", "msg124", new BigDecimal("2000.00"), "EUR", "DEBIT", LocalDateTime.now().plusDays(1)
        );
        response.setUserId("user456");
        response.setMessageId("message002");
        response.setResponseCode("DECLINED");
        response.setBalance(newBalance);

        // Assert new values to confirm setters are working
        assertEquals("user456", response.getUserId());
        assertEquals("message002", response.getMessageId());
        assertEquals("DECLINED", response.getResponseCode());
        assertEquals(newBalance, response.getBalance());
    }
}
