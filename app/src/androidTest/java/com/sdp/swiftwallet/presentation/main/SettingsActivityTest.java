package com.sdp.swiftwallet.presentation.main;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.PreferenceMatchers.withKey;
import static androidx.test.espresso.matcher.PreferenceMatchers.withTitle;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagKey;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.preference.Preference;
import android.provider.Settings;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.main.SettingsActivity;

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
public class SettingsActivityTest {

    public ActivityScenarioRule<SettingsActivity> testRule = new ActivityScenarioRule<>(SettingsActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setUp() throws Exception {
        hiltRule.inject();
        Intents.init();
        closeSoftKeyboard();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        closeSoftKeyboard();
    }

    @Test
    public void settingsFragmentIsDisplayed() {
        onView(withId(R.id.settings_framelayout)).check(matches(isDisplayed()));
    }

    @Test
    public void historySwitch() {
        onData(allOf(is(instanceOf(Preference.class)), withKey("history_switch")))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Preference.class)), withKey("history_switch")))
                .perform(click());
    }

    @Test
    public void historyList() {
        onData(allOf(is(instanceOf(Preference.class)), withKey("history_list")))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Preference.class)), withKey("history_list")))
                .perform(click());
    }

    @Test
    public void walletSwitch() {
        onData(allOf(is(instanceOf(Preference.class)), withKey("wallet_switch")))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Preference.class)), withKey("wallet_switch")))
                .perform(click());
    }

    @Test
    public void feedback() {
        onData(allOf(is(instanceOf(Preference.class)), withKey("feedback")))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Preference.class)), withKey("feedback")))
                .perform(click());
    }
}