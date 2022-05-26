package com.sdp.swiftwallet.domain.model.messaging;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.sdp.cryptowalletapp.R;

import io.reactivex.annotations.NonNull;

public class NotificationBuilder {


    /**
     * Builds a notification on the app
     * @param context activity
     * @param textTitle text title
     * @param textContent text content to display on the notification
     * @param channel channel on where to publish
     * @param intent intent
     * @return
     */
    public static NotificationCompat.Builder buildNotification(Context context,
           @NonNull String textTitle, @NonNull String textContent, @NonNull String channel,@NonNull PendingIntent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_baseline_attach_money_24)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setAutoCancel(true);
        return builder;
    }


}
