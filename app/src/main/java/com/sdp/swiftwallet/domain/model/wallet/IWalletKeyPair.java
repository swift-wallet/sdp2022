package com.sdp.swiftwallet.domain.model.wallet;

import java.math.BigInteger;

public interface IWalletKeyPair {
    public String getHexPublicKey();
    public int getID();
    public BigInteger getNativeBalance();
    public void updateBalance();
}
