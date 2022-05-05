package com.sdp.swiftwallet;

import android.app.Application;
import android.content.Context;

import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;

import com.sdp.swiftwallet.domain.model.wallet.IWallets;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SwiftWalletApp extends Application {
    private final TransactionHistoryProducer transactionHistoryProducer = null;
    private IWallets wallets = null;
    private User currUser = null;

    private static Context context;

    public SwiftWalletApp(){
        super();
    }

    /**
     * Getter for the wallets object
     * @return the wallets object as an interface
     */
    public IWallets getWallets() {
        return wallets;
    }

    /**
     *  True if the seed is configured
     */
    public boolean hasSeed(){
        return wallets != null;
    }
    /**
     *  Recovers the generated wallets with the seed if existent
     *  Otherwise just set the wallets object to null
     */
    public void updateWallets(){
        if(SeedGenerator.hasSeed(this.getApplicationContext())){
            wallets = SeedGenerator.recoverWallets(super.getBaseContext());
        }else{
            wallets = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * Getter for the ApplicationContext
     *
     * @return the Context of this Application
     */
    public static Context getAppContext() {
        return SwiftWalletApp.context;
    }

    /**
     * Getter for the currently signed in user
     * @return
     */
    public User getCurrUser() {
        return currUser;
    }

    /**
     * Setter for the current user
     * @param user the user which is signed in
     */
    public void setCurrUser(User user) {
        this.currUser = user;
    }
}
