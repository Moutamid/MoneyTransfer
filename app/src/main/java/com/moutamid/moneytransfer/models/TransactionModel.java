package com.moutamid.moneytransfer.models;

public class TransactionModel {
    String id, senderID, transactionName;
    double money;
    long timestamp;
    UserModel receiver;

    public TransactionModel() {
    }

    public TransactionModel(String id, String senderID, String transactionName, double money, long timestamp, UserModel receiver) {
        this.id = id;
        this.senderID = senderID;
        this.transactionName = transactionName;
        this.money = money;
        this.timestamp = timestamp;
        this.receiver = receiver;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }
}
