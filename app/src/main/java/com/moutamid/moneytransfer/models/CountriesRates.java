package com.moutamid.moneytransfer.models;

public class CountriesRates {
    String name, currencyCode;
    static Rates rates;

    public CountriesRates() {
    }

    public CountriesRates(String name, String currencyCode, Rates rates) {
        this.name = name;
        this.currencyCode = currencyCode;
        CountriesRates.rates = rates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public static class Rates {
        double Egypt, UAE, Saudi_Arabia, Qatar, Morocco, Sudan, Oman, Italy, Russia, Syria, Palestine;

        public Rates() {
        }

        public Rates(double egypt, double uae, double saudi_Arabia, double qatar, double morocco, double sudan, double oman, double italy, double russia, double syria, double palestine) {
            Egypt = egypt;
            UAE = uae;
            Saudi_Arabia = saudi_Arabia;
            Qatar = qatar;
            Morocco = morocco;
            Sudan = sudan;
            Oman = oman;
            Italy = italy;
            Russia = russia;
            Syria = syria;
            Palestine = palestine;
        }

        public double getEgypt() {
            return Egypt;
        }

        public void setEgypt(double egypt) {
            Egypt = egypt;
        }

        public double getUAE() {
            return UAE;
        }

        public void setUAE(double UAE) {
            this.UAE = UAE;
        }

        public double getSaudi_Arabia() {
            return Saudi_Arabia;
        }

        public void setSaudi_Arabia(double saudi_Arabia) {
            Saudi_Arabia = saudi_Arabia;
        }

        public double getQatar() {
            return Qatar;
        }

        public void setQatar(double qatar) {
            Qatar = qatar;
        }

        public double getMorocco() {
            return Morocco;
        }

        public void setMorocco(double morocco) {
            Morocco = morocco;
        }

        public double getSudan() {
            return Sudan;
        }

        public void setSudan(double sudan) {
            Sudan = sudan;
        }

        public double getOman() {
            return Oman;
        }

        public void setOman(double oman) {
            Oman = oman;
        }

        public double getItaly() {
            return Italy;
        }

        public void setItaly(double italy) {
            Italy = italy;
        }

        public double getRussia() {
            return Russia;
        }

        public void setRussia(double russia) {
            Russia = russia;
        }

        public double getSyria() {
            return Syria;
        }

        public void setSyria(double syria) {
            Syria = syria;
        }

        public double getPalestine() {
            return Palestine;
        }

        public void setPalestine(double palestine) {
            Palestine = palestine;
        }
    }

}
