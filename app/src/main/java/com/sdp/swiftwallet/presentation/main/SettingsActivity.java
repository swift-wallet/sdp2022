package com.sdp.swiftwallet.presentation.main;

import android.os.Bundle;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Settings Activity
 */
@AndroidEntryPoint
public class SettingsActivity extends BaseActivity {

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