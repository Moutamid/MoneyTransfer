package com.moutamid.moneytransfer.models;

public class ConversationModel {
    String message;
    String senderID;
    long timestamps;

    public ConversationModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }

    public ConversationModel(String message, String senderID, long timestamps) {
        this.message = message;
        this.senderID = senderID;
        this.timestamps = timestamps;
    }
}
