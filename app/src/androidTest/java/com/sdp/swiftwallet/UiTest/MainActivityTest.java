package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;

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
public class MainActivityTest {

    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setUp() throws Exception {
        hiltRule.inject();
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void bottom_bar_with_icons_is_displayed() {
        onView(withId(R.id.mainBottomNavView)).check(matches(isDisplayed()));
        onView(withId(R.id.mainNavHomeItem)).check(matches(isDisplayed()));
        onView(withId(R.id.mainNavStatsItem)).check(matches(isDisplayed()));
        onView(withId(R.id.mainNavPaymentItem)).check(matches(isDisplayed()));
        onView(withId(R.id.mainNavMessageItem)).check(matches(isDisplayed()));
        onView(withId(R.id.mainNavProfileItem)).check(matches(isDisplayed()));
    }

    @Test
    public void press_home_display_fragments() {
        closeSoftKeyboard();
        onView(withId(R.id.mainNavHomeItem)).perform(click());
        onView(withId(R.id.home_fragment)).check(matches(isDisplayed()));
        closeSoftKeyboard();
    }

    @Test
    public void press_payment_display_fragments() {
        closeSoftKeyboard();
        onView(withId(R.id.mainNavPaymentItem)).perform(click());
        onView(withId(R.id.payment_fragment)).check(matches(isDisplayed()));
        closeSoftKeyboard();
    }

    @Test
    public void press_message_display_fragments() {
        closeSoftKeyboard();
        onView(withId(R.id.mainNavMessageItem)).perform(click());
        onView(withId(R.id.message_fragment)).check(matches(isDisplayed()));
        closeSoftKeyboard();
    }

    @Test
    public void press_profile_display_fragments() {
        closeSoftKeyboard();
        onView(withId(R.id.mainNavProfileItem)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        closeSoftKeyboard();
    }
}