package com.sdp.swiftwallet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.cryptowalletapp.R;

import org.junit.Rule;
import org.junit.Test;

public class walletActivityTest {
    @Rule
    public ActivityScenarioRule<WalletActivity> testRule = new ActivityScenarioRule<>(WalletActivity.class);

    @Test
    public void displayCurrency() {
        onView(withId(R.id.balanceLabel))
                .check(matches(isDisplayed()));
    }
}
