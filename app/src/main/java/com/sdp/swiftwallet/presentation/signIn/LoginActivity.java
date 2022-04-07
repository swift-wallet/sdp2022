package com.sdp.swiftwallet.presentation.signIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.di.FirebaseAuthenticator;
import com.sdp.swiftwallet.di.GoogleAuthenticator;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import java.util.Locale;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private TextView attemptsTextView;
    private EditText emailET;
    private EditText passwordET;

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    private CountingIdlingResource mIdlingResource;

    @FirebaseAuthenticator
    @Inject
    SwiftAuthenticator firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attemptsTextView = (TextView) findViewById(R.id.attemptsMessage);
        attemptsTextView.setText("");

        emailET = (EditText) findViewById(R.id.loginEmailEt);
        passwordET = (EditText) findViewById(R.id.loginPasswordEt);

        // Init idling resource for testing purpose
        mIdlingResource = new CountingIdlingResource("Login Calls");

        // Set Listeners
        Button loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(v -> login());

        TextView forgotPasswordTv = findViewById(R.id.forgotPasswordTv);
        forgotPasswordTv.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
        );

        TextView registerTv = findViewById(R.id.registerTv);
        registerTv.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        TextView useOfflineTv = findViewById(R.id.useOfflineTv);
        useOfflineTv.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, MainActivity.class))
        );
    }

    /**
     * Login method which is launched by the LOGIN button on the login screen
     * check email and password validity before signIn
     */
    public void login() {
        // Retrieve username and password from login screen
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        SwiftAuthenticator.Credentials credentials = new SwiftAuthenticator.Credentials(
                email, password
        );

        SwiftAuthenticator.Result authRes = firebaseAuth.signIn(
                Optional.of(credentials), () -> nextActivity(), () -> checkAttempts()
        );

        if (authRes != SwiftAuthenticator.Result.SUCCESS) {
            handleError(authRes);
        }
    }

    private void nextActivity() {
        Toast.makeText(this, "User successfully signed in", Toast.LENGTH_LONG).show();

        startActivity(
                new Intent(this, MainActivity.class)
        );
    }

    private void handleError(SwiftAuthenticator.Result result) {
        switch (result) {
            case NULL_EMAIL:
                Toast.makeText(this, "Email required", Toast.LENGTH_LONG).show();
                break;
            case NULL_PASSWORD:
                Toast.makeText(this, "Password required", Toast.LENGTH_LONG).show();
                break;
            case ERROR:
                Toast.makeText(this, "An unexpected error occured", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
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

    /**
     * Getter for idling resource, used for testing
     *
     * @return idling resource used in this activity
     */
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

}