package com.sdp.swiftwallet.di.transaction;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.swiftwallet.domain.repository.firebase.FirebaseTransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Transaction history module for hilt
 */
@Module
@InstallIn(SingletonComponent.class)
public class TransactionHistoryProducerModule {

    /**
     * @return producer for Transactions
     */
    @Provides
    public static TransactionHistoryProducer provideProducer(FirebaseFirestore db) {
        return new FirebaseTransactionHistoryProducer(db);
    }

}