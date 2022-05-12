package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

/**
 * Wallet associated with it's key
 */
public interface IWalletKeyPair {

    String getHexPublicKey();

    BigInteger getNativeBalance();

    /**
     * Updates the balance with the given web3requests
     */
    void updateBalance(IWeb3Requests web3Requests);

    /**
     * Sign locally the transaction with the raw transaction and
     * returns the signature
     */
    String signTransaction(RawTransaction rawTransaction);
}
