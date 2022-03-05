package com.sdp.swiftwallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sdp.cryptowalletapp.R;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="com.sdp.swift-wallet.GREETING";

    private static final String WELCOME_MESSAGE = "Welcome to SwiftWallet!";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        Intent intent = new Intent(this, GreetingActivity.class);

        // Retrieve username and password from login screen
        EditText editText = (EditText) findViewById(R.id.loginUsername);
        String username = editText.getText().toString();

        editText = (EditText) findViewById(R.id.loginPassword);
        String password = editText.getText().toString();

        //Authenticate username and password
        boolean succesfulLogin = (
                username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)
        );

        if (succesfulLogin) {
            //Launch next activity
            intent.putExtra(GreetingActivity.EXTRA_MESSAGE, WELCOME_MESSAGE);
            startActivity(intent);
        } else {
            //Display error message
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setTitle("Unsuccessful login...")
                    .setMessage("Wrong username or password")
                    .setPositiveButton("OK", null)
                    .setCancelable(true);   //TODO : Read setCancelable docs

            AlertDialog errorDialog = alertBuilder.create();
            errorDialog.show();

            //TODO : check why second setter is necessary?
            alertBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {

                        }
                    });
        }
    }
}