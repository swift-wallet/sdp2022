package com.sdp.swiftwallet.presentation.signIn;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

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
public class LoginActivityTest {

    // Rules Set Up
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    FirebaseAuth mAuth;
    DummyAuthenticator authenticator;

    @Rule
    public final RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setup() {
        // Not sure but this may be required as the first line in setUp()
        hiltRule.inject();
        // Init the fake authenticator by using a static instance from DummyAuthenticator
        authenticator = DummyAuthenticator.INSTANCE;
        // Init Espresso intents
        Intents.init();
        // Make sure no user is signed in before testing
        mAuth.signOut();
        // Make sure no dialogs are displayed before testing
        closeSystemDialogs();
        // Reset fake authenticator flags
        authenticator.setExecFailure(false);
        authenticator.setExecSuccess(false);
    }

    @After
    public void teardown() {
        Intents.release();
    }

    /**
     * Close all dialogs from the ongoing view
     */
    public void closeSystemDialogs() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Test
    public void loginTitleIsDisplayed() {
        onView(withId(R.id.loginTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void appLogoIsDisplayed() {
        onView(withId(R.id.appLogo)).check(matches(isDisplayed()));
    }

    @Test
    public void emailTVIsDisplayed() {
        onView(withId(R.id.loginEmailTv)).check(matches(isDisplayed()));
    }

    @Test
    public void emailETIsDisplayed() {
        onView(withId(R.id.loginEmailEt)).check(matches(isDisplayed()));
    }

    @Test
    public void passwordTVIsDisplayed() {
        onView(withId(R.id.loginPasswordTv)).check(matches(isDisplayed()));
    }

    @Test
    public void passwordETIsDisplayed() {
        onView(withId(R.id.loginPasswordEt)).check(matches(isDisplayed()));
    }

    @Test
    public void loginButtonIsDisplayed() {
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void forgotPasswordTVIsDisplayed() {
        onView(withId(R.id.forgotPasswordTv)).check(matches(isDisplayed()));
    }

    @Test
    public void registerTVIsDisplayed() {
        onView(withId(R.id.registerTv)).check(matches(isDisplayed()));
    }

    @Test
    public void offlineTVIsDisplayed() {
        onView(withId(R.id.useOfflineTv)).check(matches(isDisplayed()));
    }


    @Test
    public void forgotPWButtonFiresIntentCorrectly() {
        onView(withId(R.id.forgotPasswordTv)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        intended(hasComponent(ForgotPasswordActivity.class.getName()));
    }

    @Test
    public void registerButtonFiresIntentCorrectly() {
        onView(withId(R.id.registerTv)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        intended(hasComponent(RegisterActivity.class.getName()));
    }

    @Test
    public void offlineButtonFiresIntentCorrectly() {
        onView(withId(R.id.useOfflineTv)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void emptyPasswordDisplaysToastMessage() {
        authenticator.setResult(SwiftAuthenticator.Result.EMPTY_PASSWORD);

        onView(withId(R.id.loginButton)).perform(click());

        //How do you check that toast messages are displayed???
    }

    @Test
    public void emptyEmailDisplaysToastMessage() {
        authenticator.setResult(SwiftAuthenticator.Result.EMPTY_EMAIL);

        onView(withId(R.id.loginButton)).perform(click());

        //How do you check that toast messages are displayed???
    }

    @Test
    public void authErrorDisplaysErrorMessage() {
        authenticator.setResult(SwiftAuthenticator.Result.ERROR);

        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Authentication error"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void successCallbackDisplaysToastMessage() {
        authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
        authenticator.setExecSuccess(true);

        onView(withId(R.id.loginButton)).perform(click());

        //How do you check that toast messages are displayed???
    }

    @Test
    public void successCallbackFiresIntent() {
        authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
        authenticator.setExecSuccess(true);

        onView(withId(R.id.loginButton)).perform(click());

        intended(toPackage("com.sdp.swiftwallet"));
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void failureCallbackDisplaysErrorMessage() {
        authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
        authenticator.setExecFailure(true);

        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Incorrect username or password"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void failureCallbackDisplaysRemainingAttempts() {
        authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
        authenticator.setExecFailure(true);

        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText("You have 2 attempt(s) remaining")).check(matches(isDisplayed()));
    }

    @Test
    public void manyFailuresDisplaysErrorMessage() {
        authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
        authenticator.setExecFailure(true);

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
}