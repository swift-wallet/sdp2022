package com.sdp.swiftwallet.presentation.signIn;

import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseActivity;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Login Activity
 */
@AndroidEntryPoint
public class LoginActivity extends BaseActivity {

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

    /**
     * Method associated to the Forgot Password button
     *
     * @param view the View of the button
     */
    public void forgotPassword(View view) {
        Intent it = new Intent(this, ForgotPasswordActivity.class);
        startActivity(it);
    }

    /**
     * Method associated to the Register button
     *
     * @param view the View of the button
     */
    public void register(View view) {
        Intent it = new Intent(this, RegisterActivity.class);
        startActivity(it);
    }

    /**
     * Method associated to the Use Offline button
     *
     * @param view the View of the button
     */
    public void useOffline(View view) {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    /**
     * Method associated to the Login button
     * Uses the injected SwiftAuthenticator
     *
     * @param view the View of the button
     */
    public void login(View view) {
        // Retrieve username and password from login screen
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        SwiftAuthenticator.Result authRes = authenticator.signIn(email, password,
                () -> {
                    ((BaseApp) getApplication()).setCurrUser(authenticator.getUser().get());
                    nextActivity();
                },
                this::checkAttempts);

        if (authRes != SwiftAuthenticator.Result.SUCCESS) {
            handleError(authRes);
        }
    }

    /**
     * Error handler for the result of the SwiftAuthenticator's
     * signIn method
     *
     * @param result the result of the authentication
     */
    private void handleError(SwiftAuthenticator.Result result) {
        switch (result) {
            case EMPTY_EMAIL:
                displayToast(this, "Email required");
                break;
            case EMPTY_PASSWORD:
                displayToast(this, "Password required");
                break;
            case ERROR:
                authError(LoginActivity.this).show();
                break;
            default:
                break;
        }
    }

    /**
     * Method used to get the next Activity
     * This is passed as a callback to the SwiftAuthenticator
     */
    private void nextActivity() {
        displayToast(this, "User successfully signed in");

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

    /**
     * Creates an AlertDialog to inform the user of an unexpected authentication error
     *
     * @param context the context of the alert
     * @return an AlertDialog informing the user an error occurred while authenticating
     */
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