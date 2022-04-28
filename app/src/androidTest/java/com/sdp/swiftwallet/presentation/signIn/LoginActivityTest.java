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
import com.sdp.swiftwallet.di.AuthenticatorModule;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import dagger.hilt.components.SingletonComponent;
import java.util.Optional;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

@UninstallModules(AuthenticatorModule.class)
@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Module
    @InstallIn(SingletonComponent.class)
    public static class TestModule {

        @Provides
        public static SwiftAuthenticator provideAuthenticator() {
            return authenticator;
        }

    }

    // Rules Set Up
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    FirebaseAuth mAuth;
    private static DummyAuthenticator authenticator = new DummyAuthenticator();

    @Rule
    public final RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void setup() {
        hiltRule.inject();

        Intents.init();

        mAuth.signOut();

        closeSystemDialogs();

        authenticator.setExecFailure(false);
        authenticator.setExecSuccess(false);
    }

    @After
    public void teardown() {
        Intents.release();
    }

    public void closeSystemDialogs() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }


    // Check visibility of all elements on the view
    @Test
    public void viewElementsAreDisplayed() {
        onView(withId(R.id.loginTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.appLogo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailTv)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailEt)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPasswordTv)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPasswordEt)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.forgotPasswordTv)).check(matches(isDisplayed()));
        onView(withId(R.id.registerTv)).check(matches(isDisplayed()));
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

    public static class DummyAuthenticator implements SwiftAuthenticator {

        SwiftAuthenticator.Result result;

        boolean execSuccess;
        boolean execFailure;

        @Override
        public Result signIn(String email, String password, Runnable success, Runnable failure) {
            if (execSuccess) {
                success.run();
            }

            if (execFailure) {
                failure.run();
            }

            return result;
        }

        @Override
        public Optional<User> getUser() {
            return Optional.empty();
        }

        public void setResult(SwiftAuthenticator.Result result) {
            this.result = result;
        }

        public void setExecSuccess(boolean execSuccess) {
            this.execSuccess = execSuccess;
        }

        public void setExecFailure(boolean execFailure) {
            this.execFailure = execFailure;
        }

    }
}