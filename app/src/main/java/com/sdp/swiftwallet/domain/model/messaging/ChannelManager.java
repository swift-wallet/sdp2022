package com.sdp.swiftwallet.domain.model.messaging;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import io.reactivex.annotations.NonNull;

public class ChannelManager {

    private final String channelId;

    /**
     * Create a channel manager useful for creation a channel
     * @param channelId if of the desired channel
     */
    public ChannelManager(@NonNull String channelId){
        this.channelId=channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    /**
     * Create a notification channel useful for notifications
     * @param context context (activity)
     * @param channelId id of the desired channel
     * @param channel_description description of the channel (purpose)
     */
    public void createNotificationChannel(@NonNull Context context,@NonNull String channelId, @NonNull String channel_description) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}

