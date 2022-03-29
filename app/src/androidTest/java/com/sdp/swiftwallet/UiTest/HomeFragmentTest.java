package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;

import androidx.core.view.ViewCompat;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.kenai.jffi.Main;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jnr.ffi.annotations.In;

@RunWith(JUnit4.class)
public class HomeFragmentTest {
    public final static String mockSeed = "test-testouille-aille-deux-trois";
    public final static String mockSpaceSeed = "test testouille aille deux trois";
    public final static String[] mockArraySeed = new String[]{"test", "testouille", "aille", "deux", "trois"};

    public Context context;
    public final static int mockCounter = 10;
    public ActivityScenario<MainActivity> testScenario;

    public void setValidSeedAndCounter(Context context){
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
        context = ApplicationProvider.getApplicationContext();
        resetPrefs(context);
    }

    public Intent setupReset(){
        return new Intent(context, MainActivity.class);
    }

    public Intent setupValid(){
        setValidSeedAndCounter(context);
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
            scenario.recreate();
            scenario.moveToState(Lifecycle.State.STARTED);
            onView(withId(R.id.seed_setup)).check(matches(isDisplayed()));
            onView(withId(R.id.seed_not_setup)).check(matches(isDisplayed()));
        }
    }
    @Test
    public void shouldBeAbleToLaunchSeedActivityWhenNoSeed(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            scenario.recreate();
            scenario.moveToState(Lifecycle.State.STARTED);
            clickOn(R.id.seed_setup);
            intended(hasComponent(CreateSeedActivity.class.getName()));
        }
    }
    @Test
    public void shouldBeAbleToCreateAddressesWhenSeed(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupValid())) {
            scenario.recreate();
            scenario.moveToState(Lifecycle.State.STARTED);
            onView(withId(R.id.create_address_button)).check(matches(isDisplayed()));
            clickOn(R.id.create_address_button);
            onView(withId(R.id.home_nested_frag_container)).check(matches(hasMinimumChildCount(1)));
        }
    }
}
