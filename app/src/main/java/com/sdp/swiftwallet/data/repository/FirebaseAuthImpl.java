package com.sdp.swiftwallet.data.repository;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

public class FirebaseAuthImpl implements ClientAuth {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signInWithGoogleAccount(GoogleSignInAccount account, Activity signInActivity, Activity resultActivity, String TAG) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "onSuccess: Logged In");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        Log.d(TAG, "onSuccess: Email: "+email);
                        Log.d(TAG, "onSuccess: UID: "+uid);

                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            Log.d(TAG, "onSuccess: Account Created...\n"+email);
                            Toast.makeText(signInActivity, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d(TAG, "onSuccess: Existing user...\n"+email);
                            Toast.makeText(signInActivity, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();
                        }

                        signInActivity.startActivity(new Intent(signInActivity, resultActivity.getClass()));
                        signInActivity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Loggin failed"+e.getMessage());
                    }
                });
    }

    public boolean isCurrUserChecked() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null;
//        if (user != null) {
//            fromActivity.startActivity(new Intent(fromActivity, toActivity.getClass()));
//            fromActivity.finish();
//        }
    }

    @Override
    public String getCurrUserName() {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    @Override
    public String getCurrUserEmail() {
        return firebaseAuth.getCurrentUser().getEmail();
    }



    public void signOut() {
        firebaseAuth.signOut();
    }

}
