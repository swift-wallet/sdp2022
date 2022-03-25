package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class CryptoGraphActivity extends AppCompatActivity {
    private Currency currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_graph);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        currency = (Currency) intent.getSerializableExtra("currency");
        System.out.println(currency.getSymbol());
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.idCurrencyToShowName);
        textView.setText(currency.getName());
    }

    private String getData(){
        AtomicReference<JSONObject> dataArray = null;
        String url = "https://api.binance.com/api/v1/klines?symbol="+currency.getSymbol()+"USDT&interval=1h";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            dataArray.set(response);
            System.out.println(response);
        }, error -> {
            System.out.println("ERROR");
        });
        return dataArray.get().toString();
    }
}