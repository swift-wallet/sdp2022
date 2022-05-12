package com.sdp.swiftwallet.presentation.wallet;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class WalletSelectActivityTest {

    public final static String mockSeed = "test-testouille-aille-deux-trois";
    public final static String mockPK = "0000000000000000000000000000000000000000000000000000000000000002";

    public Context context;

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    public void setValidSeedAndCounter(Context context, int mockCounter){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(SeedGenerator.PREF_SEED_ID, mockSeed)
                .putInt(SeedGenerator.PREF_COUNTER_ID, mockCounter)
                .apply();
    }
    public void resetPrefs(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
    @Before
    public void setup() {
        hiltRule.inject();
        context = ApplicationProvider.getApplicationContext();
        resetPrefs(context);
    }

    public Intent setupReset(){
        return new Intent(context, WalletSelectActivity.class);
    }

    public Intent setupValid(int mockCounter){
        setValidSeedAndCounter(context, mockCounter);
        return new Intent(context, WalletSelectActivity.class);
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
    public void shouldBeAbleToCreateAddresses(){
        try (ActivityScenario<WalletSelectActivity> scenario = ActivityScenario.launch(setupValid(1))) {
            onView(withId(R.id.create_address_button)).check(matches(isDisplayed()));
            clickOn(R.id.create_address_button);
        }
    }

    @Test
    public void shouldBeAbleToImportPK(){
        try (ActivityScenario<WalletSelectActivity> scenario = ActivityScenario.launch(setupValid(1))) {
            onView(withId(R.id.show_import_address_button)).check(matches(isDisplayed()));
            clickOn(R.id.show_import_address_button);
            typeTo(R.id.import_wallet_input, mockPK);
            closeSoftKeyboard();
            clickOn(R.id.import_address_button);
            intended(hasAction("alo"));
        }
    }
}