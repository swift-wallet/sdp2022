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
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseApp;
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

  public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
  public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

  @Rule
  public RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

  // Counter for idling resources in StatsFragment
  CountingIdlingResource mIdlingResource;

  @Before
  public void setUp() {
    hiltRule.inject();
    context = ApplicationProvider.getApplicationContext();
    Intents.init();
  }

  public Intent setupReset(){
    return new Intent(context, MainActivity.class);
  }

  @After
  public void tearDown() {
    Intents.release();
    // Unregister the idling resource
    IdlingRegistry.getInstance().unregister(mIdlingResource);
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
    try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
      scenario.onActivity(activity -> {
        mIdlingResource = activity.getIdlingResource();
      });
      IdlingRegistry.getInstance().register(mIdlingResource);

      // FOR COVERAGE ONLY (can be removed later)
      clickOn(R.id.mainNavStatsItem);
      clickOn(R.id.create_transaction_button);
    }
  }
}