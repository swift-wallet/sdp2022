package com.sdp.swiftwallet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sdp.cryptowalletapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CryptoValuesActivityTest {

    @Rule
    public ActivityScenarioRule<CryptoValuesActivity> testRule = new ActivityScenarioRule<CryptoValuesActivity>(CryptoValuesActivity.class);

    @Before
    public void initIntents() {
        Intents.init();
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

    @Test
    public void dataDisplayedCorrectly() {
        onView(withId(R.id.idCryptoSearch)).perform(typeText("ETH"), closeSoftKeyboard());

        onView(withText("Ethereum")).check(matches(isDisplayed()));
        onView(withText("Bitcoin")).check(doesNotExist());
    }

}
