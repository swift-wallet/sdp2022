package com.sdp.swiftwallet.domain.repository;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface ClientAuth {

    public boolean isCurrUserChecked();

    public String getCurrUserName();

    public String getCurrUserEmail();

    public void signInWithGoogleAccount(GoogleSignInAccount account, Activity signInActivity, Activity resultActivity, String TAG);

    public void signOut();
}
