package com.sdp.swiftwallet.presentation.transactions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class TransactionStatsTest {

    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";

    private static DummyProducer producer = DummyProducer.INSTANCE;

    public ActivityScenarioRule<TransactionActivity> testRule = new ActivityScenarioRule<>(TransactionActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public final RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setup() {
        hiltRule.inject();
        producer.clearList();
        onView(withId(R.id.transaction_statsButton)).perform(click());
    }

    @Test
    public void pieChartIsDisplayedInStatsFragment() {
        onView(withId(R.id.transaction_statsButton)).perform(click());
        onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.transaction_pieChart)).check(matches(isDisplayed()));
    }

    @Test
    public void addingANewTransactionDisplaysItInStats() {
        onView(withId(R.id.transaction_statsButton)).perform(click());
        Transaction t1 = new Transaction(2.2, CURR_2, MY_WALL, THEIR_WALL, 1);
        Transaction t2 = new Transaction(17.5, CURR_1, MY_WALL, THEIR_WALL, 2);
        Transaction t3 = new Transaction(-19, CURR_3, MY_WALL, THEIR_WALL, 3);
        Transaction t4 = new Transaction(22, CURR_4, MY_WALL, THEIR_WALL, 4);
        Transaction t5 = new Transaction(76, CURR_2, MY_WALL, THEIR_WALL, 5);
        producer.addTransaction(t1);
        producer.addTransaction(t2);
        producer.addTransaction(t3);
        producer.addTransaction(t4);
        producer.addTransaction(t5);
        producer.alertAll();
    }
}
