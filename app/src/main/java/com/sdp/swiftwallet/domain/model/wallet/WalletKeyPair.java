package com.sdp.swiftwallet.domain.model.wallet;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public class WalletKeyPair {
    private ECKeyPair keyPair;
    private String hexPublicKey;
    private int ID;
    public WalletKeyPair(ECKeyPair keyPair, int ID){
        byte[] encodedPK = Hash.sha3(keyPair.getPublicKey().toString(16).getBytes());
        byte[] finalPK = new byte[20];
        System.arraycopy(encodedPK, 12, finalPK, 0, 20);
        this.keyPair = keyPair;
        this.hexPublicKey = Numeric.toHexString(finalPK);
        this.ID = ID;
    }
    public WalletKeyPair(ECKeyPair keyPair, String hexPublicKey, int ID){
        this.keyPair = keyPair;
        this.hexPublicKey = hexPublicKey;
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }
    public String getHexPublicKey() {
        return hexPublicKey;
    }
}
