package com.sdp.swiftwallet.domain.model.wallet;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

public interface IWalletKeyPair {
    String getHexPublicKey();
    int getID();
    BigInteger getNativeBalance();
    void updateBalance();
    public String signTransaction(RawTransaction rawTransaction);
}
