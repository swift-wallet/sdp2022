package com.sdp.swiftwallet.presentation.transactions;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.domain.model.messaging.ChannelManager;
import com.sdp.swiftwallet.presentation.transactions.fragments.TransactionHistoryFragment;
import com.sdp.swiftwallet.presentation.transactions.fragments.TransactionStatsFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Transaction Activity screen
 */
@AndroidEntryPoint
public class TransactionActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment historyFragment, statsFragment;
    private Fragment activeFragment;


    // For notification purpose
    private String receive_channel_description;
    private String send_channel_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        historyFragment = new TransactionHistoryFragment();
        statsFragment = new TransactionStatsFragment();
        activeFragment = historyFragment;
        fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.transaction_flFragment, historyFragment, TransactionHistoryFragment.class.getName())
                .add(R.id.transaction_flFragment, statsFragment, TransactionStatsFragment.class.getName())
                .hide(statsFragment)
                .setReorderingAllowed(true)
                .commit();

        // Create a notification channel as soon as the fragment activity starts
        // Will be changed later with proper send and receive functions
        receive_channel_description = "Channel useful for receiving crypto";
        send_channel_description = "Channel useful for sending crypto";
        ChannelManager receive =
                new ChannelManager(Constants.RECEIVE_CHANNEL);
        ChannelManager send =
                new ChannelManager(Constants.SEND_CHANNEL);
        receive.createNotificationChannel(this, receive.getChannelId(), receive_channel_description);
        send.createNotificationChannel(this, send.getChannelId(), send_channel_description);
    }

    /**
     * Function used to switch to the history fragment
     *
     * @param view the current View
     */
    public void openHistory(View view) {
        fragmentManager
                .beginTransaction()
                .hide(activeFragment)
                .show(historyFragment)
                .setReorderingAllowed(true)
                .commit();
        activeFragment = historyFragment;
    }

    /**
     * Function used to switch to the stats fragment
     *
     * @param view the current View
     */
    public void openStats(View view) {
        fragmentManager
                .beginTransaction()
                .hide(activeFragment)
                .show(statsFragment)
                .setReorderingAllowed(true)
                .commit();
        activeFragment = statsFragment;
    }
}