package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO fix tests to work with new HistoryGenerator interface

//@RunWith(AndroidJUnit4.class)
//public class TransactionActivityTest {
//    private final static Currency CURR = new Currency("DumbCoin", "DUM", 9.5);
//    private final static String MY_WALL = "MY_WALL";
//    private final static String THEIR_WALL = "THEIR_WALL";
//    private final static List<Transaction> list = new ArrayList<>();
//
//    static {
//        Random r = new Random();
//        for (int i = 0; i < 50; i++) {
//            double amount = -100 + r.nextDouble() * 200;
//            Transaction t = new Transaction(amount, CURR, MY_WALL, THEIR_WALL, i);
//            list.add(t);
//        }
//    }
//
//    private Intent i;
//
//    @Before
//    public void setupIntent() {
//        i = new Intent(ApplicationProvider.getApplicationContext(), TransactionActivity.class);
//        i.putExtra(
//                ApplicationProvider.getApplicationContext().getString(R.string.transactionHistoryGeneratorExtraKey),
//                (Parcelable) new DummyTransactionProducer()
//        );
//    }
//
//    @Test
//    public void historyButtonIsDisplayed() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_historyButton)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void statsButtonIsDisplayed() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_statsButton)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void fragmentLayoutIsDisplayed() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_flFragment)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void activityStartsWithHistoryFragmentDisplayed() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void historyButtonDisplaysHistoryFragment() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_historyButton)).perform(click());
//            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void statsButtonDisplaysStatsFragment() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_statsButton)).perform(click());
//            onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void canSwitchBackToHistoryFragmentAfterGoingToStats() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_statsButton)).perform(click());
//            onView(withId(R.id.transaction_historyButton)).perform(click());
//            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void recyclerViewIsDisplayedInHistoryFragment() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_historyButton)).perform(click());
//            onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
//            onView(withId(R.id.transaction_recyclerView)).check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void pieChartIsDisplayedInStatsFragment() {
//        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(i)) {
//            onView(withId(R.id.transaction_statsButton)).perform(click());
//            onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
//            onView(withId(R.id.transaction_pieChart)).check(matches(isDisplayed()));
//        }
//    }
//
//    public static class DummyTransactionProducer implements TransactionHistoryProducer, Parcelable {
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//        }
//
//        @Override
//        public List<Transaction> getTransactionHistory() {
//            return list;
//        }
//
//        public static final Parcelable.Creator<DummyTransactionProducer> CREATOR = new Parcelable.Creator<DummyTransactionProducer>() {
//            @Override
//            public DummyTransactionProducer createFromParcel(Parcel source) {
//                return new DummyTransactionProducer(source);
//            }
//
//            @Override
//            public DummyTransactionProducer[] newArray(int size) {
//                return new DummyTransactionProducer[size];
//            }
//        };
//
//        private DummyTransactionProducer(Parcel in) {
//        }
//
//        public DummyTransactionProducer() {
//        }
//    }
//}
