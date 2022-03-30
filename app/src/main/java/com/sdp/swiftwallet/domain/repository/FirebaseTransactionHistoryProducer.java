package com.sdp.swiftwallet.domain.repository;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseTransactionHistoryProducer implements TransactionHistoryProducer, Parcelable {
    private static final String COLLECTION_NAME = "transactions";
    private static final String AMOUNT_KEY = "amount";
    public static final String CURRENCY_KEY = "currency";
    private static final String WALLET_1_KEY = "wallet1";
    private static final String WALLET_2_KEY = "wallet2";
    private static final String TRANSACTION_ID_KEY = "id";
    private final List<TransactionHistorySubscriber> subscribers = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final FirebaseFirestore db;

    //TODO figure out better way to store currencies with Firestore
    private final static Map<String, Currency> currencyMap = new HashMap<>();
    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    static {
        currencyMap.put(CURR_1.getSymbol(), CURR_1);
        currencyMap.put(CURR_2.getSymbol(), CURR_2);
        currencyMap.put(CURR_3.getSymbol(), CURR_3);
        currencyMap.put(CURR_4.getSymbol(), CURR_4);
    }

    public FirebaseTransactionHistoryProducer(FirebaseFirestore db) {
        this.db = db;
        initSnapshotListener();
    }

    protected FirebaseTransactionHistoryProducer(Parcel in) {
    }

    public static final Creator<FirebaseTransactionHistoryProducer> CREATOR = new Creator<FirebaseTransactionHistoryProducer>() {
        @Override
        public FirebaseTransactionHistoryProducer createFromParcel(Parcel in) {
            return new FirebaseTransactionHistoryProducer(in);
        }

        @Override
        public FirebaseTransactionHistoryProducer[] newArray(int size) {
            return new FirebaseTransactionHistoryProducer[size];
        }
    };

    private void initSnapshotListener() {
        db.collection(COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                transactions.clear();

                for (DocumentSnapshot snapshot : documentSnapshots) {
                    double amount = snapshot.getDouble(AMOUNT_KEY);
                    Currency curr = currencyMap.get(snapshot.getString(CURRENCY_KEY));
                    String myWall = snapshot.getString(WALLET_1_KEY);
                    String theirWall = snapshot.getString(WALLET_2_KEY);
                    int transactionId = snapshot.getLong(TRANSACTION_ID_KEY).intValue();

                    Transaction t = new Transaction.Builder()
                            .setAmount(amount)
                            .setCurr(curr)
                            .setMyWallet(myWall)
                            .setTheirWallet(theirWall)
                            .setId(transactionId)
                            .build();

                    transactions.add(t);
                }

                alertAll();
            }
        });
    }

    private boolean alertAll() {
        for (TransactionHistorySubscriber subscriber : subscribers) {
            subscriber.receiveTransactions(transactions);
        }
        return true;
    }

    @Override
    public boolean subscribe(TransactionHistorySubscriber subscriber) {
        if (subscriber != null) {
            return subscribers.add(subscriber) && alertAll();
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
