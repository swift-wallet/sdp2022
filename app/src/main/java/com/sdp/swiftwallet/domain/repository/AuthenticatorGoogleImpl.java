package com.sdp.swiftwallet.domain.repository;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.swiftwallet.SwiftWalletApp;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;

public class AuthenticatorGoogleImpl {

    private final static String LOG_TAG = "GOOGLE_SIGNIN_TAG";

    private FirebaseAuth auth;

    @InstallIn(SingletonComponent.class)
    @EntryPoint
    interface AuthenticatorGoogleImplEntryPoint {
        FirebaseAuth getFirebaseAuth();
    }

    public AuthenticatorGoogleImpl() {
        AuthenticatorGoogleImplEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SwiftWalletApp.getAppContext(),
                        AuthenticatorGoogleImplEntryPoint.class
                );

        auth = hiltEntryPoint.getFirebaseAuth();
    }

    private void initResultLauncher() {

    }
}
