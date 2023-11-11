package com.moutamid.moneytransfer.models;

public class CountriesRates {
    String name, currencyCode;
    Rates rates;

    public CountriesRates() {
    }

    public CountriesRates(String name, String currencyCode, Rates rates) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.rates = rates;
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

    public class Rates {
        double EG, AE, SA, QA, MA, SD, OM, IT, RU, SY, PS;

        public Rates() {
        }

        public Rates(double EG, double AE, double SA, double QA, double MA, double SD, double OM, double IT, double RU, double SY, double PS) {
            this.EG = EG;
            this.AE = AE;
            this.SA = SA;
            this.QA = QA;
            this.MA = MA;
            this.SD = SD;
            this.OM = OM;
            this.IT = IT;
            this.RU = RU;
            this.SY = SY;
            this.PS = PS;
        }

        public double getEG() {
            return EG;
        }

        public void setEG(double EG) {
            this.EG = EG;
        }

        public double getAE() {
            return AE;
        }

        public void setAE(double AE) {
            this.AE = AE;
        }

        public double getSA() {
            return SA;
        }

        public void setSA(double SA) {
            this.SA = SA;
        }

        public double getQA() {
            return QA;
        }

        public void setQA(double QA) {
            this.QA = QA;
        }

        public double getMA() {
            return MA;
        }

        public void setMA(double MA) {
            this.MA = MA;
        }

        public double getSD() {
            return SD;
        }

        public void setSD(double SD) {
            this.SD = SD;
        }

        public double getOM() {
            return OM;
        }

        public void setOM(double OM) {
            this.OM = OM;
        }

        public double getIT() {
            return IT;
        }

        public void setIT(double IT) {
            this.IT = IT;
        }

        public double getRU() {
            return RU;
        }

        public void setRU(double RU) {
            this.RU = RU;
        }

        public double getSY() {
            return SY;
        }

        public void setSY(double SY) {
            this.SY = SY;
        }

        public double getPS() {
            return PS;
        }

        public void setPS(double PS) {
            this.PS = PS;
        }
    }

}
