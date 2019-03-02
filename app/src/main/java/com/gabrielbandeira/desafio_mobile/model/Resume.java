package com.gabrielbandeira.desafio_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Resume {

    @SerializedName("balance")
    public Double balance;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "{" +
                "\"balance\":" + balance +
                '}';
    }
}