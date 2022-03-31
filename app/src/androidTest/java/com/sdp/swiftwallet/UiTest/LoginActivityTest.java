package com.sdp.swiftwallet.UiTest;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.signIn.ForgotPasswordActivity;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
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

    CountingIdlingResource mIdlingResource;
    FirebaseUser currUser;

    @Before
    public void initIntents() {
        Intents.init();
    }

    @Before
    public void registerIdlingResource() {
        testRule.getScenario().onActivity(activity ->
                mIdlingResource = activity.getIdlingResource()
        );
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void signOutCurrentUser() {
        currUser = FirebaseUtil.getAuth().getCurrentUser();
        if (currUser != null) {
            FirebaseUtil.getAuth().signOut();
        }
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

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void loginLayoutCorrectlyDisplayed() {
        onView(withId(R.id.loginTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.appLogo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailTv)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailEt)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPasswordTv)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPasswordEt)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.forgotPasswordTv)).check(matches(isDisplayed()));
        onView(withId(R.id.registerTv)).check(matches(isDisplayed()));
        onView(withId(R.id.googleSignInBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.useOfflineTv)).check(matches(isDisplayed()));
    }

    @Test
    public void pressForgotPWFiresIntentCorrectly() {
        onView(withId(R.id.forgotPasswordTv)).perform(click());

        intended(allOf(
                toPackage("com.sdp.swiftwallet"),
                hasComponent(hasClassName(ForgotPasswordActivity.class.getName()))
        ));
    }

    @Test
    public void pressRegisterFiresIntentCorrectly() {
        onView(withId(R.id.registerTv)).perform(click());

        intended(allOf(
                toPackage("com.sdp.swiftwallet"),
                hasComponent(hasClassName(RegisterActivity.class.getName()))
        ));
    }

    @Test
    public void emptyEmailRequestFocus() {
        onView(withId(R.id.loginPasswordEt))
                .perform(typeText("wrong"),
                        closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.loginEmailEt)).check(matches(hasFocus()));
    }

    @Test
    public void emptyPasswordRequestFocus() {
        onView(withId(R.id.loginEmailEt))
                .perform(typeText("email.test@gmail.com"),
                        closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.loginPasswordEt)).check(matches(hasFocus()));
    }

    @Test
    public void incorrectEmailDisplaysAlert() {
        onView(withId(R.id.loginEmailEt)).perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEt)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Incorrect username or password"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void incorrectPasswordDisplaysAlert() {
        onView(withId(R.id.loginEmailEt)).perform(typeText("email.test@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEt)).perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Incorrect username or password"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void incorrectCredentialsShowsRemainingAttempts() {
        onView(withId(R.id.loginEmailEt)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEt)).perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText("You have 2 attempt(s) remaining")).check(matches(isDisplayed()));
    }

    @Test
    public void tooManyFailedAttemptsDisplaysAlert() {
        onView(withId(R.id.loginEmailEt)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEt)).perform(typeText("b"), closeSoftKeyboard());
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
    public void successfulLoginLaunchesMain() {
        onView(withId(R.id.loginEmailEt)).perform(typeText("jerome.ceccaldi@epfl.ch"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEt)).perform(typeText("abcdef"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        intended(anyIntent());
    }

    @Test
    public void signInWithGoogleCredentialFailsCorrectly() {
        String testToken = "malformedToken";
        AuthCredential testCredential = GoogleAuthProvider.getCredential(testToken, null);
        testRule.getScenario().onActivity(activity -> {
           activity.signInWithCredential(testCredential);
        });

        currUser = FirebaseUtil.getAuth().getCurrentUser();
        assert(currUser == null);
    }

    @Test
    public void pressGoogleSignInStartAuth() {
        onView(withId(R.id.googleSignInBtn)).perform(click());

        Bundle googleBundle = new Bundle();
        googleBundle.putParcelable("googleSignInAccount", GoogleSignInAccount.createDefault());
        Intent googleIntent = new Intent();
        googleIntent.putExtra("account bundle", googleBundle);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(RESULT_OK, googleIntent);
        intending(toPackage("com.google.android.gms")).respondWith(result);
    }

    @Test
    public void pressOfflineTvFiresIntentCorrectly() {
        onView(withId(R.id.useOfflineTv)).perform(click());

        intended(allOf(
                toPackage("com.sdp.swiftwallet"),
                hasComponent(hasClassName(MainActivity.class.getName()))
        ));
    }

}