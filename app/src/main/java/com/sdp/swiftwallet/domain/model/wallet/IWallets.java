package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

public interface IWallets {
    IWalletKeyPair[] getWallets();
    IWalletKeyPair getWalletFromId(int id);
    public int getCounter();
    public void saveCounter(Context context);
    public int generateWallet();
}
