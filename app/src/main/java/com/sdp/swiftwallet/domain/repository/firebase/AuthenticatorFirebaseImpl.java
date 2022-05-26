package com.sdp.swiftwallet.domain.repository.firebase;

import static com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator.LoginMethod.BASIC;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.swiftwallet.common.HelperFunctions;
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
    private final static String REGISTER_TAG = "FIREBASE_REGISTER_TAG";
    private static final String RESET_PASSWORD_TAG = "FIREBASE_RESET_PASSWORD_TAG";

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
                        this.currUser = new User(u.getUid(), u.getEmail(), BASIC);
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
                        this.currUser = new User(user.getUid(), user.getEmail(), BASIC);
                        Log.d(REGISTER_TAG, "Register Success");
                        success.run();
                    } else {
                        Log.w(REGISTER_TAG, "Register Failure: Error from firebase task",
                            task.getException());
                        failure.run();
                    }
                });
        return Result.SUCCESS;
    }


    public void signOut(Runnable handler) {
        auth.signOut();
        handler.run();
    }

    public Result updateEmail(String email, EditText emailField, Runnable success,
        Runnable failure) {

        FirebaseUser mUser = auth.getCurrentUser();
        // First check sanity of the email
        if (!HelperFunctions.checkEmail(email, emailField)) {
            return Result.ERROR_EMAIL_FORMAT;
        }
        // Then perform the actual update
        if (mUser != null) {
            mUser.updateEmail(email).addOnSuccessListener(a -> {
                success.run();
            }).addOnFailureListener(a -> {
                failure.run();
            });
        } else {
            return Result.ERROR_NOT_ONLINE;
        }
        return Result.SUCCESS;
    }

    @Override
    public Result sendPasswordResetEmail(String email, Runnable success, Runnable failure) {
        if (email == null) return Result.ERROR;

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(command -> {
                    Log.d(RESET_PASSWORD_TAG, "Password successfully sent on \n" + email);
                    success.run();
                }).addOnFailureListener(command -> {
                    Log.d(RESET_PASSWORD_TAG, "Password reset failed at auth step" + email);
                    failure.run();
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

    @Override
    public Optional<String> getUid() {
        if (currUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(currUser.getUid());
        }
    }

    @Override
    public void setLanguageCode(String code) {
        auth.setLanguageCode(code);
    }

}
