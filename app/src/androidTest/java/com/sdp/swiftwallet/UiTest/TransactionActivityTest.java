package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.TransactionHistorySubscriber;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO fix tests to work with new HistoryGenerator interface

@RunWith(AndroidJUnit4.class)
public class TransactionActivityTest {
    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Currency> currencyList = new ArrayList<>();

    static {
        currencyList.add(CURR_1);
        currencyList.add(CURR_2);
        currencyList.add(CURR_3);
        currencyList.add(CURR_4);
    }

    private Intent i;
    private DummyHistoryProducer producer;

    @Before
    public void setupIntent() {
        i = new Intent(ApplicationProvider.getApplicationContext(), TransactionActivity.class);
        producer = new DummyHistoryProducer();
        ((SwiftWalletApp) ApplicationProvider
                .getApplicationContext())
                .setTransactionHistoryProducer(producer);
    }

//    @Test
//    public void foreverTest() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            for(;;);
//        }
//    }

    @Test
    public void historyButtonIsDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void statsButtonIsDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void fragmentLayoutIsDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_flFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void activityStartsWithHistoryFragmentDisplayed() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void historyButtonDisplaysHistoryFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyButton)).perform(click());
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void statsButtonDisplaysStatsFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void canSwitchBackToHistoryFragmentAfterGoingToStats() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            onView(withId(R.id.transaction_historyButton)).perform(click());
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void recyclerViewIsDisplayedInHistoryFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_historyButton)).perform(click());
            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.transaction_recyclerView)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void pieChartIsDisplayedInStatsFragment() {
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
            onView(withId(R.id.transaction_statsButton)).perform(click());
            onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.transaction_pieChart)).check(matches(isDisplayed()));
        }
    }

    public static class DummyHistoryProducer implements TransactionHistoryProducer {
        private List<TransactionHistorySubscriber> subscribers = new ArrayList<>();
        private List<Transaction> transactions = new ArrayList<>();

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
}
