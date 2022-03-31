package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CryptoGraphActivity extends AppCompatActivity {
    private Currency currency;
    private String rateSymbol;
    private String interval;
    private ArrayList<Integer> openTimes = new ArrayList<>();
    private ArrayList<Double> openValues = new ArrayList<>();
    private ArrayList<Double> highValues = new ArrayList<>();
    private ArrayList<Double> lowValues = new ArrayList<>();
    private ArrayList<Double> closeValues = new ArrayList<>();
    private ArrayList<Double> volumeValues = new ArrayList<>();
    private ArrayList<Integer> closeTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_graph);

        // Get the Intent that started this activity and extract the currency
        Intent intent = getIntent();
        currency = (Currency) intent.getSerializableExtra("currency");
        rateSymbol = currency.getSymbol()+"USDT";
        interval = "1h";

        // Get the data from Binance API
        getData();
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(openTimes.size());
        System.out.println(openValues.size());
        System.out.println(highValues.size());
        System.out.println(lowValues.size());
        System.out.println(closeValues.size());
        System.out.println(volumeValues.size());
        System.out.println(closeTimes.size());

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.idCurrencyToShowName);
        textView.setText(closeTimes.size());

        //textView.setText("LAST CLOSING TIME : "+closeTimes.get(closeTimes.size()-1) + " LAST CLOSING VALUE : "+closeValues.get(closeValues.size()-1));
    }

    private void getData(){
        String url = "https://api.binance.com/api/v1/klines";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,response -> {
            System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
            System.out.println(response.length());
            try{
                for(int i = 0; i<response.length(); ++i){
                    JSONArray array = response.getJSONArray(i);
                    System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
                    System.out.println(array);
                    openTimes.add((Integer)array.get(0));
                    openValues.add((Double)array.get(1));
                    highValues.add((Double)array.get(2));
                    lowValues.add((Double)array.get(3));
                    closeValues.add((Double)array.get(4));
                    volumeValues.add((Double)array.get(5));
                    closeTimes.add((Integer)array.get(6));
                }
            } catch(Exception e){
                System.out.println("ERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERROR");
                e.printStackTrace();
                Toast.makeText(CryptoGraphActivity.this, "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("NORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSENORESPONSE");
            Toast.makeText(CryptoGraphActivity.this, "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("symbol", "BTCUSDT");
                headers.put("interval", "1h");
                return headers;
        }
    };

        requestQueue.add(jsonArrayRequest);
    }
}