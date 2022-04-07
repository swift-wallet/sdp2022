package com.sdp.swiftwallet.domain.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.User;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

public class AuthenticatorFirebaseImpl implements SwiftAuthenticator {

    private final static String LOG_TAG = "FIREBASE_AUTH_TAG";

    @InstallIn(SingletonComponent.class)
    @EntryPoint
    interface AuthenticatorFirebaseImplEntryPoint {
        FirebaseAuth getFirebaseAuth();
    }

    private FirebaseAuth auth;
    private User currUser = null;

    public AuthenticatorFirebaseImpl() {
        AuthenticatorFirebaseImplEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                    SwiftWalletApp.getAppContext(),
                    AuthenticatorFirebaseImplEntryPoint.class
                );

        auth = hiltEntryPoint.getFirebaseAuth();
    }

    @Override
    public Result signIn(Optional<Credentials> credentials, Runnable success, Runnable failure) {
        if (!credentials.isPresent()) {
            return Result.ERROR;
        }

        Credentials cred = credentials.get();

        if (cred.getEmail().isEmpty()) {
            return Result.NULL_EMAIL;
        }

        if (cred.getPassword().isEmpty()) {
            return Result.NULL_PASSWORD;
        }

        Log.d(LOG_TAG, "Begin firebase authentication");
        auth.signInWithEmailAndPassword(cred.getEmail(), cred.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Login successful for email: " + cred.getEmail());
                        // TODO init user object here
                        success.run();
                    } else {
                        Log.w(LOG_TAG, "Error from task", task.getException());
                        failure.run();
                    }
                });

        return Result.SUCCESS;
    }

    @Override
    public Optional<User> getUser() {
        if (currUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(currUser);
        }
    }
}
