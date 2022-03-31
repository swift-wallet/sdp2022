package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;

import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.signIn.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void bottom_bar_with_icons_is_displayed() {
        onView(withId(R.id.bottom_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_home)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_stats)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_payment)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_message)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_profile)).check(matches(isDisplayed()));
    }

    @Test
    public void press_home_display_fragments() {
        onView(withId(R.id.bar_home)).perform(click());
        onView(withId(R.id.home_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void press_stats_display_fragments() {
        onView(withId(R.id.bar_stats)).perform(click());
        onView(withId(R.id.stats_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void press_payment_display_fragments() {
        onView(withId(R.id.bar_payment)).perform(click());
        onView(withId(R.id.payment_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void press_message_display_fragments() {
        onView(withId(R.id.bar_message)).perform(click());
        onView(withId(R.id.friend_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void press_profile_display_fragments() {
        onView(withId(R.id.bar_profile)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
    }
}