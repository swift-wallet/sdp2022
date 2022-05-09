package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

public interface IWallets {
    int getCounter();
    void saveCounter(Context context);
    int generateWallet();
    String[] getAddresses();
}
