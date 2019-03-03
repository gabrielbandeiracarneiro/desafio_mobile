package com.gabrielbandeira.desafio_mobile.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Compras{


    @SerializedName("date")
    private String date;
    @SerializedName("store")
    private String store;
    @SerializedName("description")
    private String description;
    @SerializedName("value")
    private Double value;

    public Date getDate() {
        Date retorno = null;
        try {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            retorno=dt.parse(date);
        }
        catch (Exception e) {
            Log.d("Erro",e.toString());
        }
        return retorno;
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