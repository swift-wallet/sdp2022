package com.sdp.swiftwallet.UiTest;

import static android.service.autofill.Validators.not;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.Root;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoGraphActivity;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.domain.model.Currency;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CryptoGraphActivityTest {
    CountingIdlingResource mIdlingResource;
    Currency ethereum;
    @Rule
    public ActivityScenarioRule<CryptoGraphActivity> testRule = new ActivityScenarioRule<>(CryptoGraphActivity.class);

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
    public void closeSystemDialogs() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @After
    public void releaseIntents(){
        Intents.release();
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void changeIntervalShows5m(){
        onView(withId(R.id.idInterval)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.idInterval)).check(matches(withSpinnerText(containsString("5m"))));
    }

    /*@Test
    public void changeIntervalShowsToastMessage(){
        onView(withId(R.id.idInterval)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withText("Selected Interval: 30m")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }*/

    @Test
    public void chartNameAndSpinnerCorrectlyDisplayed(){
        onView(withId(R.id.idInterval)).check(matches(isDisplayed()));
        onView(withId(R.id.idCurrencyToShowName)).check(matches(isDisplayed()));
        onView(withId(R.id.candle_stick_chart)).check(matches(isDisplayed()));
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    //means this window isn't contained by any other windows.
                }
            }
            return false;
        }
    }

}
