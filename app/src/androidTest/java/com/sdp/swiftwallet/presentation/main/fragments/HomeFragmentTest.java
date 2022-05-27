package com.sdp.swiftwallet.presentation.main.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;
import com.sdp.swiftwallet.presentation.wallet.WalletInfoActivity;
import com.sdp.swiftwallet.presentation.wallet.WalletSelectActivity;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class HomeFragmentTest {

    public final static String mockSeed = "test-testouille-aille-deux-trois";

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
        return new Intent(context, MainActivity.class);
    }

    public Intent setupValid(int mockCounter){
        setValidSeedAndCounter(context, mockCounter);
        return new Intent(context, MainActivity.class);
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
    public void shouldBeAbleToSeeConfigureButtonsWhenNoSeed(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            onView(withId(R.id.seed_setup)).check(matches(isDisplayed()));
            onView(withId(R.id.seed_not_setup)).check(matches(isDisplayed()));
        }
    }
    @Test
    public void shouldBeAbleToLaunchSeedActivityWhenNoSeed(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            clickOn(R.id.seed_setup);
            intended(hasComponent(CreateSeedActivity.class.getName()));
        }
    }
    @Test
    public void shouldBeAbleToSeeCreateFirstAddressWhenSeedNoAddresses(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupValid(0))) {
            onView(withId(R.id.first_wallet_setup_layout)).check(matches(isDisplayed()));
            clickOn(R.id.first_wallet_setup_button);
            intended(hasComponent(WalletSelectActivity.class.getName()));
        }
    }
    @Test
    public void shouldBeAbleToSelectWalletWhenSeed(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupValid(1))) {
            onView(withId(R.id.item_address)).check(matches(isDisplayed()));
            clickOn(R.id.item_address);
            intended(hasComponent(WalletSelectActivity.class.getName()));
        }
    }
    @Test
    public void shouldBeAbleToLaunchInfoWhenSeed(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupValid(5))) {
            onView(withId(R.id.wallet_container)).check(matches(isDisplayed()));
            clickOn(R.id.show_qr_button);
            intended(hasComponent(WalletInfoActivity.class.getName()));
        }
    }
}