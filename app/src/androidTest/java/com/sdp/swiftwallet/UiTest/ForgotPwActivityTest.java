package com.sdp.swiftwallet.UiTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.ForgotPasswordActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ForgotPwActivityTest {

  @Rule
  public ActivityScenarioRule<ForgotPasswordActivity> testRule = new ActivityScenarioRule<>(ForgotPasswordActivity.class);

  @Before
  public void initIntents() {
    Intents.init();
  }

  @Test
  public void test_display(){
    onView(withId(R.id.emailField)).check(matches(isDisplayed()));
    onView(withId(R.id.enterYourEmail)).check(matches(isDisplayed()));
    onView(withId(R.id.sendReset)).check(matches(isDisplayed()));
  }


}
