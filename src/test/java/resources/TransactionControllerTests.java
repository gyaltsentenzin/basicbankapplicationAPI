package resources;

import dev.codescreen.AuthorizationRequest;
import dev.codescreen.BankLedgerApplication;
import dev.codescreen.Controllers.TransactionController;
import dev.codescreen.LoadRequest;
import dev.codescreen.Service.TransactionService;
import dev.codescreen.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BankLedgerApplication.class)
@AutoConfigureMockMvc
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;


    @Test
    public void testPing() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverTime").exists());
    }

    @Test
    public void testLoadFunds() throws Exception {
        // Create a sample response object
        TransactionResponse response = new TransactionResponse();
        response.setUserId("user1");
        response.setMessageId("msg123");
        response.setResponseCode("APPROVED");
        // Assuming there's a way to set the transaction event or balance details if needed
        // response.setBalance(...);

        // Mock the service method to return this response
        Mockito.when(transactionService.processLoad(any(LoadRequest.class))).thenReturn(response);

        String jsonRequest = "{\"userId\":\"user1\", \"messageId\":\"msg123\", \"transactionAmount\":{\"amount\": \"100.00\", \"currency\":\"USD\", \"debitOrCredit\":\"CREDIT\"}}";

        mockMvc.perform(post("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("APPROVED"));
    }

    @Test
    public void testLoadFunds_NegativeAmount() throws Exception {
        // Create a sample response object for negative transaction amount
        TransactionResponse response = new TransactionResponse();
        response.setUserId("user1");
        response.setMessageId("msg123");
        response.setResponseCode("FAILED"); // Assuming FAILED is the code for errors

        // Mock the service to simulate the negative amount handling
        doReturn(response).when(transactionService).processLoad(any(LoadRequest.class));

        String jsonRequest = "{\"userId\":\"user1\", \"messageId\":\"msg123\", \"transactionAmount\":{\"amount\": \"-100.00\", \"currency\":\"USD\", \"debitOrCredit\":\"CREDIT\"}}";

        mockMvc.perform(post("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseCode").value("FAILED"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.messageId").value("msg123"));
    }

    @Test
    public void testAuthorizeTransaction() throws Exception {
        // Create a sample response object
        TransactionResponse response = new TransactionResponse();
        response.setUserId("user2");
        response.setMessageId("msg124");
        response.setResponseCode("DENIED");
        // Assuming there's a way to set the transaction event or balance details if needed
        // response.setBalance(...);

        // Mock the service method to return this response
        Mockito.when(transactionService.processAuthorization(any(AuthorizationRequest.class))).thenReturn(response);

        String jsonRequest = "{\"userId\":\"user2\", \"messageId\":\"msg124\", \"transactionAmount\":{\"amount\": \"50.00\", \"currency\":\"USD\", \"debitOrCredit\":\"DEBIT\"}}";

        mockMvc.perform(post("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("DENIED"));
    }


    @Test
    public void testSetServerTime() {
        // Arrange
        String initialTime = "2023-01-01T00:00:00.000Z";
        String newTime = "2023-12-31T23:59:59.999Z";
        TransactionController.PingResponse pingResponse = new TransactionController.PingResponse(initialTime);

        // Act
        pingResponse.setServerTime(newTime);

        // Assert
        assertEquals(newTime, pingResponse.getServerTime(), "The serverTime should be updated to the expected value");
    }

    @Test
    public void testLoadFunds_Failure() throws Exception {
        String jsonRequest = "{\"userId\":\"user1\", \"messageId\":\"msg123\", \"transactionAmount\":{\"amount\": 100.00, \"currency\":\"USD\"}}";

        when(transactionService.processLoad(any())).thenReturn(null);

        mockMvc.perform(post("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthorizeTransaction_Failure() throws Exception {
        String jsonRequest = "{\"userId\":\"user2\", \"messageId\":\"msg124\", \"transactionAmount\":{\"amount\": 50.00, \"currency\":\"USD\"}}";

        when(transactionService.processAuthorization(any())).thenReturn(null);

        mockMvc.perform(post("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}
