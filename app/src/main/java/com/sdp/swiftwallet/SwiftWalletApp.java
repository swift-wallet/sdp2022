package com.sdp.swiftwallet;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SwiftWalletApp extends Application {

    private TransactionHistoryProducer transactionHistoryProducer = null;

    private static Context context;

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
