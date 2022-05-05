package com.sdp.swiftwallet.presentation.message;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.signIn.DummyAuthenticator;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class AddContactActivityTest {

    // Rules Set Up
    public ActivityScenarioRule<AddContactActivity> testRule = new ActivityScenarioRule<>(AddContactActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public final RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

    // Counter for idling resources in AddContactActivity
    CountingIdlingResource mIdlingResource;

    @Before
    public void setUp() {
        // Not sure but this may be required as the first line in setUp()
        hiltRule.inject();
        // Init Espresso intents
        Intents.init();
        // Get the idling resource from AddContactActivity and register it
        testRule.getScenario().onActivity(activity ->
                mIdlingResource = activity.getIdlingResource()
        );
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void teardown() {
        // Release all Espresso intents
        Intents.release();
        // Unregister the idling resource
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void addContactLayoutIsCorrectlyDisplayed() {
        onView(withId(R.id.addContactTitle)).check(matches(isDisplayed()));

        onView(withId(R.id.contacts_qr_view)).check(matches(isDisplayed()));
        onView(withId(R.id.contacts_via_qr_button)).check(matches(isDisplayed()));
        onView(withId(R.id.addContactOr)).check(matches(isDisplayed()));
        onView(withId(R.id.addContactInputEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.previewBtnLayout)).check(matches(isDisplayed()));
    }
}