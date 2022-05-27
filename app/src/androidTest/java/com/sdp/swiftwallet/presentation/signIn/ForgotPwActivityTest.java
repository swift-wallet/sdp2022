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
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class ForgotPwActivityTest {

  // Rules Set Up
  public ActivityScenarioRule<ForgotPasswordActivity> testRule = new ActivityScenarioRule<>(ForgotPasswordActivity.class);
  public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

  @Rule
  public final RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

  @Inject
  FirebaseAuth mAuth;
  DummyAuthenticator authenticator;

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
    authenticator.setCurrUser(null);
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
  public void layoutCorrectlyDisplayed(){
    onView(withId((R.id.appLogo))).check(matches(isDisplayed()));
    onView(withId(R.id.emailField)).check(matches(isDisplayed()));
    onView(withId(R.id.sendReset)).check(matches(isDisplayed()));
    onView(withId((R.id.goBackForgotPw))).check(matches(isDisplayed()));
  }

  @Test
  public void emptyEmailRaisesError() {
    onView(withId(R.id.sendReset)).perform(click());

    onView(withId(R.id.emailField)).check(matches(hasFocus()));
  }

  @Test
  public void wrongEmailRaisesError() {
    onView(withId(R.id.emailField)).perform(typeText("wrong"), closeSoftKeyboard());
    onView(withId(R.id.sendReset)).perform(click());

    onView(withId(R.id.emailField)).check(matches(hasFocus()));
  }

  @Test
  public void sendEmailSuccessCorrectly(){
    authenticator.setExecSuccess(true);
    authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
    onView(withId(R.id.emailField)).perform(typeText("dummy@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.sendReset)).perform(click());

    intended(allOf(
        toPackage("com.sdp.swiftwallet"),
        hasComponent(hasClassName(LoginActivity.class.getName()))
    ));
  }

  @Test
  public void sendEmailFailsCorrectly() {
    authenticator.setExecFailure(true);
    authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
    onView(withId(R.id.emailField)).perform(typeText("dummy@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.sendReset)).perform(click());

    onView(withId(R.id.emailField)).check(matches(hasFocus()));
  }

  @Test
  public void resetEmailFailsCorrectlyOnAuthSide() {
    authenticator.setResult(SwiftAuthenticator.Result.ERROR);
    onView(withId(R.id.emailField)).perform(typeText("dummy@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.sendReset)).perform(click());

    onView(withId(R.id.emailField)).check(matches(hasFocus()));
  }

  @Test
  public void backButtonFiresIntent(){
    onView(withId(R.id.goBackForgotPw)).perform(click());

    intended(allOf(
            toPackage("com.sdp.swiftwallet"),
            hasComponent(hasClassName(LoginActivity.class.getName()))
    ));
  }
}
