package com.sdp.swiftwallet.presentation.wallet.fragments;

// Simple wallet model
public class WalletItem {
    public String address;
    public double balance = 10.2321;

    public WalletItem(String address) {
        this.address = address;
    }
    public void updateBalance(double newBalance) {
        balance = newBalance;
    }
}
