package com.gabrielbandeira.desafio_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("name")
    public String name;
    @SerializedName("cardNumber")
    public String cardNumber;
    @SerializedName("expirationDate")
    public String expirationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name:\"" + name + '"' +
                ", \"cardNumber\":\"" + cardNumber + '"' +
                ", \"expirationDate\":\"" + expirationDate + '"' +
                '}';
    }
}