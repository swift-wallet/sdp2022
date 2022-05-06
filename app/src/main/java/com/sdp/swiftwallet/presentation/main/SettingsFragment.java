package com.sdp.swiftwallet.presentation.main;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import com.sdp.cryptowalletapp.R;

/**
 * Settings Fragment
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add the preferences XML
        addPreferencesFromResource(R.xml.settings_screen);
    }
}