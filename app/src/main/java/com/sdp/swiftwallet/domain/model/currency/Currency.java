package com.sdp.swiftwallet.domain.model.currency;

import java.io.Serializable;

/**
 * Represents a serializable currency
 */
public class Currency implements Serializable {

    // Name needs to be changed to be symbol for base asset
    private String baseAsset;
    // Symbol is now symbol for both assets (e.g. "ETHBTC")
    private String symbol;
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