package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.kenai.jffi.Main;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.MainActivity;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;
import com.sdp.swiftwallet.presentation.wallet.CreateSeedActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HomeFragmentTest {
    public final static String mockSeed = "test-testouille";
    public final static String mockSpaceSeed = "test testouille";
    public final static String[] mockArraySeed = new String[]{"test", "testouille"};

    public final static int mockCounter = 10;

    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);

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

    public String getSeed(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SeedGenerator.WALLETS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SeedGenerator.PREF_SEED_ID, null);
    }

    @Before
    public void init(){
        testRule.getScenario().onActivity(activity -> {
            resetPrefs(activity);
        });
    }

    @Test
    public void setupSeedShouldAppearWhenNoSeed(){
        onView(withId(R.id.seed_setup)).check(matches(isDisplayed()));
    }
    @Test
    public void setupSeedShouldNotBeDisplayedWhenSeed(){
        testRule.getScenario().onActivity(activity -> {
            setValidSeedAndCounter(activity);
            onView(withId(R.id.seed_setup)).check(matches(not(isDisplayed())));
        });
    }
    @Test
    public void shouldBeAbleToCreateAddressesWhenSeed(){
        testRule.getScenario().onActivity(activity -> {
            setValidSeedAndCounter(activity);
            onView(withId(R.id.create_address_button)).check(matches(isDisplayed()));
            try{
                clickOn(R.id.create_address_button);
            }
            catch(Exception e){
                fail();
            }
        });
    }
}
