package com.sdp.swiftwallet.domain.repository;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Interface for Client Authentication
 */
public interface ClientAuth {

    /**
     * Check if there is a current user connected
     * @return True if the user is not null
     */
    boolean currUserIsChecked();

    /**
     * Getter for user name
     * @return a String of user name
     */
    String getCurrUserName();


    /**
     * Sets up language for authentication
     * @param country country (format "fr=France", ...)
     * @param countryLanguage country language (format "en_gb"= UK british)
     */
    void setLanguage(String country, String countryLanguage);



    /**
     * Getter for user email
     * @return a String of user email
     */
    String getCurrUserEmail();

    void createUserWithEmailAndPassword(String username, String email, String password, Activity signInActivity, Activity resultActivity);

    void signInWithEmailAndPassword(String email, String password, Activity signInActivity, Activity resultActivity);

    void sendPasswordResetEmail(String email, String country, String countryCode, Activity from);


    /**
     * Perform SignIn to database using a googleSignIn account and the corresponded TAG for log
     * signInActivity is the activity from where the intent is launched
     * resultActivity is the next activity after signIn is complete
     * @param account a GoogleSignInAccount already setup
     * @param signInActivity the activity that launch signIn
     * @param resultActivity the activity where the intent is fired
     */
    void signInWithGoogleAccount(GoogleSignInAccount account, Activity signInActivity, Activity resultActivity);

    /**
     * Perform signOut from google account
     */
    void signOut();
}
