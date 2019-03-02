package com.gabrielbandeira.desafio_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Compras{
    @SerializedName("date")
    private String date;
    @SerializedName("store")
    private String store;
    @SerializedName("description")
    private String description;
    @SerializedName("value")
    private Double value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "\"date\":\"" + date + '"' +
                ", \"store\":\"" + store + '"' +
                ", \"description\":\"" + description + '"' +
                ", \"value\":" + value +
                '}';
    }
}