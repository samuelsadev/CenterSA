package com.saproject.centerSA.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "transactions")
public class TransactionRecord {

    @Id
    private String id;
    private String transactionId;
    private String type;
    private String status;
    private String originAccount;
    private String destinationAccount;
    private String accountNumber;
    private double amount;
    private String timestamp;

    public TransactionRecord() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public TransactionRecord(String transactionId, String status, TransactionDTO transactionDTO) {
        this.transactionId = transactionId;
        this.type = "transfer";
        this.status = status;
        this.originAccount = transactionDTO.originAccountNumber();
        this.destinationAccount = transactionDTO.destinationAccountNumber();
        this.amount = transactionDTO.amount();
        this.timestamp = LocalDateTime.now().toString();
    }

    public TransactionRecord(String type, String accountNumber, double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.status = "COMPLETED";
        this.timestamp = LocalDateTime.now().toString();
    }

    public void updateStatus(String status) {
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(String originAccount) {
        this.originAccount = originAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
