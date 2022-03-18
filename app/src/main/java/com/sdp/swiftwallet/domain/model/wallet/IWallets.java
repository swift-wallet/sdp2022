package com.sdp.swiftwallet.domain.model.wallet;

public interface IWallets {
    public WalletKeyPair[] getWallets();
    public WalletKeyPair getWalletFromId(int id);
}
