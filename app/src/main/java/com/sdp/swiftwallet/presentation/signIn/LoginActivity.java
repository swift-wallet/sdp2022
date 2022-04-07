package com.sdp.swiftwallet.presentation.signIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private TextView attemptsTextView;
    private EditText emailEditText;
    private EditText passwordEditText;

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    @Inject
    SwiftAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attemptsTextView = findViewById(R.id.attemptsMessage);
        attemptsTextView.setText("");

        emailEditText = findViewById(R.id.loginEmailEt);
        passwordEditText = findViewById(R.id.loginPasswordEt);
    }

    public void forgotPassword(View view) {
        Intent it = new Intent(this, ForgotPasswordActivity.class);
        startActivity(it);
    }

    public void register(View view) {
        Intent it = new Intent(this, RegisterActivity.class);
        startActivity(it);
    }

    public void useOffline(View view) {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    /**
     * Login method which is launched by the LOGIN button on the login screen
     * check email and password validity before signIn
     */
    public void login(View view) {
        // Retrieve username and password from login screen
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        SwiftAuthenticator.Result authRes = authenticator.signIn(
                email, password, () -> nextActivity(), () -> checkAttempts()
        );

        if (authRes != SwiftAuthenticator.Result.SUCCESS) {
            handleError(authRes);
        }
    }

    private void handleError(SwiftAuthenticator.Result result) {
        switch (result) {
            case NULL_EMAIL:
                Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show();
                break;
            case NULL_PASSWORD:
                Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                authError(LoginActivity.this).show();
                break;
            default:
                break;
        }
    }

    private void nextActivity() {
        Toast.makeText(this, "User successfully signed in", Toast.LENGTH_LONG).show();

        startActivity(
                new Intent(this, MainActivity.class)
        );
    }

    /**
     * Perform a max attempts check by updating the value and raising error if necessary
     * Also Display error if credentials were wrong
     */
    private void checkAttempts() {
        // Check if over max attempts
        if (++loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            tooManyAttemptsError(this).show();
            return;
        }

        // Set attempts left text
        String attemptsLeft = String.format(
                Locale.US,
                "You have %d attempt(s) remaining",
                MAX_LOGIN_ATTEMPTS - loginAttempts
        );
        attemptsTextView.setText(attemptsLeft);

        // Display error message
        incorrectCredentialsError(LoginActivity.this).show();
    }

    private AlertDialog authError(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle("Authentication error")
                .setMessage("Unexpected error when authenticating")
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .create();
    }

    /**
     * Creates an AlertDialog to inform the user they used incorrect credentials
     *
     * @param context the context of the alert
     * @return an AlertDialog informing the user their login attempt was unsuccessful
     */
    private AlertDialog incorrectCredentialsError(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle("Unsuccessful login")
                .setMessage("Incorrect username or password")
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .create();
    }

    /**
     * Creates an AlertDialog to inform the user they have used up their login attempts
     *
     * @param context the context of the alert
     * @return an AlertDialog informing the user they used up their login attempts
     */
    private AlertDialog tooManyAttemptsError(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle("Too many unsuccessful attempts")
                .setMessage("You have tried to login unsuccessfully too many times")
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .create();
    }

}