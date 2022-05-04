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
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(JUnit4.class)
public class QRCodeScannerTest {

    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public final RuleChain rule =
            RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void initIntents() {
        hiltRule.inject();
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void shouldBeAbleToRegisterForAnActivity(){
        testRule.getScenario().moveToState(Lifecycle.State.CREATED).onActivity(activity -> {
            QRCodeScanner qrCodeScanner = new QRCodeScanner(result -> {}, activity);
        });
    }
    @Test
    public void shouldBeAbleToLaunchScanner(){
        testRule.getScenario().moveToState(Lifecycle.State.CREATED).onActivity(activity -> {
            QRCodeScanner qrCodeScanner = new QRCodeScanner(result -> {}, activity);
            qrCodeScanner.launch();
        });
        intended(hasAction("com.google.zxing.client.android.SCAN"));
    }
}
