package com.sdp.swiftwallet.presentation.cryptoGraph;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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
import com.sdp.swiftwallet.presentation.cryptoGraph.CryptoValuesActivity;

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
public class CryptoValuesActivityTest {

    CountingIdlingResource mIdlingResource;

    public ActivityScenarioRule<CryptoValuesActivity> testRule = new ActivityScenarioRule<CryptoValuesActivity>(CryptoValuesActivity.class);
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
    public void dataDisplayedCorrectly() {
        onView(withId(R.id.idSpinnerShowAll)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.idCryptoSearch)).perform(typeText("ETH"), closeSoftKeyboard());

        //onView(withText("ETHUSDT")).check(matches(isDisplayed()));
        onView(withText("Bitcoin")).check(doesNotExist());
    }

    @Test
    public void askToSeeAllChangesCorrectly(){
        onView(withId(R.id.idSpinnerShowAll)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.idSpinnerShowAll)).check(matches(withSpinnerText(containsString("Show All"))));
    }

}