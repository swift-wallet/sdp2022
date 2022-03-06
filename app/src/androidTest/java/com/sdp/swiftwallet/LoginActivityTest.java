package com.sdp.swiftwallet;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import androidx.test.espresso.intent.matcher.IntentMatchers.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.junit.Assert.*;

import com.sdp.cryptowalletapp.R;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void successfulLoginLaunchesGreeting() {
        Intents.init();

        onView(withId(R.id.loginUsername)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        intended(hasComponent(GreetingActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void successfulLoginLaunchesGreetingWithCorrectMessage() {
        Intents.init();

        onView(withId(R.id.loginUsername)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        intended(hasExtra(GreetingActivity.EXTRA_MESSAGE, "Welcome to SwiftWallet!"));

        Intents.release();
    }

    @Test
    public void incorrectCredentialsDisplaysAlert() {
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Incorrect username or password")).check(matches(isDisplayed()));
    }

    @Test
    public void incorrectCredentialsShowsRemainingAttempts() {
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withText("You have 2 attempt(s) remaining")).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void tooManyFailedAttemptsDisplaysAlert() {
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Too many unsuccessful attempts")).check(matches(isDisplayed()));
    }

    @Test
    public void tooManyFailedAttemptsSendsBackToMainActivity() {
        Intents.init();

        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK")).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        intended(hasComponent(MainActivity.class.getName()));

        Intents.release();
    }
}