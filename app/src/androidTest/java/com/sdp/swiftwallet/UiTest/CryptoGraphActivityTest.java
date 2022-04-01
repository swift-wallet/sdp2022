package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoGraphActivity;
import com.sdp.swiftwallet.CryptoValuesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CryptoGraphActivityTest {

    CountingIdlingResource mIdlingResource;
    int size;

    @Rule
    public ActivityScenarioRule<CryptoGraphActivity> testRule = new ActivityScenarioRule<CryptoGraphActivity>(CryptoGraphActivity.class);

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
    public void nameDisplayedCorrectly() {
        onView(withId(R.id.idCryptoSearch)).perform(typeText("ETH"), closeSoftKeyboard());
        onView(withId(R.id.idRelativeLayoutCurrency)).perform(click());

        onView(withText("Ethereum")).check(matches(isDisplayed()));
        onView(withText("Bitcoin")).check(doesNotExist());
    }

    @Test
    public void dataContainsMoreThanAHundredSamples() throws InterruptedException {
        onView(withId(R.id.idCryptoSearch)).perform(typeText("ETH"), closeSoftKeyboard());
        onView(withId(R.id.idRelativeLayoutCurrency)).perform(click());
        wait(5);
        testRule.getScenario().onActivity(activity ->
                size = activity.getOpenTimes().size()
        );
        assertTrue(size>100);
    }
}
