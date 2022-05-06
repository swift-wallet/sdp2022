package com.sdp.swiftwallet.presentation.message;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;
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
        testRule.getScenario().onActivity(activity -> {
            ((BaseApp) activity.getApplication()).setCurrUser(null);
            mIdlingResource = activity.getIdlingResource();
        });
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

    @Test
    public void qrCodeBtnLaunchesIntent() {
        onView(withId(R.id.contacts_via_qr_button)).perform(click());

        intended(hasAction("com.google.zxing.client.android.SCAN"));
    }

    @Test
    public void previewBtnOfflineWithEmptyEmailFailsCorrectly() {
        onView(withId(R.id.previewBtn)).perform(click());

        //check for toast or for error message if one is added
    }

    @Test
    public void previewBtnOfflineWithEmailFailsCorrectly() {
        onView(withId(R.id.addContactInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.previewBtn)).perform(click());
        
        // check for toast or for error message if one is added
    }

    @Test
    public void previewBtnOnlineWithEmailLaunchesIntent() {
        testRule.getScenario().onActivity(activity -> {
            User addContactUser = new User("HtaZP4b2oJQ9CDrjpmf6tLjkAK33",
                    "newRegister.test@gmail.com",
                    SwiftAuthenticator.LoginMethod.BASIC);
            ((BaseApp) activity.getApplication()).setCurrUser(addContactUser);
        });

        onView(withId(R.id.addContactInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.previewBtn)).perform(click());

        onView(withId(R.id.confirmBtnLayout))
                .check(matches(isDisplayed()))
                .perform(click());

        intended(hasComponent(MainActivity.class.getName()));
    }
}