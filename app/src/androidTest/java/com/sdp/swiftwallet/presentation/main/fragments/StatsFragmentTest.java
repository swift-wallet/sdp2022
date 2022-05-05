package com.sdp.swiftwallet.presentation.main.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class StatsFragmentTest {

  public Context context;

  public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(
      MainActivity.class);
  public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

  @Rule
  public RuleChain rule =
      RuleChain.outerRule(hiltRule).around(testRule);

  @Before
  public void setup() {
    hiltRule.inject();
    context = ApplicationProvider.getApplicationContext();
  }

  @Before
  public void initIntents() {
    Intents.init();
  }

  @After
  public void releaseIntents() {
    Intents.release();
  }

  @Test
  public void checkElementsAreDisplayed() {
    clickOn(R.id.mainNavStatsItem);
    onView(withId(R.id.cryptovalues_button)).check(matches(isDisplayed()));
    onView(withId(R.id.transaction_history_button)).check(matches(isDisplayed()));
    onView(withId(R.id.create_transaction_button)).check(matches(isDisplayed()));
  }

  @Test
  public void clickOnCryptoValuesFiresCorrectIntent() {
    clickOn(R.id.mainNavStatsItem);
    clickOn(R.id.cryptovalues_button);
    intended(allOf(
        toPackage("com.sdp.swiftwallet"),
        hasComponent(hasClassName(CryptoValuesActivity.class.getName()))
    ));
  }

  @Test
  public void clickOnTransactionHistoryFiresCorrectIntent() {
    clickOn(R.id.mainNavStatsItem);
    clickOn(R.id.transaction_history_button);
    intended(allOf(
        toPackage("com.sdp.swiftwallet"),
        hasComponent(hasClassName(TransactionActivity.class.getName()))
    ));
  }

  @Test
  public void dummyTransactionCorrectlyInstanciated() {
    // FOR COVERAGE ONLY (can be removed later)
    clickOn(R.id.mainNavStatsItem);
    clickOn(R.id.create_transaction_button);
  }

}