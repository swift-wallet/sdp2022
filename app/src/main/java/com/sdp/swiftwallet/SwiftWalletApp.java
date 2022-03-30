package com.sdp.swiftwallet;

import android.app.Application;

import com.sdp.swiftwallet.domain.repository.FirebaseTransactionHistoryProducer;

public class SwiftWalletApp extends Application {
    private FirebaseTransactionHistoryProducer transactionHistoryProducer;

    /**
     * Getter for the TransactionHistoryProducer
     *
     * @return the TransactionHistoryProducer
     */
    public FirebaseTransactionHistoryProducer getTransactionHistoryProducer() {
        return transactionHistoryProducer;
    }

    /**
     * Setter for the TransactionHistoryProducer
     *
     * @param transactionHistoryProducer the new TransactionHistoryProducer
     */
    public void setTransactionHistoryProducer(FirebaseTransactionHistoryProducer transactionHistoryProducer) {
        this.transactionHistoryProducer = transactionHistoryProducer;
    }
}
