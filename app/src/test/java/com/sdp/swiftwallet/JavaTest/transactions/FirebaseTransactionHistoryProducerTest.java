//package com.sdp.swiftwallet.JavaTest.transactions;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertThrows;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.sdp.swiftwallet.SwiftWalletApp;
//import com.sdp.swiftwallet.domain.model.Currency;
//import com.sdp.swiftwallet.domain.model.Transaction;
//import com.sdp.swiftwallet.domain.repository.FirebaseTransactionHistoryProducer;
//import com.sdp.swiftwallet.domain.repository.TransactionHistorySubscriber;
//import com.sdp.swiftwallet.presentation.main.MainActivity;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import java.util.List;
//
////TODO Test with a mock of Firestore
//
//@RunWith(JUnit4.class)
//public class FirebaseTransactionHistoryProducerTest {
//    private final static Currency CURR = new Currency("Dummy", "DUM", 9.5);
//    private final static String MY_WALL = "MY_WALL";
//    private final static String THEIR_WALL = "THEIR_WALL";
//
//    private FirebaseTransactionHistoryProducer producer;
//    private static boolean wasPinged;
//    private SwiftWalletApp app;
//
//    @Before
//    public void initProducer() {
//        producer = new FirebaseTransactionHistoryProducer(null);
//        wasPinged = false;
//    }
//
//    @Test
//    public void producerThrowsIAEOnNullSubscribe() {
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            producer.subscribe(null);
//        });
//
//        assertThat(exception.getMessage(), is("Cannot subscribe a null subscriber"));
//    }
//
//    @Test
//    public void producerThrowsIAEOnNullUnsubscribe() {
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            producer.unsubscribe(null);
//        });
//
//        assertThat(exception.getMessage(), is("Cannot unsubscribe a null subscriber"));
//    }
//
//    @Test
//    public void canAddSubscriber() {
//        boolean wasAdded = producer.subscribe(new DummySubscriber());
//
//        assertThat(wasAdded, is(true));
//    }
//
//    @Test
//    public void removeNonSubscriberReturnsTrue() {
//        boolean wasRemoved = producer.unsubscribe(new DummySubscriber());
//
//        assertThat(wasRemoved, is(true));
//    }
//
//    @Test
//    public void canRemoveSubscriber() {
//        DummySubscriber subscriber = new DummySubscriber();
//        producer.subscribe(subscriber);
//        boolean wasRemoved = producer.unsubscribe(subscriber);
//
//        assertThat(wasRemoved, is(true));
//    }
//
//    @Test
//    public void subscriberGetsPingedOnSubscribe() {
//        producer.subscribe(new DummySubscriber());
//
//        assertThat(wasPinged, is(true));
//    }
//
//    public static class DummySubscriber implements TransactionHistorySubscriber {
//        @Override
//        public void receiveTransactions(List<Transaction> transactions) {
//            wasPinged = true;
//        }
//    }
//}
