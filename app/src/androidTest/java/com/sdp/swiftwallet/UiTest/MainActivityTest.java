package com.sdp.swiftwallet.UiTest;

import static androidx.core.os.BundleKt.bundleOf;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.MainActivity;
import com.sdp.swiftwallet.data.repository.UserDatabase;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.presentation.fragments.ProfileFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void bottom_bar_with_icons_is_displayed() {
        onView(withId(R.id.bottom_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_home)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_stats)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_payment)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_message)).check(matches(isDisplayed()));
        onView(withId(R.id.bar_profile)).check(matches(isDisplayed()));
    }

    @Test
    public void click_display_fragments() {
        onView(withId(R.id.bar_home)).perform(click());
        onView(withId(R.id.bar_stats)).perform(click());
        onView(withId(R.id.bar_payment)).perform(click());
        onView(withId(R.id.bar_message)).perform(click());
        onView(withId(R.id.bar_profile)).perform(click());
    }
}