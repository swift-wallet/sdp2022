package com.sdp.swiftwallet.domain.repository.firebase;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.currency.CurrencyBank;
import com.sdp.swiftwallet.domain.model.currency.SwiftWalletCurrencyBank;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;

import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistorySubscriber;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * TransactionHistoryProducer implementation with a Firestore database
 */
public class FirebaseTransactionHistoryProducer implements TransactionHistoryProducer {

    private static final String COLLECTION_NAME = "transactions2";

    private static final String AMOUNT_KEY = "amount";
    private static final String CURRENCY_KEY = "currency";

    private static final String TRANSACTION_ID_KEY = "id";
    private static final String DATE_KEY = "date";

    private static final String SENDER_WALLET_KEY = "senderWallet";
    private static final String RECEIVER_WALLET_KEY = "receiverWallet";

    private static final String SENDER_ID_KEY = "senderID";
    private static final String RECEIVER_ID_KEY = "receiverID";

    private final List<TransactionHistorySubscriber> subscribers = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final FirebaseFirestore db;

    public FirebaseTransactionHistoryProducer(FirebaseFirestore db) {
        this.db = db;
        initSnapshotListener();
    }

    /**
     * Register a snapshot listener to the Firestore database
     */
    private void initSnapshotListener() {
        db.collection(COLLECTION_NAME).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FirebaseTransactionHistoryProducer", error.getMessage());
                    return;
                }

                transactions.clear();

                for (DocumentSnapshot snapshot : documentSnapshots) {
                    Transaction.Builder builder = new Transaction.Builder();

                    double amount = snapshot.getDouble(AMOUNT_KEY);
                    Map<String, Object> currMap = (Map<String, Object>) snapshot.get(CURRENCY_KEY);
                    Currency curr = new Currency(
                            (String) currMap.get("name"),
                            (String) currMap.get("symbol"),
                            (Double) currMap.get("value"));
                    builder.setAmountAndCurrency(amount, curr);

                    int transactionId = snapshot.getLong(TRANSACTION_ID_KEY).intValue();
                    Date date = snapshot.getDate(DATE_KEY);
                    builder.setMetadata(transactionId, date);

                    String senderWallet = snapshot.getString(SENDER_WALLET_KEY);
                    String receiverWallet = snapshot.getString(RECEIVER_WALLET_KEY);
                    builder.setWalletIDs(senderWallet, receiverWallet);

                    String senderID = snapshot.getString(SENDER_ID_KEY);
                    if (!senderID.equals("")) {
                        builder.setSenderID(senderID);
                    }

                    String receiverID = snapshot.getString(RECEIVER_ID_KEY);
                    if (!receiverID.equals("")) {
                        builder.setReceiverID(receiverID);
                    }

                    transactions.add(builder.build());
                }

                alertAll();
            }
        });
    }

    /**
     * Alert all subscribers of this producer of new transactions
     */
    private void alertAll() {
        for (TransactionHistorySubscriber subscriber : subscribers) {
            subscriber.receiveTransactions(transactions);
        }
    }

    /**
     * Alert a single subscriber of new transactions
     *
     * @param subscriber the subscriber to alert
     */
    private void alert(TransactionHistorySubscriber subscriber) {
        subscriber.receiveTransactions(transactions);
    }

    @Override
    public boolean subscribe(TransactionHistorySubscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("Cannot subscribe a null subscriber");
        }
        if (subscribers.add(subscriber)) {
            alert(subscriber);
            return true;
        }
        return false;
    }

    @Override
    public boolean unsubscribe(TransactionHistorySubscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("Cannot unsubscribe a null subscriber");
        }
        if (subscribers.contains(subscriber)) {
            return subscribers.remove(subscriber);
        }
        return true;
    }
}
