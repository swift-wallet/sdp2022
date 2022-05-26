package com.sdp.swiftwallet.domain.object.messaging;

import static org.junit.Assert.*;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.domain.model.messaging.NotificationBuilder;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class ChannelManagerTest {

    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    private static String channel = "DEBUG";
    private static String notifName = "debug notification";
    private static String notifDescription = "for testing";

    // Init dummy intents
    private static Intent intent = new Intent();
    private static PendingIntent notificationIntent;
    private Context context;

    @Rule
    public final RuleChain rule = RuleChain.outerRule(hiltRule).around(testRule);

    @Before
    public void init(){
        Intents.init();
        context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
        notificationIntent=PendingIntent
                .getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }


    @Test(expected = Test.None.class /* no exception expected */)
    public void setUpBasicNotification(){
        NotificationCompat.Builder builder = NotificationBuilder
                .buildNotification(context,"RECEIVE", "TEST", Constants.RECEIVE_CHANNEL, notificationIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }

    // Currently not possible to test actual to test content of the notification nor the fact that the .notify
    // has been called is case of subscribe
}