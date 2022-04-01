package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;

import org.json.JSONArray;

import java.util.ArrayList;

public class CryptoGraphActivity extends AppCompatActivity {
    private Spinner intervalSpinner;
    private ArrayList<String> intervalsText;
    private ArrayList<String> intervalsForRequest;
    private Currency currency;
    private String rateSymbol;
    private String interval;
    private ArrayList<Long> openTimes = new ArrayList<>();
    private ArrayList<Double> openValues = new ArrayList<>();
    private ArrayList<Double> highValues = new ArrayList<>();
    private ArrayList<Double> lowValues = new ArrayList<>();
    private ArrayList<Double> closeValues = new ArrayList<>();
    private ArrayList<Double> volumeValues = new ArrayList<>();
    private ArrayList<Long> closeTimes = new ArrayList<>();

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


        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.idCurrencyToShowName);
        //textView.setText(closeTimes.size());
        textView.setText(currency.getName());

        //textView.setText("LAST CLOSING TIME : "+closeTimes.get(closeTimes.size()-1) + " LAST CLOSING VALUE : "+closeValues.get(closeValues.size()-1));
    }

    private void getData(){
        String url = "https://api.binance.com/api/v1/klines?symbol=ETHUSDT&interval=1h";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,response -> {
            try{
                for(int i = 0; i<response.length(); ++i){
                    JSONArray array = response.getJSONArray(i);
                    openTimes.add((Long)array.get(0));
                    openValues.add(Double.parseDouble((String)array.get(1)));
                    highValues.add(Double.parseDouble((String)array.get(2)));
                    lowValues.add(Double.parseDouble((String)array.get(3)));
                    closeValues.add(Double.parseDouble((String)array.get(4)));
                    volumeValues.add(Double.parseDouble((String)array.get(5)));
                    closeTimes.add((Long)array.get(6));
                }
            } catch(Exception e){
                e.printStackTrace();
                Toast.makeText(CryptoGraphActivity.this, "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(CryptoGraphActivity.this, "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void setIntervalsSpinner(){
        this.intervalSpinner = (Spinner) findViewById(R.id.idInterval);
        this.intervalsText = Interval.getTextToShowUser();
        this.intervalsForRequest = Interval.getIntervalForRequest();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intervalsText);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.intervalSpinner.setAdapter(adapter);

        this.intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelectedHandler(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id){
        Adapter adapter = adapterView.getAdapter();
        String interval = (String) adapter.getItem(position);

        Toast.makeText(getApplicationContext(), "Selected Interval: " + interval, Toast.LENGTH_SHORT).show();
    }
}