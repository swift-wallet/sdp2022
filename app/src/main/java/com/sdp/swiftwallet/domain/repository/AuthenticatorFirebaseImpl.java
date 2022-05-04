package com.sdp.swiftwallet.domain.repository;

import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.BASIC;

import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.swiftwallet.domain.model.User;
import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import java.util.Optional;

/**
 * SwiftAuthenticator implementation using FirebaseAuth, needed for Hilt
 * contains useful functions related to authentication
 */
public class AuthenticatorFirebaseImpl implements SwiftAuthenticator {

    private final static String LOG_TAG = "FIREBASE_AUTH_TAG";

    @InstallIn(SingletonComponent.class)
    @EntryPoint
    interface AuthenticatorFirebaseImplEntryPoint {
        FirebaseAuth getFirebaseAuth();
    }

    private final FirebaseAuth auth;
    private User currUser = null;

    public AuthenticatorFirebaseImpl(@ApplicationContext Context context) {
        AuthenticatorFirebaseImplEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        context,
                        AuthenticatorFirebaseImplEntryPoint.class
                );
        auth = hiltEntryPoint.getFirebaseAuth();
    }

    /**
     * Signs in the given user
     * @param email    the email of the user
     * @param password the password of the user
     * @param success  a callback to run if the user successfully logs in
     * @param failure  a callback to run if the user fails to log in
     * @return
     */
    @Override
    public Result signIn(String email, String password, Runnable success, Runnable failure) {
        if (email == null || password == null) {
            return Result.ERROR;
        }

        if (email.isEmpty()) {
            return Result.EMPTY_EMAIL;
        }

        if (password.isEmpty()) {
            return Result.EMPTY_PASSWORD;
        }

        Log.d(LOG_TAG, "Begin firebase authentication");
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Login successful for email: " + email);

                        // Initialize user upon successful login
                        // Suppose either can be null because we just registered them
                        FirebaseUser u = auth.getCurrentUser();
                        this.currUser = new User(u.getEmail(), BASIC);
                        success.run();
                    } else {
                        Log.w(LOG_TAG, "Error from task", task.getException());
                        failure.run();
                    }
                });

        return Result.SUCCESS;
    }

    /**
     * Returns the current user
     */
    @Override
    public Optional<User> getUser() {
        if (currUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(currUser);
        }
    }


}
