package com.sdp.swiftwallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="com.sdp.swift-wallet.GREETING";

    private static final String WELCOME_MESSAGE = "Welcome to SwiftWallet!";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private TextView ATTEMPTS_TEXTVIEW;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ATTEMPTS_TEXTVIEW = (TextView) findViewById(R.id.loginAttempts);
    }

    public void login(View view) {
        Intent greetingIntent = new Intent(this, GreetingActivity.class);

        // Retrieve username and password from login screen
        EditText editText = (EditText) findViewById(R.id.loginUsername);
        String username = editText.getText().toString();

        editText = (EditText) findViewById(R.id.loginPassword);
        String password = editText.getText().toString();

        // Authenticate username and password
        boolean successfulLogin = (
                username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)
        );

        if (successfulLogin) {
            // Launch next activity
            greetingIntent.putExtra(GreetingActivity.EXTRA_MESSAGE, WELCOME_MESSAGE);
            startActivity(greetingIntent);
        } else {
            // Check if over max attempts
            if (++loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                new AlertDialog.Builder(this)
                        .setTitle("Too many unsuccessful attempts")
                        .setMessage("You have tried to login unsuccessfully too many times")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(mainIntent);
                            }
                        })
                        .setCancelable(true)
                        .create()
                        .show();
            }

            // Set attempts left text
            String attemptsLeft = String.format(
                    "You have %d attempts remaining",
                    MAX_LOGIN_ATTEMPTS - loginAttempts
            );
            String test = "HELLO";
            ATTEMPTS_TEXTVIEW.setText(test);

            // Display error message
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setTitle("Unsuccessful login...")
                    .setMessage("Incorrect username or password")
                    .setPositiveButton("OK", null)
                    .setCancelable(true);

            AlertDialog errorDialog = alertBuilder.create();
            errorDialog.show();
        }
    }
}