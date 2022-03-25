package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;

import java.io.Serializable;

public class CryptoGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_graph);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Currency message = (Currency) intent.getSerializableExtra("currency");
        System.out.println(message.getSymbol());
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView222);
        textView.setText(message.getName());
    }
}