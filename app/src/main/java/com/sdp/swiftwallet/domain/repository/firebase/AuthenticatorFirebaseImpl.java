package com.sdp.swiftwallet.domain.repository.firebase;

import static com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator.LoginMethod.BASIC;

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

/**
 * SwiftAuthenticator implementation using FirebaseAuth, needed for Hilt
 * contains useful functions related to authentication
 */
public class AuthenticatorFirebaseImpl implements SwiftAuthenticator {

    private final static String LOG_TAG = "FIREBASE_AUTH_TAG";
    private final static String REGISTER_TAG = "FIREBASE_REGISTER_TAG";
    private final static String RESET_PASSWORD_TAG = "FIREBASE_RESET_PASSWORD_TAG";
    private final static String UPDATE_EMAIL_TAG = "FIREBASE_UPDATE_EMAIL_TAG";

    @InstallIn(SingletonComponent.class)
    @EntryPoint
    interface AuthenticatorFirebaseImplEntryPoint {
        FirebaseAuth getFirebaseAuth();
    }

    private final FirebaseAuth auth;
    private User currUser;

    public AuthenticatorFirebaseImpl(@ApplicationContext Context context) {
        AuthenticatorFirebaseImplEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        context,
                        AuthenticatorFirebaseImplEntryPoint.class
                );
        auth = hiltEntryPoint.getFirebaseAuth();
        checkForUser();
    }

    private void checkForUser() {
        FirebaseUser oldUser = auth.getCurrentUser();
        if (oldUser != null) {
            currUser = new User(oldUser.getUid(), oldUser.getEmail());
        }
        else {
            currUser = null;
        }
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
                        // Suppose either can be null because we just registered them
                        FirebaseUser u = auth.getCurrentUser();
                        this.currUser = new User(u.getUid(), u.getEmail());
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
                        this.currUser = new User(user.getUid(), user.getEmail());

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
    public void signOut() {
        auth.signOut();
        currUser = null;
    }

    @Override
    public Result sendPasswordResetEmail(String email, Runnable success, Runnable failure) {
        if (email == null) return Result.ERROR;

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(command -> {
                    Log.d(RESET_PASSWORD_TAG, "Password successfully sent on " + email);
                    success.run();
                }).addOnFailureListener(command -> {
                    Log.d(RESET_PASSWORD_TAG, "Password reset failed at auth step");
                    failure.run();
                });

        return Result.SUCCESS;
    }

    @Override
    public Result updateUserEmail(String email, Runnable success, Runnable failure) {
        if (currUser == null || email == null) return Result.ERROR;

        FirebaseUser mUser = auth.getCurrentUser();
        if (mUser != null) {
            mUser.updateEmail(email)
                    .addOnSuccessListener(command -> {
                        Log.d(UPDATE_EMAIL_TAG, "email successfully updated to " + email);
                        currUser.setEmail(email);
                        success.run();
                    }).addOnFailureListener(command -> {
                        Log.d(UPDATE_EMAIL_TAG, "email failed to update to " + email);
                        failure.run();
                    });
        } else {
            return Result.ERROR;
        }

        return Result.SUCCESS;
    }

    @Override
    public User getUser() {
        return currUser;
    }

    @Override
    public String getUid() {
        if (currUser != null) {
            return currUser.getUid();
        }
        else {
            return null;
        }
    }

    @Override
    public void setLanguageCode(String code) {
        auth.setLanguageCode(code);
    }

}
