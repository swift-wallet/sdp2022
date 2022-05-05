package com.sdp.swiftwallet.presentation.wallet.fragments;

import com.sdp.swiftwallet.domain.model.wallet.IWalletKeyPair;

import java.math.BigInteger;

/**
 * Represent a simple view model of a wallet
 */
public class WalletItem {

    private final String address;
    private final BigInteger balance;

    public WalletItem(IWalletKeyPair walletKeyPair) {
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
