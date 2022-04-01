package com.sdp.swiftwallet.presentation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.CurrencyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CryptoValuesActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ArrayList<Currency> currencyArrayList;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_values);
        EditText searchCrypto = findViewById(R.id.idCryptoSearch);
        RecyclerView recyclerView = findViewById(R.id.idCryptoCurrencies);
        progressBar = findViewById(R.id.idProgressBar);
        currencyArrayList = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(currencyArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(currencyAdapter);
        getCurrencyData();
        searchCrypto.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterCurrencies(editable.toString());
            }
        });
    }

    private void filterCurrencies(String currency) {
        ArrayList<Currency> filteredList = new ArrayList<>();
        for (Currency item : currencyArrayList) {
            if (item.getName().toLowerCase().contains(currency.toLowerCase()) || item.getSymbol().toLowerCase().contains(currency.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Couldn't find any currency for this query...", Toast.LENGTH_SHORT).show();
        } else {
            currencyAdapter.filterList(filteredList);
        }
    }

    private void addToCurrencyList(){

    }

    private void getCurrencyData() {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.GONE);
            try {
                JSONArray dataArray = response.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String name = dataObject.getString("name");
                    String symbol = dataObject.getString("symbol");
                    JSONObject quote = dataObject.getJSONObject("quote");
                    JSONObject valueJSON = quote.getJSONObject("USD");
                    double value = valueJSON.getDouble("price");
                    currencyArrayList.add(new Currency(name, symbol, value));
                }
                currencyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(CryptoValuesActivity.this, "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CryptoValuesActivity.this, "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();

        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "db2a8973-af74-4b0f-bf14-7f6c84b648d0");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}