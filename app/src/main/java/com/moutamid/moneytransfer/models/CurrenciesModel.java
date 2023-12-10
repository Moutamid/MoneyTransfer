package com.moutamid.moneytransfer.models;

public class CurrenciesModel {
    String amount, name;
    int icon;

    public CurrenciesModel(String amount, String name, int icon) {
        this.amount = amount;
        this.name = name;
        this.icon = icon;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
