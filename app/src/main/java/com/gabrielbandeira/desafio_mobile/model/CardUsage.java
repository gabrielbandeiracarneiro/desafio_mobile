package com.gabrielbandeira.desafio_mobile.model;

import com.google.gson.annotations.SerializedName;

public class CardUsage {

    @SerializedName("month")
    public String month;
    @SerializedName("name")
    public String name;
    @SerializedName("value")
    public Double value;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "\"month\":\"" + month + '"' +
                ", \"name\":\"" + name + '"' +
                ", \"value\":" + value +
                '}';
    }
}