package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

public interface IWallets {
    int getCounter();
    void saveCounter(Context context);
    IWalletKeyPair generateWallet();
    String[] getAddresses();
    void importKeyPair(String privateKey);
    IWalletKeyPair importKeyPair(Context context, String privateKey);
}
