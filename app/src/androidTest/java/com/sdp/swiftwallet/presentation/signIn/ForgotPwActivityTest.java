//package com.sdp.swiftwallet.presentation.signIn;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
//import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.Matchers.allOf;
//
//import androidx.test.espresso.IdlingRegistry;
//import androidx.test.espresso.idling.CountingIdlingResource;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import com.sdp.cryptowalletapp.R;
//import dagger.hilt.android.testing.HiltAndroidRule;
//import dagger.hilt.android.testing.HiltAndroidTest;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.RuleChain;
//import org.junit.runner.RunWith;
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4.class)
//public class ForgotPwActivityTest {
//
//  public ActivityScenarioRule<ForgotPasswordActivity> testRule = new ActivityScenarioRule<>(ForgotPasswordActivity.class);
//  public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
//
//  @Rule
//  public final RuleChain rule =
//          RuleChain.outerRule(hiltRule).around(testRule);
//
//  CountingIdlingResource mIdlingResource;
//
//  @Before
//  public void setUp() {
//    hiltRule.inject();
//    Intents.init();
//  }
//
//  @Before
//  public void registerIdlingResource() {
//    testRule.getScenario().onActivity(activity ->
//            mIdlingResource = activity.getIdlingResource()
//    );
//    IdlingRegistry.getInstance().register(mIdlingResource);
//  }
//
//  @After
//  public void tearDown() {
//    Intents.release();
//  }
//
//  @After
//  public void unregisterIdlingResource() {
//    IdlingRegistry.getInstance().unregister(mIdlingResource);
//  }
//
//  /**
//   * Test that everything is displayed
//   */
//  @Test
//  public void test_display(){
//    onView(withId(R.id.emailField)).check(matches(isDisplayed()));
//    onView(withId(R.id.enterYourEmail)).check(matches(isDisplayed()));
//    onView(withId(R.id.sendReset)).check(matches(isDisplayed()));
//    onView(withId((R.id.goBackForgotPw))).check(matches(isDisplayed()));
//    onView(withId((R.id.appLogo))).check(matches(isDisplayed()));
//  }
//
//  @Test
//  public void no_email_raises_error() {
//    onView(withId(R.id.emailField)).perform(typeText(""), closeSoftKeyboard());
//    onView(withId(R.id.sendReset)).perform(click());
//
//    onView(withId(R.id.emailField)).check(matches(hasFocus()));
//  }
//
//  @Test
//  public void firesCorrectIntentAfterLinkSent(){
//    onView(withId(R.id.emailField)).perform(typeText("anders.hominal@gmail.com"), closeSoftKeyboard());
//    onView(withId(R.id.sendReset)).perform(click());
//
//    intended(allOf(
//            toPackage("com.sdp.swiftwallet"),
//            hasComponent(hasClassName(LoginActivity.class.getName()))
//    ));
//  }
//
//  @Test
//  public void sendLinkFailsCorrectly() {
//    onView(withId(R.id.emailField)).perform(typeText("wrong"), closeSoftKeyboard());
//    onView(withId(R.id.sendReset)).perform(click());
//
//    onView(withId(R.id.sendReset)).check(matches(isDisplayed()));
//  }
//
//  @Test
//  public void backButtonFiresIntent(){
//    onView(withId(R.id.goBackForgotPw)).perform(click());
//
//    intended(allOf(
//            toPackage("com.sdp.swiftwallet"),
//            hasComponent(hasClassName(LoginActivity.class.getName()))
//    ));
//  }
//}
