package com.sdp.swiftwallet.domain.repository.transaction;

import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistorySubscriber;

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

    /**
     * Method used by TransactionHistorySubscribers to unregister for updates
     *
     * @param subscriber a subscriber that should be unsubscribed from this producer
     * @return
     */
    boolean unsubscribe(TransactionHistorySubscriber subscriber);
}
