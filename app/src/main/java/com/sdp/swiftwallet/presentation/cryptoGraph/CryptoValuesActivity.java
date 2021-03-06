package com.sdp.swiftwallet.presentation.cryptoGraph;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseActivity;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.currency.CurrencyAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity displaying crypto values
 */
@AndroidEntryPoint
public class CryptoValuesActivity extends BaseActivity {

    // UI elementss
    private ProgressBar progressBar;
    private String showUSDTOnly;
    private ArrayList<Currency> currencyArrayList;
    private CurrencyAdapter currencyAdapter;
    private Spinner showAllSpinner;
    // For testing purposes
    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_values);
        EditText searchCrypto = findViewById(R.id.idCryptoSearch);
        RecyclerView recyclerView = findViewById(R.id.idCryptoCurrencies);
        progressBar = findViewById(R.id.idProgressBar);

        showUSDTOnly = "Show USDT Only";

        currencyArrayList = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(currencyArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(currencyAdapter);
        mIdlingResource = new CountingIdlingResource("CryptoValue Calls");
        setShowAllSpinner();
        mIdlingResource.increment();
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
                mIdlingResource.increment();
                filterCurrencies(editable.toString());
                mIdlingResource.decrement();
            }
        });
    }

    /**
     * Add to the currency filtered list the currency
     * @param currency the currency to add
     */
    private void filterCurrencies(String currency) {
        ArrayList<Currency> filteredList = new ArrayList<>();
        for (Currency item : currencyArrayList) {
            if (item.getName().toLowerCase().contains(currency.toLowerCase())
                || item.getSymbol().toLowerCase().contains(currency.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Couldn't find any currency for this query...", Toast.LENGTH_SHORT).show();
        } else {
            currencyAdapter.filterList(filteredList);
        }
    }

    /**
     * Returns all data for current currencies
     */
    private void getCurrencyData() {
        progressBar.setVisibility(View.VISIBLE);
        currencyArrayList.clear();
        String url = "https://api.binance.com/api/v3/ticker/24hr";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.GONE);
            try {
                for (int i = 0; i<response.length(); ++i) {
                    JSONObject dataObject = response.getJSONObject(i);
                    String symbol = dataObject.getString("symbol");
                    if (showUSDTOnly.contains("Show All")){
                        String name = dataObject.getString("symbol");
                        double value = dataObject.getDouble("lastPrice");
                        currencyArrayList.add(new Currency(name, symbol, value));
                    } else if (symbol.endsWith("USDT")) {
                        String name = dataObject.getString("symbol");
                        name = name.substring(0, name.length() - 4);
                        double value = dataObject.getDouble("lastPrice");
                        currencyArrayList.add(new Currency(name, symbol, value));
                    }
                }
                currencyAdapter.notifyDataSetChanged();
                mIdlingResource.decrement();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CryptoValuesActivity.this,
                    "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(CryptoValuesActivity.this,
                "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();
        });

        // Add to the queue the request
        requestQueue.add(jsonArrayRequest);
    }


    private void setShowAllSpinner(){
        this.showAllSpinner = findViewById(R.id.idSpinnerShowAll);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.show_USDT_only, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.showAllSpinner.setAdapter(adapter);

        this.showAllSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelectedHandler(adapterView, view, i, l);
                showUSDTOnly = (String)adapterView.getItemAtPosition(i);
                mIdlingResource.increment();
                getCurrencyData();
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

    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

}