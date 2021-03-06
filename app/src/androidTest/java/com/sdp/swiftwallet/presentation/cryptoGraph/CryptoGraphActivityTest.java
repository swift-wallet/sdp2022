package com.sdp.swiftwallet.presentation.cryptoGraph;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.currency.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class CryptoGraphActivityTest {
    CountingIdlingResource mIdlingResource;
    Currency ethereum;

    public ActivityScenarioRule<CryptoGraphActivity> testRule = new ActivityScenarioRule<>(CryptoGraphActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @Before
    public void registerIdlingResource() {
        testRule.getScenario().onActivity(activity ->
                mIdlingResource = activity.getIdlingResource()
        );
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void closeSystemDialogs() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @After
    public void releaseIntents(){
        Intents.release();
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void changeCurrencyShowsETH(){
        onView(withId(R.id.idCurrencyToShow)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.idCurrencyToShow)).check(matches(withSpinnerText(containsString("ETH"))));
    }

    @Test
    public void changeCurrencyShowsBTC(){
        onView(withId(R.id.idCurrencyToShow)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.idCurrencyToShow)).check(matches(withSpinnerText(containsString("BTC"))));
    }

    @Test
    public void changeIntervalShows5m(){
        onView(withId(R.id.idInterval)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withId(R.id.idInterval)).check(matches(withSpinnerText(containsString("5m"))));
    }

    @Test
    public void changeIntervalShows3m(){
        onView(withId(R.id.idInterval)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.idInterval)).check(matches(withSpinnerText(containsString("3m"))));
    }

    @Test
    public void changeIntervalShows1m(){
        onView(withId(R.id.idInterval)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.idInterval)).check(matches(withSpinnerText(containsString("1m"))));
    }

    @Test
    public void chartNameAndSpinnerCorrectlyDisplayed(){
        onView(withId(R.id.idInterval)).check(matches(isDisplayed()));
        onView(withId(R.id.idCurrencyToShowName)).check(matches(isDisplayed()));
        onView(withId(R.id.candle_stick_chart)).check(matches(isDisplayed()));
    }

}
