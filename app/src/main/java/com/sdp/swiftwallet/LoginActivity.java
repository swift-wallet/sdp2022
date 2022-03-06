package com.sdp.swiftwallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="com.sdp.swift-wallet.GREETING";

    private static final String WELCOME_MESSAGE = "Welcome to SwiftWallet!";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

//    private TextView ATTEMPTS_TEXTVIEW;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

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
//            String attemptsLeft = String.format(
//                    Locale.US,
//                    "You have %d attempts remaining",
//                    MAX_LOGIN_ATTEMPTS - loginAttempts
//            );
//            String test = "HELLO";
//            ATTEMPTS_TEXTVIEW.setText(test);

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
        Intent nextActivity = new Intent(this, GreetingActivity.class);
        nextActivity.putExtra(GreetingActivity.EXTRA_MESSAGE, WELCOME_MESSAGE);
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
     * @param context the context of the alert
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
}