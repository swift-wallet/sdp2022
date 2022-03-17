package com.sdp.swiftwallet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.sdp.swiftwallet.LOGIN";
    private static final String WELCOME_MESSAGE = "Welcome to SwiftWallet!";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    private TextView attemptsTextView;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    private ClientAuth clientAuth;
    private ActivityResultLauncher<Intent> googleSignInActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attemptsTextView = (TextView) findViewById(R.id.attemptsMessage);
        attemptsTextView.setText("");

        // init client authentication and launcher for google signIn
        clientAuth = new FirebaseAuthImpl();
        initGoogleSignInResultLauncher();

        SignInButton googleSignInBtn = findViewById(R.id.googleSignInBtn);
        googleSignInBtn.setOnClickListener(v -> startGoogleSignIn());
    }

    /**
     * Login method which is launched by the LOGIN button on the login screen
     *
     * @param view current View of the user
     */
    public void login(View view) {
        // Retrieve username and password from login screen
        EditText editText = (EditText) findViewById(R.id.loginUsername);
        String username = editText.getText().toString();
        editText = (EditText) findViewById(R.id.loginPassword);
        String password = editText.getText().toString();

        // Authenticate username and password
        boolean successfulLogin = authCredentials(username, password);

        if (successfulLogin) {
            // Launch next activity
            startActivity(nextActivity(this));
        } else {
            // Check if over max attempts
            if (++loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                tooManyAttemptsError(this, mainIntent).show();
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
            incorrectCredentialsError(this).show();
        }
    }

    /**
     * Prepares the intent for the next intent to launch when the login is successful
     *
     * @param context the context of the current Activity
     * @return an Intent to launch the next activity
     */
    private Intent nextActivity(Context context) {
        Intent nextActivity = new Intent(context, MainActivity.class);
        nextActivity.putExtra(EXTRA_MESSAGE, WELCOME_MESSAGE);
        return nextActivity;
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
     * @param context    the context of the alert
     * @param homeScreen the Intent of the Activity the user is sent to (i.e. the home screen)
     * @return an AlertDialog informing the user they used up their login attempts which also
     * starts another activity
     */
    private AlertDialog tooManyAttemptsError(Context context, Intent homeScreen) {
        return new AlertDialog.Builder(context)
                .setTitle("Too many unsuccessful attempts")
                .setMessage("You have tried to login unsuccessfully too many times")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(homeScreen);
                    }
                })
                .setCancelable(false)
                .create();
    }

    /**
     * Authenticates the user-provided credentials
     *
     * @param username the user-provided username
     * @param password the user-provided password
     * @return whether or not the credentials correspond to a valid account or not
     */
    private boolean authCredentials(String username, String password) {
        return (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD));
    }

    /**
     * Init google SignIn launcher,
     * Perform signIn through clientAuth by giving this activity and the result activity
     */
    private void initGoogleSignInResultLauncher() {
        googleSignInActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "onActivityResult: Google signIn intent result");
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                            clientAuth.signInWithGoogleAccount(account, LoginActivity.this, new MainActivity(), TAG);
                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: " + e.getMessage());
                        }
                    }
                });
    }

    /**
     * Setup GoogleSignInClient and launch google signIn with intent
     */
    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        Log.d(TAG, "onClick: launch Google signIn");
        Intent it = googleSignInClient.getSignInIntent();
        googleSignInActivityResultLauncher.launch(it);
    }
}