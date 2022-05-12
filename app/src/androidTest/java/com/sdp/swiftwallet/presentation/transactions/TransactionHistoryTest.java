package com.sdp.swiftwallet.presentation.transactions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import androidx.cardview.widget.CardView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Checks;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class TransactionHistoryTest {

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
    }

    private final static double AMOUNT_BOUND = 100;

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
        onView(withId(R.id.transaction_historyButton)).perform(click());
    }

    @Test
    public void recyclerViewIsDisplayedInHistoryFragment() {
        onView(withId(R.id.transaction_historyButton)).perform(click());
        onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.transaction_recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void addingANewTransactionDisplaysItInHistory() {
        Random r = new Random();

        double amount = r.nextDouble() * AMOUNT_BOUND;
        amount -= r.nextDouble() * AMOUNT_BOUND;
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        int id = r.nextInt(Integer.MAX_VALUE);

        Transaction t = new Transaction(amount, curr, MY_WALL, THEIR_WALL, id);

        producer.addTransaction(t);
        producer.alertAll();

        String idString = "Transaction ID #" + id;
        String amountString = String.format(
                Locale.US,
                "%.1f %s",
                t.getAmount(), t.getSymbol()
        );
        String descriptionString = t.toString();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText(idString)).check(matches(isDisplayed()));
        onView(withText(amountString)).check(matches(isDisplayed()));
        onView(withText(descriptionString)).check(matches(isDisplayed()));
    }

    @Test
    public void positiveTransactionIsGreen() {
        Transaction t = new Transaction(10, CURR_1, MY_WALL, THEIR_WALL, 0);

        producer.addTransaction(t);
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withBgColor(Color.parseColor("#4CAF50"))).check(matches(isDisplayed()));
    }

    @Test
    public void smallestPositiveIsGreen() {
        double amount = Double.MIN_VALUE;
        Transaction t = new Transaction(amount, CURR_1, MY_WALL, THEIR_WALL, 0);

        producer.addTransaction(t);
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(Color.parseColor("#4CAF50"))).check(matches(isDisplayed()));
    }

    @Test
    public void largestPositiveIsGreen() {
        double amount = AMOUNT_BOUND;
        Transaction t = new Transaction(amount, CURR_1, MY_WALL, THEIR_WALL, 0);

        producer.addTransaction(t);
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(Color.parseColor("#4CAF50"))).check(matches(isDisplayed()));
    }

    @Test
    public void negativeTransactionIsRed() {
        Transaction t = new Transaction(-10, CURR_1, MY_WALL, THEIR_WALL, 0);

        producer.addTransaction(t);
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withBgColor(Color.parseColor("#F44336"))).check(matches(isDisplayed()));
    }

    @Test
    public void smallestNegativeIsRed() {
        double amount = -Double.MIN_VALUE;
        Transaction t = new Transaction(amount, CURR_1, MY_WALL, THEIR_WALL, 0);

        producer.addTransaction(t);
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withBgColor(Color.parseColor("#F44336"))).check(matches(isDisplayed()));
    }

    @Test
    public void largestNegativeIsRed() {
        double amount = -AMOUNT_BOUND;
        Transaction t = new Transaction(amount, CURR_1, MY_WALL, THEIR_WALL, 0);

        producer.addTransaction(t);
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(Color.parseColor("#F44336"))).check(matches(isDisplayed()));
    }

    public static Matcher<View> withBgColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, CardView>(CardView.class) {

            @Override
            protected boolean matchesSafely(CardView item) {
                return color == (item.getCardBackgroundColor().getDefaultColor());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }
        };
    }
}
