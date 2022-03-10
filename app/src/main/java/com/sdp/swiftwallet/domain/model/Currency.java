package com.sdp.swiftwallet.domain.model;

public class Currency {
    private String name;
    private String symbol;
    private double value;

    public Currency(String name, String symbol, double value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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