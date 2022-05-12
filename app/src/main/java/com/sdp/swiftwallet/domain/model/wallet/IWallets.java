package com.sdp.swiftwallet.domain.model.wallet;

import android.content.Context;

/**
 * Represents a wallet
 */
public interface IWallets {

    /**
     * @return the list of existing wallets
     */
    IWalletKeyPair[] getWallets();

    /**
     * @return returns the wallet with id "id"
     */
    IWalletKeyPair getWalletFromId(int id);

    int getCounter();

    /**
     * Saves the counter in given context
     */
    void saveCounter(Context context);

    /**
     * Generate a wallet and add to the list of wallet
     */
    int generateWallet();

    /**
     * @return the addresses of the wallet
     */
    String[] getAddresses();
}
