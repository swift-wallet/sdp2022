package com.sdp.swiftwallet.presentation.main.fragments;

import static androidx.test.espresso.Espresso.onView;
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
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.sdp.cryptowalletapp.R;
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

import java.util.concurrent.Semaphore;

import javax.inject.Inject;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class ProfileFragmentTest {

  public Context context;
  public CountingIdlingResource mIdlingResource;

  public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(
      MainActivity.class);
  public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

  @Rule
  public RuleChain rule =
      RuleChain.outerRule(hiltRule).around(testRule);

  @Before
  public void setup() {
    Intents.init();
    hiltRule.inject();
    context = ApplicationProvider.getApplicationContext();
    testRule.getScenario().onActivity(activity ->
            mIdlingResource = activity.getIdlingResource()
    );
    IdlingRegistry.getInstance().register(mIdlingResource);

  }

  @After
  public void releaseIntents() {
    Intents.release();
    IdlingRegistry.getInstance().unregister(mIdlingResource);
  }

  @Test
  public void loggingOutRedirectsToLogin() throws InterruptedException {
    clickOn(R.id.mainNavProfileItem);
    clickOn(R.id.logout_Btn);
  }

  @Test
  public void checkElementsAreDisplayed() {
    clickOn(R.id.mainNavProfileItem);
    onView(withId(R.id.logout_Btn)).check(matches(isDisplayed()));
    onView(withId(R.id.reset_email_Btn)).check(matches(isDisplayed()));
    onView(withId(R.id.reset_email_field)).check(matches(isDisplayed()));
  }

  @Test
  public void resetWrongFormatEmailGetsFocus() {
    String wrong_email = "dummy";
    clickOn(R.id.mainNavProfileItem);
    typeTo(R.id.reset_email_field, wrong_email);
    onView(withId(R.id.reset_email_field)).check(matches(hasFocus()));
  }

  @Test
  public void resetEmailNotOnlineDoesNothing() {
    String dummy_email = "dummy@epfl.ch";
    clickOn(R.id.mainNavProfileItem);
    typeTo(R.id.reset_email_field, dummy_email);
    clickOn(R.id.reset_email_Btn);
  }

}