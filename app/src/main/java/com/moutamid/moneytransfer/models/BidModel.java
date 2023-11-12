package com.moutamid.moneytransfer.models;

public class BidModel {
    String ID;
    String userID, username, userImage;
    Rating userRating;
    double price,price_ioc;
    String myCountry, bidCountry;

    public BidModel() {
    }

    public BidModel(String ID, String userID, String username, String userImage, Rating userRating, double price, double price_ioc, String myCountry, String bidCountry) {
        this.ID = ID;
        this.userID = userID;
        this.username = username;
        this.userImage = userImage;
        this.userRating = userRating;
        this.price = price;
        this.price_ioc = price_ioc;
        this.myCountry = myCountry;
        this.bidCountry = bidCountry;
    }

    public double getPrice_ioc() {
        return price_ioc;
    }

    public void setPrice_ioc(double price_ioc) {
        this.price_ioc = price_ioc;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Rating getUserRating() {
        return userRating;
    }

    public void setUserRating(Rating userRating) {
        this.userRating = userRating;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMyCountry() {
        return myCountry;
    }

    public void setMyCountry(String myCountry) {
        this.myCountry = myCountry;
    }

    public String getBidCountry() {
        return bidCountry;
    }

    public void setBidCountry(String bidCountry) {
        this.bidCountry = bidCountry;
    }

}
