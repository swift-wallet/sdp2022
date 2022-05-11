package com.sdp.swiftwallet.presentation.main.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.domain.model.object.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.message.AddContactActivity;

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
public class MessageFragmentTest {
    public Context context;

    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

    // Counter for idling resources in MessageFragment
    CountingIdlingResource mIdlingResource;

    @Before
    public void setUp() {
        hiltRule.inject();
        context = ApplicationProvider.getApplicationContext();
        Intents.init();
    }

    public Intent setupReset(){
        return new Intent(context, MainActivity.class);
    }

    @After
    public void tearDown() {
        Intents.release();
        // Unregister the idling resource
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void checkLayoutCorrectlyDisplayed() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            onView(withId(R.id.mainNavMessageItem)).perform(click());
            onView(withId(R.id.contactsTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.addContactBtn)).check(matches(isDisplayed()));
            onView(withId(R.id.textErrorMessage)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addContactBtnLaunchesIntent() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            onView(withId(R.id.mainNavMessageItem)).perform(click());
            onView(withId(R.id.addContactBtn)).perform(click());

            intended(hasComponent(AddContactActivity.class.getName()));
        }
    }

    @Test
    public void displayContactsWithOnlineUserWorksCorrectly() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(setupReset())) {
            User registerUser = new User("HtaZP4b2oJQ9CDrjpmf6tLjkAK33",
                    "newRegister.test@gmail.com",
                    SwiftAuthenticator.LoginMethod.BASIC);

            scenario.onActivity(activity -> {
                mIdlingResource = activity.getIdlingResource();
                ((BaseApp) activity.getApplication()).setCurrUser(registerUser);
            });
            IdlingRegistry.getInstance().register(mIdlingResource);

            onView(withId(R.id.mainNavMessageItem)).perform(click());
            onView(withId(R.id.contactsRecyclerView)).check(matches(isDisplayed()));
        }
    }

}