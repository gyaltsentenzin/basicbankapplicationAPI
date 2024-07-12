package resources;

import dev.codescreen.BankLedgerApplication;
import dev.codescreen.Service.TransactionService;
import dev.codescreen.TransactionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = BankLedgerApplication.class)
public class TransactionEventTests {
    @Test
    public void testTransactionEventGettersAndSetters() {
        String userId = "user123";
        String messageId = "message001";
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "USD";
        String debitOrCredit = "CREDIT";
        LocalDateTime timeStamp = LocalDateTime.now();

        // Create an instance of TransactionEvent
        TransactionEvent event = new TransactionEvent(userId, messageId, amount, currency, debitOrCredit, timeStamp);

        // Test getters
        assertEquals(userId, event.getUserId());
        assertEquals(messageId, event.getMessageId());
        assertEquals(amount, event.getAmount());
        assertEquals(currency, event.getCurrency());
        assertEquals(debitOrCredit, event.getDebitOrCredit());
        assertEquals(timeStamp, event.getTimeStamp());

        // Test setters
        event.setUserId("user456");
        event.setMessageId("message002");
        event.setAmount(new BigDecimal("200.00"));
        event.setCurrency("EUR");
        event.setDebitOrCredit("DEBIT");
        LocalDateTime newTimeStamp = LocalDateTime.now().plusDays(1);
        event.setTimeStamp(newTimeStamp);

        // Test getters after setting new values
        assertEquals("user456", event.getUserId());
        assertEquals("message002", event.getMessageId());
        assertEquals(new BigDecimal("200.00"), event.getAmount());
        assertEquals("EUR", event.getCurrency());
        assertEquals("DEBIT", event.getDebitOrCredit());
        assertEquals(newTimeStamp, event.getTimeStamp());
    }
}

