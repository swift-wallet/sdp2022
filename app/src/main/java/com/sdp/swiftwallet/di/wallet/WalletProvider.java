package com.sdp.swiftwallet.di.wallet;

import android.content.Context;

import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

/**
 * Wallet provider
 */
public class WalletProvider {

    private Wallets wallets = null;
    private final Context context;

    /**
     * Create t
     * @param context
     */
    public WalletProvider(Context context){
        this.context = context;
        hasWallets();
    }

    public Wallets getWallets() {
        return wallets;
    }

    /**
     * Check if there are currently some wallets
     */
    public boolean hasWallets() {
        if (wallets == null && SeedGenerator.hasSeed(context)) {
            wallets = SeedGenerator.recoverWallets(context);
        }
        return wallets != null;
    }
}
