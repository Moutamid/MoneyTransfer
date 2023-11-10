package com.moutamid.moneytransfer.models;

public class UserModel {
    String ID, name, image, phone, email, password;

    Rating rating;

    public UserModel() {
    }

    public UserModel(String ID, String name, String image, String phone, String email, String password, Rating rating) {
        this.ID = ID;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
