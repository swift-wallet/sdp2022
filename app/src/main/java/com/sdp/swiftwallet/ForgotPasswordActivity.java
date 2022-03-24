package com.sdp.swiftwallet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;
import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

  private ClientAuth dbClient;
  private String country;
  private String countryCodeLanguage;

  private EditText emailView;


  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    //Set the content
    setContentView(R.layout.activity_reset_password);

    // Get the contents of the email fieldemailView = findViewById(R.id.emailField);
    country = "en";
    countryCodeLanguage = "en_gb";

    emailView = findViewById(R.id.emailField);

    // Sets up the button for confirming
    Button sendLink = findViewById(R.id.sendReset);
    sendLink.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        checkAndSend(emailView.getText().toString().trim());
      }
    });

    dbClient = new FirebaseAuthImpl();
    //Hardcoded, to be changed
    dbClient.setLanguage("en", "en_gb");
  }


  /**
   * Checks and sends the email the reset link if the user exists+correctly formatted
   * By default, we hardcode the
   * @param email email to send
   */
  public void checkAndSend(@NotNull String email){

    if (isEmailValid(email)) {
      dbClient.sendPasswordResetEmail(email, country, countryCodeLanguage, this);
    } else {
      Toast.makeText(this,
          "Erro, please check again the format of the email", Toast.LENGTH_LONG).show();
    }

  }


  /**
   * Check if email is correctly formatted, might become a function called everywhere
   * It's purely a 'String' format method
   */
  private Boolean isEmailValid(String email) {
    if (email.isEmpty()) {
      emailView.setError("Email required");
      emailView.requestFocus();
      return false;
    }
    return true;
  }



}
