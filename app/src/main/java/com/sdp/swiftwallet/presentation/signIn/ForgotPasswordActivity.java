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
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {


  private ClientAuth dbClient;
  private FirebaseAuth mAuth;
  private final String COUNTRY = "en";
  private final String COUNTRY_CODE = "en_gb";


  private EditText emailView;
  private static final String RESET_PASSWORD_TAG = "RESET_PASSWORD_TAG";

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    //Set the content
    setContentView(R.layout.activity_reset_password);

    //Sets up the db
    mAuth = FirebaseUtil.getAuth();
    setLanguage(COUNTRY, COUNTRY_CODE);

    emailView = findViewById(R.id.emailField);

    //Sets up the button for confirming
    Button sendLink = findViewById(R.id.sendReset);
    sendLink.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        checkAndSend(emailView.getText().toString().trim());
      }
    });

    Button goBack = findViewById(R.id.goBackForgotPW);
    goBack.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        ForgotPasswordActivity.this.startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
      }
    });

    //Hardcoded, to be changed
    setLanguage(COUNTRY, COUNTRY_CODE);
  }


  /**
   * Checks and sends the email the reset link if the user exists+correctly formatted
   * By default, we hardcode the
   * @param email email to send
   */
  public void checkAndSend(@NotNull String email){

    if (checkEmail(email, emailView)) {
      sendPasswordResetEmail(email, this);
    }
  }

  /**
   * Sends a password reset email to the user
   * @param email email that must previously have been checked
   */
  public void sendPasswordResetEmail(String email, Activity from){
    Log.d(RESET_PASSWORD_TAG, "Trying to send a confirmation email");
    mAuth.sendPasswordResetEmail(email)
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
   * Sets up language for authentication
   * @param country country (format "fr=France", ...)
   * @param countryLanguage country language (format "en_gb"= UK british)
   */
  public void setLanguage(String country, String countryLanguage){
    mAuth.setLanguageCode(COUNTRY);
    mAuth.setLanguageCode(COUNTRY_CODE);
  }

}
