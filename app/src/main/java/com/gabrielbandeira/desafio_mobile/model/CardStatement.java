package com.gabrielbandeira.desafio_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardStatement {

    @SerializedName("purchases")
    private List<Compras> purchases = null;

    public List<Compras> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Compras> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        return "{" +
                "\"purchases\":" + purchases +
                '}';
    }
}
