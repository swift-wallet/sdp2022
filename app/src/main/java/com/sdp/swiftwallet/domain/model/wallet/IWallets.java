package com.sdp.swiftwallet.domain.model.wallet;

public interface IWallets {
    WalletKeyPair[] getWallets();
    WalletKeyPair getWalletFromId(int id);
}
