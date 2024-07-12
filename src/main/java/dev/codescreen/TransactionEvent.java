package dev.codescreen;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionEvent {
    private String userId;
    private String messageId;
    private BigDecimal amount;
    private String currency;
    private String debitOrCredit;
    private LocalDateTime timeStamp;

    public TransactionEvent(String userId, String messageId, BigDecimal amount, String currency, String debitOrCredit, LocalDateTime timeStamp) {
        this.userId = userId;
        this.messageId = messageId;
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
        this.timeStamp = timeStamp;
    }

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

    // Getter for amount
    public BigDecimal getAmount() {
        return amount;
    }

    // Setter for amount
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Getter for currency
    public String getCurrency() {
        return currency;
    }

    // Setter for currency
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Getter for debitOrCredit
    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    // Setter for debitOrCredit
    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    // Getter for timeStamp
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    // Setter for timeStamp
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}