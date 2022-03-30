package com.sdp.swiftwallet;

import android.app.Application;

import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;

public class SwiftWalletApp extends Application {
    private TransactionHistoryProducer transactionHistoryProducer = null;

    /**
     * Getter for the TransactionHistoryProducer
     *
     * @return the TransactionHistoryProducer
     */
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
