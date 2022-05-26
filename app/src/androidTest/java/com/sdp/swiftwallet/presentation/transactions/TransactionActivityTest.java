package com.sdp.swiftwallet.presentation.transactions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.di.transaction.TransactionHistoryProducerModule;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistorySubscriber;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import dagger.hilt.components.SingletonComponent;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class TransactionActivityTest {

    private static final DummyProducer producer = DummyProducer.INSTANCE;

    public ActivityScenarioRule<TransactionActivity> testRule = new ActivityScenarioRule<>(TransactionActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public final RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setup() {
        hiltRule.inject();
        producer.clearList();
    }

    @Test
    public void historyButtonIsDisplayed() {
        onView(withId(R.id.transaction_historyButton)).check(matches(isDisplayed()));
    }

    @Test
    public void statsButtonIsDisplayed() {
        onView(withId(R.id.transaction_statsButton)).check(matches(isDisplayed()));
    }

    @Test
    public void fragmentLayoutIsDisplayed() {
        onView(withId(R.id.transaction_flFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void activityStartsWithHistoryFragmentDisplayed() {
        onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void historyButtonDisplaysHistoryFragment() {
        onView(withId(R.id.transaction_historyButton)).perform(click());
        onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void statsButtonDisplaysStatsFragment() {
        onView(withId(R.id.transaction_statsButton)).perform(click());
        onView(withId(R.id.transaction_statsFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void canSwitchBackToHistoryFragmentAfterGoingToStats() {
        onView(withId(R.id.transaction_statsButton)).perform(click());
        onView(withId(R.id.transaction_historyButton)).perform(click());
        onView(withId(R.id.transaction_historyFragment)).check(matches(isDisplayed()));
    }
}
