package com.sdp.swiftwallet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {
//    @Rule
//    public ActivityScenarioRule<ProfileActivity> testRule = new ActivityScenarioRule<>(ProfileActivity.class);
//
//    @Before
//    public void init() {
//        Intents.init();
//    }
//
//    @After
//    public void release() {
//        Intents.release();
//    }
//
//    @Test
//    public void email_correctly_displayed() {
//        onView(withId(R.id.email)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void logout_btn_correctly_displayed() {
//        onView(withId(R.id.logoutBtn)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void press_logout_fires_correct_intent() {
//        onView(withId(R.id.logoutBtn)).perform(click());
//
//        intended(toPackage("com.sdp.swiftwallet"));
//    }

}