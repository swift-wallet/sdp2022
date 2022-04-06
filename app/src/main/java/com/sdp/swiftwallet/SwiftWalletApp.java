package com.sdp.swiftwallet;

import android.app.Application;

import androidx.annotation.Nullable;

import com.sdp.swiftwallet.domain.model.wallet.IWallets;
import com.sdp.swiftwallet.domain.model.wallet.Wallets;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SwiftWalletApp extends Application {
    private TransactionHistoryProducer transactionHistoryProducer = null;
    private Wallets wallets = null;

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

    /**
     * Getter for the TransactionHistoryProducer
     *
     * @return the TransactionHistoryProducer
     */
    @Nullable
    public TransactionHistoryProducer getTransactionHistoryProducer() {
        return transactionHistoryProducer;
    }

    /**
     * Setter for the TransactionHistoryProducer
     *
     * @param transactionHistoryProducer the new TransactionHistoryProducer
     */
    public void setTransactionHistoryProducer(TransactionHistoryProducer transactionHistoryProducer) {
        if (transactionHistoryProducer == null) {
            throw new IllegalArgumentException("Null HistoryProducer");
        }
        this.transactionHistoryProducer = transactionHistoryProducer;
    }
}
