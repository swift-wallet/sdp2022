package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;


/**
 * Example class of activity, mainly used as a first test
 */
public class GreetingActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.sdp.swift-wallet.GREETING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        //Get the intent that started the activity and extract the string  message.
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.mainGreeting);
        textView.setText(message);
    }
}