package com.sdp.swiftwallet.presentation.main.fragments;

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
import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;
import static com.adevinta.android.barista.interaction.BaristaEditTextInteractions.typeTo;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.signIn.DummyAuthenticator;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Inject;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class ProfileFragmentTest {

  public Context context;

  public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
  public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

  @Rule
  public RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

  @Inject
  FirebaseAuth mAuth;
  DummyAuthenticator authenticator;

  @Before
  public void setup() {
    // Not sure but this may be required as the first line in setUp()
    hiltRule.inject();
    // Set context for fragment
    context = ApplicationProvider.getApplicationContext();
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
  public void layoutCorrectlyDisplayed() {
    clickOn(R.id.mainNavProfileItem);
    onView(withId(R.id.profile_title)).check(matches(isDisplayed()));
    onView(withId(R.id.profile_welcomeTv)).check(matches(isDisplayed()));
    onView(withId(R.id.update_emailEt)).check(matches(isDisplayed()));
    onView(withId(R.id.update_email_btn)).check(matches(isDisplayed()));
    onView(withId(R.id.logout_btn)).check(matches(isDisplayed()));
  }

  @Test
  public void signOutFiresLoginIntent() {
    clickOn(R.id.mainNavProfileItem);
    clickOn(R.id.logout_btn);
    intended(allOf(
        toPackage("com.sdp.swiftwallet"),
        hasComponent(hasClassName(LoginActivity.class.getName()))
    ));
  }

  @Test
  public void resetWrongFormatEmailGetsFocus() {
    clickOn(R.id.mainNavProfileItem);
    typeTo(R.id.update_emailEt, "dummy");
    onView(withId(R.id.update_email_btn)).perform(click());

    onView(withId(R.id.update_emailEt)).check(matches(hasFocus()));
  }

  @Test
  public void updateEmailSuccessCorrectly(){
    clickOn(R.id.mainNavProfileItem);

    authenticator.setExecSuccess(true);
    authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
    onView(withId(R.id.update_emailEt)).perform(typeText("dummy@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.update_email_btn)).perform(click());

    // check for toast when possible
    onView(withId(R.id.profile_title)).check(matches(isDisplayed()));
  }

  @Test
  public void updateEmailFailsCorrectly() {
    clickOn(R.id.mainNavProfileItem);

    authenticator.setExecFailure(true);
    authenticator.setResult(SwiftAuthenticator.Result.SUCCESS);
    onView(withId(R.id.update_emailEt)).perform(typeText("dummy@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.update_email_btn)).perform(click());

    onView(withId(R.id.update_emailEt)).check(matches(hasFocus()));
  }

  @Test
  public void updateEmailFailsCorrectlyOnAuthSide() {
    clickOn(R.id.mainNavProfileItem);

    authenticator.setResult(SwiftAuthenticator.Result.ERROR);
    onView(withId(R.id.update_emailEt)).perform(typeText("dummy@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.update_email_btn)).perform(click());

    onView(withId(R.id.update_emailEt)).check(matches(hasFocus()));
  }

  @Test
  public void displayUserEmailCorrectly() {
    User profileUser = new User("profileUid", "profile@email.com");
    authenticator.setCurrUser(profileUser);

    clickOn(R.id.mainNavProfileItem);

    onView(withId(R.id.profile_userEmailTv)).check(matches(isDisplayed()));
  }

}