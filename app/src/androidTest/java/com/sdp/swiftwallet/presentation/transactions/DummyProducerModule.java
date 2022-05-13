package com.sdp.swiftwallet.presentation.transactions;

import com.sdp.swiftwallet.di.transaction.TransactionHistoryProducerModule;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
        components = SingletonComponent.class,
        replaces = TransactionHistoryProducerModule.class
)
public class DummyProducerModule {

    @Provides
    public static TransactionHistoryProducer provideProducer() {
        return DummyProducer.INSTANCE;
    }
}
