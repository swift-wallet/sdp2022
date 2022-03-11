package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sdp.cryptowalletapp.R;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="com.sdp.swift-wallet.NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void sendMessage(View view){
        //Create a new intent to be able to start a new GreetingActivity
        Intent intent = new Intent(this, GreetingActivity.class);
        //Find the EditText object from the main layout by its id;
        EditText editText = (EditText) findViewById(R.id.mainName);
        String message = editText.getText().toString();
        //Put extra parameter to the intent
        intent.putExtra(GreetingActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void startQR(View view){
        Intent intent = new Intent(this, QRActivity.class);
        startActivity(intent);
    }

    public void startLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startCryptoValues(View view){
        Intent intent = new Intent(this, CryptoValuesActivity.class);
        startActivity(intent);
    }
}