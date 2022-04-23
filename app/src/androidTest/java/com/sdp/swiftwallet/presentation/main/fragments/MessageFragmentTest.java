package com.sdp.swiftwallet.presentation.main.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;

import com.kenai.jffi.Main;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.main.MainActivity;

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
public class MessageFragmentTest {
    public Context context;

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Before
    public void setup() {
        hiltRule.inject();
        context = ApplicationProvider.getApplicationContext();
    }

    public Intent setupReset(){
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
    public void checkLayoutCorrectlyDisplayed() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            onView(withId(R.id.mainNavMessageItem)).perform(click());
            onView(withId(R.id.contactsTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.addContactBtn)).check(matches(isDisplayed()));
            onView(withId(R.id.contactsRecyclerView)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addContactWorksCorrectly() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            onView(withId(R.id.mainNavMessageItem)).perform(click());
            onView(withId(R.id.addContactBtn)).perform(click());

            onView(withId(R.id.contactNameTv)).check(matches(isDisplayed()));
        }
    }

}