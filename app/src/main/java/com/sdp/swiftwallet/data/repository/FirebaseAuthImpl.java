package com.sdp.swiftwallet.data.repository;

import android.app.Activity;
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
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

public class FirebaseAuthImpl implements ClientAuth {
    private static final String EMAIL_REGISTER_TAG = "EMAIL_REGISTER_TAG";
    private static final String EMAIL_SIGNIN_TAG = "EMAIL_SIGNIN_TAG";
    private static final String GOOGLE_SIGNIN_TAG = "GOOGLE_SIGNIN_TAG";
    private static final String RESET_PASSWORD_TAG = "RESET_PASSWORD_TAG";


  private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;

    public FirebaseAuthImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

  /**
   * Creates a User in the Firebase database
   * @param username his username
   * @param email his email
   * @param password his password
   * @param registerActivity from register activity
   * @param resultActivity to login activity back
   */
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

  /**
   * Signs in with basic email / pw
   * @param email email
   * @param password password
   * @param signInActivity from sign in activity
   * @param resultActivity to restult main activity
   */
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
   * Sends a password reset email to the user
   * @param email email that must previously have been checked
   * @param country country code, checked by the check language function
   * @param countryCode country code for language, checked by the check language function
   */
    public void sendPasswordResetEmail(String email, String country, String countryCode, Activity from){
      setLanguage(country, countryCode);
      Log.d(RESET_PASSWORD_TAG, "Trying to send a confirmation email");
      firebaseAuth.sendPasswordResetEmail(email)
          .addOnSuccessListener( a -> {
            Log.d(RESET_PASSWORD_TAG, "Password successfully sent on \n"+email);
            Toast.makeText(from, "Password successfully sent on \n"+email, Toast.LENGTH_SHORT).show();
            //Start again login activity if successful
            Intent nextIntent = new Intent(from, LoginActivity.class);
            from.startActivity(nextIntent);
          }).addOnFailureListener( a -> {
            Log.d(RESET_PASSWORD_TAG, "Something went wrong, please enter a valid email \n"+email);
            Toast.makeText(from, "Reset error, please correct your email! \n"+email, Toast.LENGTH_SHORT).show();
      });
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

  /**
   * Sets up language for authentication
   * @param country country (format "fr=France", ...)
   * @param countryLanguage country language (format "en_gb"= UK british)
   */
  @Override
  public void setLanguage(String country, String countryLanguage){
      firebaseAuth.setLanguageCode(country);
      firebaseAuth.setLanguageCode(countryLanguage);
    }


}
