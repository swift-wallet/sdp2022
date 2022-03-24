package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.DEFAULT;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isTouchable;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.sdp.cryptowalletapp.R;

import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.signIn.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @Before
    public void closeSystemDialogs() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void successfulLoginLaunchesMain() {
        onView(withId(R.id.loginEmail)).perform(typeText("jerome.ceccaldi@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("abcdef"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.main));
    }

    @Test
    public void incorrectUsernameDisplaysAlert() {
        onView(withId(R.id.loginEmail)).perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Incorrect username or password"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void incorrectPasswordDisplaysAlert() throws InterruptedException {
        onView(withId(R.id.loginEmail)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Incorrect username or password"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void incorrectCredentialsShowsRemainingAttempts() {
        onView(withId(R.id.loginEmail)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .perform(click());

        onView(withText("You have 2 attempt(s) remaining"))
                .inRoot(DEFAULT)
                .check(matches(isDisplayed()));
    }

    @Test
    public void tooManyFailedAttemptsDisplaysAlert() {
        onView(withId(R.id.loginEmail)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("b"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Too many unsuccessful attempts"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void tooManyFailedAttemptsSendsBackToMainActivity() {
        onView(withId(R.id.loginEmail)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.loginPassword)).perform(typeText("b"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        intended(allOf(
                toPackage("com.sdp.swiftwallet"),
                hasComponent(hasClassName(MainActivity.class.getName()))
        ));
    }

    @Test
    public void login_btn_correctly_displayed() {
        onView(withId(R.id.googleSignInBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void press_login_fires_correct_intent() {
        onView(withId(R.id.googleSignInBtn)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
    }

    @Test
    public void press_register_fires_intent() {
        onView(withId(R.id.register)).perform(click());

        intended(allOf(
                toPackage("com.sdp.swiftwallet"),
                hasComponent(hasClassName(RegisterActivity.class.getName()))
        ));
    }
}