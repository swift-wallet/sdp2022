package com.sdp.swiftwallet.javaTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.TransactionHistorySubscriber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SwiftWalletAppTest {
    private SwiftWalletApp app;

    @Before
    public void createApp() {
        app = new SwiftWalletApp();
    }

    @Test
    public void settingNullHistoryProducerThrowsIAE() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> app.setTransactionHistoryProducer(null)
                );

        assertThat(exception.getMessage(), is("Null HistoryProducer"));
    }

    @Test
    public void settingAndGettingHistoryProducerReturnsSameProvider() {
        TransactionHistoryProducer producer = new TransactionHistoryProducer() {
            @Override
            public boolean subscribe(TransactionHistorySubscriber subscriber) {
                return false;
            }

            @Override
            public boolean unsubscribe(TransactionHistorySubscriber subscriber) {
                return false;
            }
        };

        app.setTransactionHistoryProducer(producer);
        assertThat(app.getTransactionHistoryProducer(), is(producer));
    }
}
