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
    private final String hexPublicKey;
    private final int ID;
    private BigInteger nativeBalance = BigInteger.ZERO;

    private WalletKeyPair(ECKeyPair keyPair, String hexPublicKey, int ID){
        this.credentials = Credentials.create(keyPair);
        this.hexPublicKey = hexPublicKey;
        this.ID = ID;
    }

    /**
     * Creates a wallet key pair from a cryptographic ECDSA key pair
     * @param keyPair the keypair
     * @param ID the id of this wallet
     * @return the WalletKeyPair object
     */
    public static WalletKeyPair fromKeyPair(ECKeyPair keyPair, int ID){
        byte[] encodedPK = Hash.sha3(keyPair.getPublicKey().toString(16).getBytes());
        byte[] finalPK = new byte[20];
        System.arraycopy(encodedPK, 12, finalPK, 0, 20);
        return new WalletKeyPair(keyPair, Numeric.toHexString(finalPK), ID);
    }

    /**
     * Updates this wallet's native balance
     */
    public void updateBalance(IWeb3Requests web3Requests) {
        web3Requests.getBalanceOf(this.hexPublicKey).thenAccept((nativeBalance) -> { this.nativeBalance = nativeBalance; });
    }

    public String signTransaction(RawTransaction rawTransaction) {
        return Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, this.credentials));
    }

    public BigInteger getNativeBalance(){ return nativeBalance; }
    public int getID() { return ID; }
    public String getHexPublicKey() { return hexPublicKey; }
}
