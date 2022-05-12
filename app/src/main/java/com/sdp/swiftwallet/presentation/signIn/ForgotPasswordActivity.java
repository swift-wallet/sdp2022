package com.sdp.swiftwallet.presentation.signIn;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;

import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the forgot password process and activity
 */
@AndroidEntryPoint
public class ForgotPasswordActivity extends AppCompatActivity {
  private static final String RESET_PASSWORD_TAG = "RESET_PASSWORD_TAG";
  private static final String COUNTRY = "en";
  private static final String COUNTRY_CODE = "en_gb";

  @Inject SwiftAuthenticator authenticator;

  private EditText emailView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);

    emailView = findViewById(R.id.emailField);

    setListeners();

    // Hardcoded, to be changed
    authenticator.setLanguageCode(COUNTRY);
    authenticator.setLanguageCode(COUNTRY_CODE);
  }

  /**
   * Set all listeners from forgotPasswordActivity
   */
  private void setListeners() {
    Button sendLink = findViewById(R.id.sendReset);
    sendLink.setOnClickListener(v -> checkAndSend(emailView.getText().toString().trim()));

    Button goBack = findViewById(R.id.goBackForgotPw);
    goBack.setOnClickListener(v -> startActivity(
            new Intent(getApplicationContext(), LoginActivity.class)));
  }

  /**
   * Checks and sends the email the reset link if the user exists+correctly formatted
   * By default, we hardcode the
   * @param email email to send
   */
  private void checkAndSend(@NotNull String email){
    if (checkEmail(email, emailView)) {
      sendPasswordResetEmail(email);
    }
  }

  /**
   * Sends a password reset email to the user
   * @param email email that must previously have been checked
   */
  private void sendPasswordResetEmail(String email){
    Log.d(RESET_PASSWORD_TAG, "Trying to send a confirmation email");
    authenticator.sendPasswordResetEmail(email, this::sendSuccess, this::sendFailure);
  }

  /**
   * Callback for successful email sending
   */
  private void sendSuccess() {
    displayToast(getApplicationContext(), "Password successfully sent");

    // Start again login activity if successful
    Intent nextIntent = new Intent(getApplicationContext(), LoginActivity.class);
    startActivity(nextIntent);
  }

  /**
   * Callback for failed email sending
   */
  private void sendFailure() {
    emailView.setError("Reset error, please correct your email!");
    emailView.requestFocus();
  }

}
