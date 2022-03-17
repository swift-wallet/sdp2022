package com.sdp.swiftwallet.domain.model;

import java.util.List;

/**
 * Transaction History Generator interface
 * Used to generate transaction histories
 */
public interface TransactionHistoryGenerator {
    /**
     * Getter for the list of previous transactions
     * @return a list of past transactions
     */
    public List<Transaction> getTransactionHistory();
}
