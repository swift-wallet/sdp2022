package com.sdp.swiftwallet.domain.repository;

import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;
import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.BASIC;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
 */
public class AuthenticatorFirebaseImpl implements SwiftAuthenticator {

    private final static String LOG_TAG = "FIREBASE_AUTH_TAG";
    private final static String REGISTER_TAG = "FIREBASE_REGISTER_TAG";

    @InstallIn(SingletonComponent.class)
    @EntryPoint
    interface AuthenticatorFirebaseImplEntryPoint {
        FirebaseAuth getFirebaseAuth();
    }

    private FirebaseAuth auth;
    private User currUser = null;

    public AuthenticatorFirebaseImpl(@ApplicationContext Context context) {
        AuthenticatorFirebaseImplEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        context,
                        AuthenticatorFirebaseImplEntryPoint.class
                );

        auth = hiltEntryPoint.getFirebaseAuth();
    }

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

    @Override
    public Result signUp(String username, String email, String password, Runnable success, Runnable failure) {
        if (username == null || email == null || password == null) return Result.ERROR;

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        this.currUser = new User(user.getEmail(), BASIC);

                        Log.d(REGISTER_TAG, "Register Success");
                        success.run();
                    }
                    else {
                        Log.w(REGISTER_TAG, "Register Failure: Error from firebase task", task.getException());
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
