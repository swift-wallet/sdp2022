package com.sdp.swiftwallet.presentation.signIn;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    // Rules Set Up
    public ActivityScenarioRule<RegisterActivity> testRule = new ActivityScenarioRule<>(RegisterActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject FirebaseAuth mAuth;
    DummyAuthenticator authenticator;

    @Rule
    public final RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

    // Counter for idling resources in RegisterActivity
    CountingIdlingResource mIdlingResource;

    @Before
    public void setUp() {
        // Not sure but this may be required as the first line in setUp()
        hiltRule.inject();
        // Init the fake authenticator by using a static instance from DummyAuthenticator
        authenticator = DummyAuthenticator.INSTANCE;
        // Init Espresso intents
        Intents.init();
        // Get the idling resource from RegisterActivity and register it
        testRule.getScenario().onActivity(activity ->
                mIdlingResource = activity.getIdlingResource()
        );
        IdlingRegistry.getInstance().register(mIdlingResource);
        // Make sure no user is signed in before testing
        mAuth.signOut();
        // Make sure no dialogs are displayed before testing
        closeSystemDialogs();
        // Reset fake authenticator flags
        authenticator.setExecFailure(false);
        authenticator.setExecSuccess(false);
        authenticator.setCurrUser(null);
    }

    /**
     * Close all dialogs from the ongoing view
     */
    public void closeSystemDialogs() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @After
    public void tearDown() {
        // Release all Espresso intents
        Intents.release();
        // Unregister the idling resource
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void registerLayoutIsCorrectlyDisplayed() {
        onView(withId(R.id.registerTitle)).check(matches(isDisplayed()));

        onView(withId(R.id.registerImageProfileLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.registerInputUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.registerInputEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.registerInputPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.registerInputConfirmPassword)).check(matches(isDisplayed()));

        onView(withId(R.id.registerBtnLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void registerWithEmptyUsernameGetFocus() {
        onView(withId(R.id.registerInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputUsername)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithTooShortUsernameGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputUsername)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithTooLongUsernameGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("toolongusernametobeprocessed"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputUsername)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithWrongUsernameGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("*****"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Aliska74!"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Aliska74!"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("michel@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputUsername)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithEmptyEmailGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputEmail)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithWrongEmailGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("michel"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputEmail)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithEmptyPasswordGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password1"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputPassword)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithWrongPasswordGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("url"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("url"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("michel@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputPassword)).check(matches(hasFocus()));
    }

    @Test
    public void registerWithDifferentConfirmPasswordGetFocus() {
        onView(withId(R.id.registerInputUsername)).perform(typeText("usernameTest"), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText("email.test@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText("Password1"), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText("Password"), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerInputPassword)).check(matches(hasFocus()));
    }

    @Test
    public void registerUserFiresLoginIntentCorrectly() {
        String userTestUsername = "registerTest";
        String userTestEmail = "register.test@epfl.ch";
        String userTestPassword = "Register1";

        authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
        authenticator.setExecSuccess(true);
        User registerUser = new User("regsterUid123", userTestEmail);
        authenticator.setCurrUser(registerUser);

        // Keep it because inputs checks are done outside of authenticator for now
        onView(withId(R.id.registerInputUsername)).perform(typeText(userTestUsername), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText(userTestEmail), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText(userTestPassword), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText(userTestPassword), closeSoftKeyboard());

        onView(withId(R.id.registerBtn)).perform(click());
        intended(allOf(
                toPackage("com.sdp.swiftwallet"),
                hasComponent(hasClassName(LoginActivity.class.getName()))
        ));
    }

    @Test
    public void registerUserFailsCorrectly() {
        String userTestUsername = "registerTest";
        String userTestEmail = "register.test@epfl.ch";
        String userTestPassword = "Register1";

        // Keep it because inputs checks are done outside of authenticator for now
        onView(withId(R.id.registerInputUsername)).perform(typeText(userTestUsername), closeSoftKeyboard());
        onView(withId(R.id.registerInputEmail)).perform(typeText(userTestEmail), closeSoftKeyboard());
        onView(withId(R.id.registerInputPassword)).perform(typeText(userTestPassword), closeSoftKeyboard());
        onView(withId(R.id.registerInputConfirmPassword)).perform(typeText(userTestPassword), closeSoftKeyboard());

        authenticator.setResult(SwiftAuthenticator.Result.ERROR);
        authenticator.setExecFailure(true);

        onView(withId(R.id.registerBtn)).perform(click());
        onView(withId(R.id.registerBtn)).check(matches(isDisplayed()));
    }
}