package com.sdp.swiftwallet.presentation.transactions;

import com.sdp.swiftwallet.domain.model.transaction.Transaction;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistorySubscriber;

import java.util.ArrayList;
import java.util.List;

public class DummyProducer implements TransactionHistoryProducer {

    public static DummyProducer INSTANCE = new DummyProducer();

    private final List<TransactionHistorySubscriber> subscribers = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public boolean subscribe(TransactionHistorySubscriber subscriber) {
        subscriber.receiveTransactions(transactions);
        return subscribers.add(subscriber);
    }

    @Override
    public boolean unsubscribe(TransactionHistorySubscriber subscriber) {
        if (subscribers.contains(subscriber)) {
            return subscribers.remove(subscriber);
        }
        return true;
    }

    public void alertAll() {
        for (TransactionHistorySubscriber subscriber : subscribers) {
            subscriber.receiveTransactions(transactions);
        }
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}
