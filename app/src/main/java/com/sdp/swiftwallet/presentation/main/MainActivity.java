package com.sdp.swiftwallet.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.domain.model.messaging.ChannelManager;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Represents the main activity of the app
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // Used for debugging purpose
    private CountingIdlingResource mIdlingResource;

    // For notification purpose
    private String receive_channel_description;
    private String send_channel_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("Main Calls");

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Create a notification channel as soon as the main activity starts
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
     * Getter for the idling resource (used only in testCase normally)
     * @return the idling resource used by MessageFragment
     */
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the app bar menu
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}