package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;

import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;


@RunWith(JUnit4.class)
@HiltAndroidTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Inject FirebaseAuth firebaseAuth;
    @Before
    public void setUp() {
        Intents.init();
    }
    @Before
    public void init() {
        hiltRule.inject();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void test(){
        try (ActivityScenario<TransactionActivity> scenario = ActivityScenario.launch(new Intent(ApplicationProvider.getApplicationContext(), MainActivityTest.class))) {
            onView(withId(R.id.bar_home)).check(matches(isDisplayed()));
        }
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
        onView(withId(R.id.message_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void press_profile_display_fragments() {
        onView(withId(R.id.bar_profile)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
    }
}