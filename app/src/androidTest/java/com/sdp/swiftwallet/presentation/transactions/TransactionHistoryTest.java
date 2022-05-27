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
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.signIn.DummyAuthenticator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class TransactionHistoryTest {

    private final static String SENDER_WALL = "SENDER_WALL";
    private final static String RECEIVER_WALL = "RECEIVER_WALL";

    private final static String OTHER_USER = "OTHER_USER";

    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);

    private final static List<Currency> currencyList = new ArrayList<>();
    public static final int GREEN_COLOR = Color.parseColor("#4CAF50");
    public static final int RED_COLOR = Color.parseColor("#F44336");
    public static final int GRAY_COLOR = Color.parseColor("#ABABAB");

    static {
        currencyList.add(CURR_1);
        currencyList.add(CURR_2);
        currencyList.add(CURR_3);
        currencyList.add(CURR_4);
    }

    private final static double AMOUNT_BOUND = 100;

    private static final DummyProducer producer = DummyProducer.INSTANCE;
    private static final User USER = new User("TEST", "asdf");
    DummyAuthenticator authenticator;

    public ActivityScenarioRule<TransactionActivity> testRule = new ActivityScenarioRule<>(TransactionActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public final RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setup() {
        hiltRule.inject();
        producer.clearList();
        authenticator = DummyAuthenticator.INSTANCE;
        authenticator.setCurrUser(USER);
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
        if (amount < 0) {
            amount += 1;
        }
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        int id = r.nextInt(Integer.MAX_VALUE);

        Transaction t = new Transaction(
                amount,
                curr,
                id,
                new Date(),
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.empty(),
                Optional.empty()
        );

        producer.addTransaction(t);
        producer.alertAll();

        String idString = "Transaction ID #" + id;
        String amountString = String.format(
                Locale.US,
                "%.1f %s",
                t.getAmount(), t.getCurr().getSymbol()
        );

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(idString)).check(matches(isDisplayed()));
        onView(withText(amountString)).check(matches(isDisplayed()));
    }

    @Test
    public void descriptionShowsWhenSenderIsSet() {
        Random r = new Random();

        double amount = r.nextDouble() * AMOUNT_BOUND;
        if (amount < 0) {
            amount += 1;
        }
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        int id = r.nextInt(Integer.MAX_VALUE);

        Transaction t = new Transaction(
                amount,
                curr,
                id,
                new Date(),
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.of(OTHER_USER),
                Optional.of(USER.getUid())
        );

        producer.addTransaction(t);
        producer.alertAll();

        String descriptionString = "From " + OTHER_USER;

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(descriptionString)).check(matches(isDisplayed()));
    }

    @Test
    public void descriptionShowsWhenReceiverIsSet() {
        Random r = new Random();

        double amount = r.nextDouble() * AMOUNT_BOUND;
        if (amount < 0) {
            amount += 1;
        }
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        int id = r.nextInt(Integer.MAX_VALUE);

        Transaction t = new Transaction(
                amount,
                curr,
                id,
                new Date(),
                SENDER_WALL,
                RECEIVER_WALL,
                Optional.of(USER.getUid()),
                Optional.of(OTHER_USER)
        );

        producer.addTransaction(t);
        producer.alertAll();

        String descriptionString = "To " + OTHER_USER;

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(descriptionString)).check(matches(isDisplayed()));
    }

    @Test
    public void receivingMoneyIsGreen() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = r.nextDouble() * AMOUNT_BOUND;
        if (amount < 0) {
            amount += 1;
        }
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        builder.setReceiverID(USER.getUid());

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(GREEN_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void receivingSmallestMoneyIsGreen() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = Double.MIN_VALUE;
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        builder.setReceiverID(USER.getUid());

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(GREEN_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void receivingLargestMoneyIsGreen() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = Double.MAX_VALUE;
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        builder.setReceiverID(USER.getUid());

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(GREEN_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void givingMoneyIsRed() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = r.nextDouble() * AMOUNT_BOUND;
        if (amount < 0) {
            amount += 1;
        }
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        builder.setSenderID(USER.getUid());

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(RED_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void givingSmallestMoneyIsRed() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = Double.MIN_VALUE;
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        builder.setSenderID(USER.getUid());

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(RED_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void givingLargestMoneyIsRed() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = Double.MAX_VALUE;
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        builder.setSenderID(USER.getUid());

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(RED_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void unknownUsersTransactionIsGray() {
        Random r = new Random();

        Transaction.Builder builder = new Transaction.Builder();

        double amount = r.nextDouble() * AMOUNT_BOUND;
        if (amount < 0) {
            amount += 1;
        }
        Currency curr = currencyList.get(r.nextInt(currencyList.size()));
        builder.setAmountAndCurrency(amount, curr);

        int id = r.nextInt(Integer.MAX_VALUE);
        Date date = new Date();
        builder.setMetadata(id, date);

        builder.setWalletIDs(SENDER_WALL, RECEIVER_WALL);

        producer.addTransaction(builder.build());
        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withBgColor(GRAY_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void greenTransactionAfterLargeListOfRedTransactionsIsGreen() throws InterruptedException {
        int nbTransactions = 100;

        for (int i = 0; i < nbTransactions; i++) {
            Transaction t = new Transaction(
                    10,
                    CURR_1,
                    i,
                    new Date(),
                    SENDER_WALL,
                    RECEIVER_WALL,
                    Optional.of(USER.getUid()),
                    Optional.empty()
            );
            producer.addTransaction(t);
        }

        producer.addTransaction(
                new Transaction(
                        10,
                        CURR_1,
                        nbTransactions,
                        new Date(),
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.empty(),
                        Optional.of(USER.getUid())
                )
        );

        producer.alertAll();

        for (int i = 0; i < nbTransactions; i += nbTransactions/10) {
            onView(withId(R.id.transaction_recyclerView))
                    .perform(RecyclerViewActions.scrollToPosition(i));
        }

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.scrollToPosition(nbTransactions));

        onView(withBgColor(GREEN_COLOR)).check(matches(isDisplayed()));
    }

    @Test
    public void greenTransactionBeforeLargeListOfRedTransactionsDoesntChangeColor() {
        int nbTransactions = 100;

        producer.addTransaction(
                new Transaction(
                        10,
                        CURR_1,
                        nbTransactions,
                        new Date(),
                        SENDER_WALL,
                        RECEIVER_WALL,
                        Optional.empty(),
                        Optional.of(USER.getUid())
                )
        );

        for (int i = 0; i < nbTransactions; i++) {
            Transaction t = new Transaction(
                    10,
                    CURR_1,
                    i,
                    new Date(),
                    SENDER_WALL,
                    RECEIVER_WALL,
                    Optional.of(USER.getUid()),
                    Optional.empty()
            );
            producer.addTransaction(t);
        }

        producer.alertAll();

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withBgColor(GREEN_COLOR)).check(matches(isDisplayed()));

        for (int i = 0; i < nbTransactions; i += nbTransactions/10) {
            onView(withId(R.id.transaction_recyclerView))
                    .perform(RecyclerViewActions.scrollToPosition(i));
        }

        onView(withId(R.id.transaction_recyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(withBgColor(GREEN_COLOR)).check(matches(isDisplayed()));
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
