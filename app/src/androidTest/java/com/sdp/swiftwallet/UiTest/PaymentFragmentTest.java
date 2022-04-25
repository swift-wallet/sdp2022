package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class PaymentFragmentTest {
    public final static String mockSeed = "test-testouille-aille-deux-trois";
    public final static int mockCounter = 10;

    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    public void setValidSeedAndCounter(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(SeedGenerator.PREF_SEED_ID, mockSeed)
                .putInt(SeedGenerator.PREF_COUNTER_ID, mockCounter)
                .apply();
    }

    @Before
    public void setup() {
        hiltRule.inject();
        setValidSeedAndCounter(ApplicationProvider.getApplicationContext());
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
    public void shouldBeAbleToSeeTheFragment(){
        clickOn(R.id.mainNavPaymentItem);
        onView(withId(R.id.send_from_spinner)).check(matches(isDisplayed()));
    }
    @Test
    public void shouldStartTheQR(){
        clickOn(R.id.mainNavPaymentItem);
        clickOn(R.id.send_qr_scan);
        intended(hasAction("com.google.zxing.client.android.SCAN"));
    }

    @Test
    public void shouldBeAbleToModifyTheAmount(){
        clickOn(R.id.mainNavPaymentItem);
        typeTo(R.id.send_amount, "10");
        closeSoftKeyboard();
    }
}
