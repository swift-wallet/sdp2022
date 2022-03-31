package com.sdp.swiftwallet.domain.repository;

import com.sdp.swiftwallet.domain.model.Transaction;

import java.util.List;

/**
 * Asynchronous Transaction History Subscriber
 * Used to receive transaction histories from producers
 */
public interface TransactionHistorySubscriber {
    /**
     * Method used by producers to pass on the new data
     *
     * @param transactions a history list of past transactions
     */
    void receiveTransactions(List<Transaction> transactions);
}
