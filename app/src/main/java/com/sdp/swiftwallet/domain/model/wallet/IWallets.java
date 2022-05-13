package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

/**
 * Represents a wallet
 */
public interface IWallets {

    /**
     * @return returns the wallet with id "id"
     */
    IWalletKeyPair getWalletFromId(int id);

    int getCounter();

    /**
     * Saves the counter in given context
     */
    void saveCounter(Context context);
    IWalletKeyPair generateWallet();

    String[] getAddresses();
    void importKeyPair(String privateKey);
    IWalletKeyPair importKeyPair(Context context, String privateKey);
}
