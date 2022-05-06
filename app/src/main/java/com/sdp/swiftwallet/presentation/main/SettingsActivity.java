package com.sdp.swiftwallet.presentation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdp.cryptowalletapp.R;

/**
 * Settings Activity
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (findViewById(R.id.settings_framelayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // below line is to inflate our fragment.
            getFragmentManager().beginTransaction().add(R.id.settings_framelayout, new SettingsFragment()).commit();
        }
    }
}