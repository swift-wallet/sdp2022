package com.sdp.swiftwallet.uiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoGraphActivity;
import com.sdp.swiftwallet.domain.model.Currency;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CryptoGraphActivityTest {
    CountingIdlingResource mIdlingResource;
    Currency ethereum;
    @Rule
    public ActivityScenarioRule<CryptoGraphActivity> testRule = new ActivityScenarioRule<>(CryptoGraphActivity.class);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @Before
    public void initEthereum(){ethereum = new Currency("ETH", "Ethereum", 2000);}

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
    public void nameAndSpinnerCorrectlyDisplayed(){
        onView(withId(R.id.idInterval)).check(matches(isDisplayed()));
        onView(withId(R.id.idCurrencyToShowName)).check(matches(isDisplayed()));
    }
    @Test
    public void nameDisplayedCorrectly() {
        onView(withText("Ethereum")).check(matches(isDisplayed()));
        onView(withText("Bitcoin")).check(doesNotExist());
    }
}
