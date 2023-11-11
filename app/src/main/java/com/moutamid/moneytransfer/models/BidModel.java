package com.moutamid.moneytransfer.models;

public class BidModel {
    double price;
    String myCountry, bidCountry;

    public BidModel() {
    }

    public BidModel(double price, String myCountry, String bidCountry) {
        this.price = price;
        this.myCountry = myCountry;
        this.bidCountry = bidCountry;
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
