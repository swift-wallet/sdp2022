package com.sdp.swiftwallet.domain.repository;

import com.sdp.swiftwallet.domain.model.Transaction;

import java.util.List;

/**
 * Asynchronous Transaction History Producer
 * Used to produce transaction histories and pass them to subscribers
 */
public interface TransactionHistoryProducer {
    /**
     * Method used by TransactionHistorySubscribers to register for updates
     *
     * @param subscriber a subscriber that this producer will need to update
     * @return
     */
    boolean subscribe(TransactionHistorySubscriber subscriber);
}
