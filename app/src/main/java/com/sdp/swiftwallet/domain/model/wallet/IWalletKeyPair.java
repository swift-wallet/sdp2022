package com.sdp.swiftwallet.domain.model.wallet;

import java.math.BigInteger;

public interface IWalletKeyPair {
    String getHexPublicKey();
    int getID();
    BigInteger getNativeBalance();
    void updateBalance();
}
