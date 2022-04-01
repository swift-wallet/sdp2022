package com.sdp.swiftwallet.presentation.wallet.fragments;

import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;

import java.math.BigInteger;

// Simple wallet model
public class WalletItem {
    private String address;
    private BigInteger balance;

    public WalletItem(WalletKeyPair walletKeyPair) {
        this.address = walletKeyPair.getHexPublicKey();
        this.balance = walletKeyPair.getNativeBalance();
    }

    public String getBalance() {
        return balance.toString(10);
    }
    public String getAddress(){
        return address;
    }
}
