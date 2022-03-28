package com.sdp.swiftwallet.domain.model.wallet;

import com.sdp.swiftwallet.data.repository.Web3Requests;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class WalletKeyPair {
    private ECKeyPair keyPair;
    private String hexPublicKey;
    private int ID;
    private BigInteger nativeBalance = BigInteger.ZERO;
    private Web3Requests web3Requests;

    private WalletKeyPair(ECKeyPair keyPair, String hexPublicKey, int ID, Web3Requests web3Requests){
        this.keyPair = keyPair;
        this.hexPublicKey = hexPublicKey;
        this.ID = ID;
        this.web3Requests = web3Requests;
        updateBalance();
    }
    public static WalletKeyPair fromKeyPair(ECKeyPair keyPair, int ID, Web3Requests web3Requests){
        byte[] encodedPK = Hash.sha3(keyPair.getPublicKey().toString(16).getBytes());
        byte[] finalPK = new byte[20];
        System.arraycopy(encodedPK, 12, finalPK, 0, 20);
        return new WalletKeyPair(keyPair, Numeric.toHexString(finalPK), ID, web3Requests);
    }
    private void updateBalance() {
        web3Requests.getFutureBalanceOf(this.hexPublicKey).thenAccept((nativeBalance) -> { this.nativeBalance = nativeBalance; });
    }
    public BigInteger getNativeBalance(){ return nativeBalance; }
    public int getID() {
        return ID;
    }
    public String getHexPublicKey() {
        return hexPublicKey;
    }
}
