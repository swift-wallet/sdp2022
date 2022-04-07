package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

public interface IWallets {
    IWalletKeyPair[] getWallets();
    IWalletKeyPair getWalletFromId(int id);
    int getCounter();
    void saveCounter(Context context);
    int generateWallet();
    String[] getAddresses();
}
