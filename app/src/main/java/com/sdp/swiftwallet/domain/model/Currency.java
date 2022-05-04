package com.sdp.swiftwallet.domain.model;

import java.io.Serializable;

public class Currency implements Serializable {
    private String baseAsset; //name needs to be changed to be symbol for base asset
    private String symbol; //symbol is now symbol for both assets (e.g. "ETHBTC")
    private double value;

    public Currency(String name, String symbol, double value) {
        this.baseAsset = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName() {
        return baseAsset;
    }

    public void setName(String name) {
        this.baseAsset = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String shortName) {
        this.symbol = shortName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}