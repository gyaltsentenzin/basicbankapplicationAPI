package dev.codescreen;

public class LoadRequest {
    private String userId;
    private String messageId;
    private TransactionEvent transactionAmount;

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for messageId
    public String getMessageId() {
        return messageId;
    }

    // Setter for messageId
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // Getter for transactionAmount
    public TransactionEvent getTransactionAmount() {
        return transactionAmount;
    }

    // Setter for transactionAmount
    public void setTransactionAmount(TransactionEvent transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
