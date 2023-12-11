package com.moutamid.moneytransfer.models;

public class CurrenciesModel {
    String symbol, sign, amount, name;
    int icon;

    public CurrenciesModel(String symbol, String sign, String amount, String name, int icon) {
        this.symbol = symbol;
        this.sign = sign;
        this.amount = amount;
        this.name = name;
        this.icon = icon;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
