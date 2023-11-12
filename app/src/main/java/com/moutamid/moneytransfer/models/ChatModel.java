package com.moutamid.moneytransfer.models;

public class ChatModel {
    String ID, userID, image, name, message;

    public ChatModel() {
    }

    public ChatModel(String ID, String userID, String image, String name, String message) {
        this.ID = ID;
        this.userID = userID;
        this.image = image;
        this.name = name;
        this.message = message;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
