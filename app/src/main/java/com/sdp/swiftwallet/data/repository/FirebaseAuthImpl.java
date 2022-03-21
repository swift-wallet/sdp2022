package com.sdp.swiftwallet.data.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.ClientAuth;
import com.sdp.swiftwallet.presentation.RegisterActivity;

public class FirebaseAuthImpl implements ClientAuth {
    private static final String EMAIL_REGISTER_TAG = "EMAIL_REGISTER_TAG";
    private static final String EMAIL_SIGNIN_TAG = "EMAIL_SIGNIN_TAG";
    private static final String GOOGLE_SIGNIN_TAG = "GOOGLE_SIGNIN_TAG";

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;

    public FirebaseAuthImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void createUserWithEmailAndPassword(String username, String email, String password, Activity registerActivity, Activity resultActivity) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, email, "BASIC");

                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(registerActivity, "User successfully registered", Toast.LENGTH_LONG).show();
                                            Log.d(EMAIL_REGISTER_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                            registerActivity.startActivity(new Intent(registerActivity, resultActivity.getClass()));
                                            registerActivity.finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(registerActivity, "User failed to register", Toast.LENGTH_LONG).show();
                                            Log.w(EMAIL_REGISTER_TAG, "Error adding document", e);
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(registerActivity, "User failed to register", Toast.LENGTH_LONG).show();
                            Log.w(EMAIL_REGISTER_TAG, "Error from task", task.getException());
                        }
                    }
                });
    }

    public void signInWithEmailAndPassword(String email, String password, Activity signInActivity, Activity resultActivity) {
        Log.d(EMAIL_SIGNIN_TAG, "signInWithEmailAndPassword: begin firebase auth with email account");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signInActivity, "User successfully signedIn", Toast.LENGTH_LONG).show();
                            Log.d(EMAIL_SIGNIN_TAG, "Login successful for email: " + email);

                            signInActivity.startActivity(new Intent(signInActivity, resultActivity.getClass()));
                            signInActivity.finish();
                        }
                        else {
                            Toast.makeText(signInActivity, "User failed to signIn", Toast.LENGTH_SHORT).show();
                            Log.w(EMAIL_SIGNIN_TAG, "Error from task", task.getException());
                        }
                    }
                });
    }

    /**
     * Signs-in a user using google login
     * @param account Google account
     * @param signInActivity activity where signIn is performed
     * @param resultActivity activity launched after signIn is successful
     */
    public void signInWithGoogleAccount(GoogleSignInAccount account, Activity signInActivity, Activity resultActivity) {
        Log.d(GOOGLE_SIGNIN_TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        //Try to sign in the current user
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
                    Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Logged In");
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    String email = firebaseUser.getEmail();

                    Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Email: "+email);
                    Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: UID: "+uid);

                    if (authResult.getAdditionalUserInfo().isNewUser()) {
                        Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Account Created...\n"+email);
                        Toast.makeText(signInActivity, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(GOOGLE_SIGNIN_TAG, "onSuccess: Existing user...\n"+email);
                        Toast.makeText(signInActivity, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();
                    }
                    signInActivity.startActivity(new Intent(signInActivity, resultActivity.getClass()));
                    signInActivity.finish();
                }).addOnFailureListener(e -> Log.d(GOOGLE_SIGNIN_TAG, "onFailure: Loggin failed"+e.getMessage()));
    }

    /**
     * @return check over the firebase if the user has already been registered
     */
    public boolean currUserIsChecked() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null;
    }

    @Override
    public String getCurrUserName() {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    @Override
    public String getCurrUserEmail() {
        return firebaseAuth.getCurrentUser().getEmail();
    }

    /**
     * Signs out the user
     */
    public void signOut() {
        firebaseAuth.signOut();
    }

}
