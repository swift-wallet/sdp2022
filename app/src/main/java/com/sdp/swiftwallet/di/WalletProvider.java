package com.sdp.swiftwallet.di;

import android.app.Application;
import android.content.Context;

import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

public class WalletProvider {
    private Wallets wallets = null;
    private Context context;
    public WalletProvider(Context context){
        this.context = context;
        hasWallets();
    }

    public Wallets getWallets() {
        return wallets;
    }

    public boolean hasWallets() {
        if(wallets == null && SeedGenerator.hasSeed(context)){
            wallets = SeedGenerator.recoverWallets(context);
        }
        return wallets != null;
    }
}
