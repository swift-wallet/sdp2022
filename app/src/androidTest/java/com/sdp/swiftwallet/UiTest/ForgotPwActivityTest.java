package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.signIn.ForgotPasswordActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ForgotPwActivityTest {

  @Rule
  public ActivityScenarioRule<ForgotPasswordActivity> testRule = new ActivityScenarioRule<>(ForgotPasswordActivity.class);

  @Before
  public void setUp() throws Exception {
    Intents.init();
  }

  @After
  public void tearDown() throws Exception {
    Intents.release();
  }

  @Test
  public void test_display(){
    onView(withId(R.id.emailField)).check(matches(isDisplayed()));
    onView(withId(R.id.enterYourEmail)).check(matches(isDisplayed()));
    onView(withId(R.id.sendReset)).check(matches(isDisplayed()));
  }

  /**@Test
  public void no_email_raises_error() {
    onView(withId(R.id.enterYourEmail)).perform(typeText(""), closeSoftKeyboard());
    onView(withId(R.id.emailField)).check(matches(hasFocus()));
  }**/

  /**@Test
  public void firesCorrectIntentAfterLinkSent(){
    onView(withId(R.id.enterYourEmail)).perform(typeText("anders.hominal@gmail.com"), closeSoftKeyboard());
    onView(withId(R.id.sendReset)).perform(click());
  }**/

}
