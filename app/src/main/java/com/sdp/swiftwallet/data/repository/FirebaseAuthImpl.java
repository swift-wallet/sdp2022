package com.sdp.swiftwallet.data.repository;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

/**
 * Implementation of a FirebaseAuthentication system
 */
public class FirebaseAuthImpl implements ClientAuth {
    private final FirebaseAuth firebaseAuth;

    /**
     * Constructor for FireBaseAuthImpl
     */
    public FirebaseAuthImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signInWithGoogleAccount(GoogleSignInAccount account, Activity signInActivity, Activity resultActivity, String TAG) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");

        // Setup credential from account token
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    // For success, get email to display a Toast message that confirm connection
                    Log.d(TAG, "onSuccess: Logged In");

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser == null) {
                        throw new NullPointerException("No user connected");
                    }

                    String uid = firebaseUser.getUid();
                    String email = firebaseUser.getEmail();

                    Log.d(TAG, "onSuccess: Email: "+email);
                    Log.d(TAG, "onSuccess: UID: "+uid);

                    // Get different message if it's the first time user signIn
                    if (authResult.getAdditionalUserInfo().isNewUser()) {
                        Log.d(TAG, "onSuccess: Account Created...\n"+email);
                        Toast.makeText(signInActivity, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "onSuccess: Existing user...\n"+email);
                        Toast.makeText(signInActivity, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();
                    }

                    // Finally start the resultActivity once done
                    signInActivity.startActivity(new Intent(signInActivity, resultActivity.getClass()));
                    signInActivity.finish();
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: Loggin failed"+e.getMessage()));
    }

    @Override
    public boolean currUserIsChecked() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public String getCurrUserName() {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    @Override
    public String getCurrUserEmail() {
        return firebaseAuth.getCurrentUser().getEmail();
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

}
