package com.sdp.swiftwallet;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.cryptowalletapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRActivityTest {

    @Rule
    public ActivityScenarioRule<QRActivity> testRule = new ActivityScenarioRule<>(QRActivity.class);

    @Test
    public void successfullyLaunchesScanningActivityWhenButtonPressed() {
        Intents.init();

        clickOn(R.id.qr_button);

        intended(hasAction("com.google.zxing.client.android.SCAN"));

        Intents.release();
    }

}