package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

public interface IWalletKeyPair {
    String getHexPublicKey();
    int getID();
    BigInteger getNativeBalance();
    void updateBalance(IWeb3Requests web3Requests);
    String signTransaction(RawTransaction rawTransaction);
}
