package dev.codescreen;

public class TransactionResponse {
    private String userId;
    private String messageId;
    private String responseCode;
    private TransactionEvent balance;

//    public TransactionResponse(String userId, String messageId, String responseCode, TransactionEvent balance) {
//        this.userId = userId;
//        this.messageId = messageId;
//        this.responseCode = responseCode;
//        this.balance = balance;
//    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and setter for messageId
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // Getter and setter for responseCode
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    // Getter and setter for balance
    public TransactionEvent getBalance() {
        return balance;
    }

    public void setBalance(TransactionEvent balance) {
        this.balance = balance;
    }
}
