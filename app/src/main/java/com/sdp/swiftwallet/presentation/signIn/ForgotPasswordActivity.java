package com.sdp.swiftwallet.presentation.signIn;

import static com.sdp.swiftwallet.common.HelperFunctions.*;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private static final String COUNTRY = "en";
  private static final String COUNTRY_CODE = "en_gb";

  private EditText emailView;
  private static final String RESET_PASSWORD_TAG = "RESET_PASSWORD_TAG";

  private CountingIdlingResource mIdlingResource;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    //Set the content
    setContentView(R.layout.activity_reset_password);

    //Sets up the db
    mAuth = FirebaseUtil.getAuth();

    // Init idling resource for testing purpose
    mIdlingResource = new CountingIdlingResource("ForgotPW Calls");

    emailView = findViewById(R.id.emailField);

    //Sets up the button for confirming
    Button sendLink = findViewById(R.id.sendReset);
    sendLink.setOnClickListener(v -> checkAndSend(emailView.getText().toString().trim()));

    Button goBack = findViewById(R.id.goBackForgotPW);
    goBack.setOnClickListener(v -> startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class)));

    //Hardcoded, to be changed
    mAuth.setLanguageCode(COUNTRY);
    mAuth.setLanguageCode(COUNTRY_CODE);
  }

  /**
   * Checks and sends the email the reset link if the user exists+correctly formatted
   * By default, we hardcode the
   * @param email email to send
   */
  public void checkAndSend(@NotNull String email){
    if (checkEmail(email, emailView)) {
      mIdlingResource.increment();
      sendPasswordResetEmail(email);
    }
  }

  /**
   * Sends a password reset email to the user
   * @param email email that must previously have been checked
   */
  public void sendPasswordResetEmail(String email){
    Log.d(RESET_PASSWORD_TAG, "Trying to send a confirmation email");
    mAuth.sendPasswordResetEmail(email)
        .addOnSuccessListener( a -> {
          Log.d(RESET_PASSWORD_TAG, "Password successfully sent on \n"+email);
          Toast.makeText(this, "Password successfully sent on \n"+email, Toast.LENGTH_SHORT).show();
          //Start again login activity if successful
          Intent nextIntent = new Intent(this, LoginActivity.class);
          mIdlingResource.decrement();
          startActivity(nextIntent);
        }).addOnFailureListener( a -> {
      Log.d(RESET_PASSWORD_TAG, "Something went wrong, please enter a valid email \n"+email);
      Toast.makeText(this, "Reset error, please correct your email! \n"+email, Toast.LENGTH_SHORT).show();
      mIdlingResource.decrement();
    });
  }

  /**
   * Getter for idling resource, used for testing
   * @return idling resource used in this activity
   */
  public CountingIdlingResource getIdlingResource() {
    return mIdlingResource;
  }

}
