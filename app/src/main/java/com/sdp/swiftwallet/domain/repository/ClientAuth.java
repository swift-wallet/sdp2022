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
    public boolean currUserIsChecked();

    /**
     * Getter for user name
     * @return a String of user name
     */
    public String getCurrUserName();

    /**
     * Getter for user email
     * @return a String of user email
     */
    public String getCurrUserEmail();

    /**
     * Perform SignIn to database using a googleSignIn account and the corresponded TAG for log
     * signInActivity is the activity from where the intent is launched
     * resultActivity is the next activity after signIn is complete
     * @param account a GoogleSignInAccount already setup
     * @param signInActivity the activity that launch signIn
     * @param resultActivity the activity where the intent is fired
     * @param TAG a log TAG corresponding to the signIn
     */
    public void signInWithGoogleAccount(GoogleSignInAccount account, Activity signInActivity, Activity resultActivity, String TAG);

    /**
     * Perform signOut from google account
     */
    public void signOut();
}