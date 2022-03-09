package com.sdp.swiftwallet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.app.Activity;

import androidx.annotation.ContentView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignInTest {

    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void logo_correctly_displayed() {
        Intents.init();
        onView(withId(R.id.iconGoogle)).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void tv_correctly_displayed() {
        Intents.init();
        onView(withId(R.id.caption)).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void login_btn_correctly_displayed() {
        Intents.init();
        onView(withId(R.id.googleSignInBtn)).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void press_login_fires_correct_intent() {
        Intents.init();
        onView(withId(R.id.googleSignInBtn)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        Intents.release();
    }

    @Test
    public void login_then_logout_fires_correct_intent() {
        Intents.init();
        onView(withId(R.id.googleSignInBtn)).perform(click());
        onView(withId(R.id.logoutBtn)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        onView(withId(R.id.main)).check(matches(isDisplayed()));
        Intents.release();
    }

}