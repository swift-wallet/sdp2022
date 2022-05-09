package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * A wallet key pair object
 */
public class WalletKeyPair implements IWalletKeyPair{
    private final Credentials credentials;
    private BigInteger nativeBalance = BigInteger.ZERO;

    private WalletKeyPair(Credentials _credentials){
        this.credentials = _credentials;
    }

    /**
     * Creates a wallet key pair from a cryptographic ECDSA key pair
     * @param keyPair the keypair
     * @return the WalletKeyPair object
     */
    public static WalletKeyPair fromKeyPair(ECKeyPair keyPair){
        return new WalletKeyPair(Credentials.create(keyPair));
    }

    /**
     * Creates a wallet key pair from a cryptographic ECDSA private key
     * @param privateKey the private key
     * @return the WalletKeyPair object
     */
    public static WalletKeyPair fromPrivateKey(String privateKey){
        return new WalletKeyPair(Credentials.create(privateKey));
    }

    /**
     * Updates this wallet's native balance
     */
    public void updateBalance(IWeb3Requests web3Requests) {
        web3Requests.getBalanceOf(credentials.getAddress()).thenAccept((nativeBalance) -> { this.nativeBalance = nativeBalance; });
    }

    public String signTransaction(RawTransaction rawTransaction) {
        return Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, this.credentials));
    }

    public BigInteger getNativeBalance(){ return nativeBalance; }
    public String getHexPublicKey() { return credentials.getAddress(); }
}
