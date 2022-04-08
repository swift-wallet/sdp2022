package com.sdp.swiftwallet.JavaTest;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.swiftwallet.domain.model.QRCodeScanner;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QRCodeScannerTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void shouldBeAbleToRegisterForAnActivity(){
        scenarioRule.getScenario().moveToState(Lifecycle.State.CREATED).onActivity(activity -> {
            QRCodeScanner qrCodeScanner = new QRCodeScanner(result -> {}, activity);
        });
    }
    @Test
    public void shouldBeAbleToLaunchScanner(){
        scenarioRule.getScenario().moveToState(Lifecycle.State.CREATED).onActivity(activity -> {
            QRCodeScanner qrCodeScanner = new QRCodeScanner(result -> {}, activity);
            qrCodeScanner.launch();
        });
        intended(hasAction("com.google.zxing.client.android.SCAN"));
    }
}
