package com.sdp.swiftwallet.presentation.signIn;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> registerScenario = new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void no_username_raises_error() {
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("passwordTest"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void too_short_username_raise_error() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("passwordTest"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void too_long_username_raise_error() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("toolongusernametobeprocessed"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("passwordTest"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerUsernameEt)).check(matches(hasFocus()));
    }

    @Test
    public void no_email_raises_error() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("passwordTest"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerEmailEt)).check(matches(hasFocus()));
    }

    @Test
    public void no_password_raises_error() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerPasswordEt)).check(matches(hasFocus()));
    }

    @Test
    public void register_user_return_to_login() {
        onView(withId(R.id.registerUsernameEt)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailEt)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerPasswordEt)).perform(typeText("passwordTest"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
//        intended(toPackage("com.sdp.swiftwallet"));
    }
}