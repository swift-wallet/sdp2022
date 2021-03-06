package com.sdp.swiftwallet.domain.model.currency;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.sdp.swiftwallet.domain.model.currency.CurrencyBank.PriceChecker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * CurrencyBank.PriceChecker implementation using RequestQueue
 */
public class RequestQueueCurrencyPriceChecker implements PriceChecker {

    private final static String MARKET_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private final static List<String> supportedCurrencies = new ArrayList<>();

    private static final String API = "X-CMC_PRO_API_KEY";
    private static final String KEY = "db2a8973-af74-4b0f-bf14-7f6c84b648d0";

    /**
     * Add symbols of app's supported currencies here
     * For the moment only Bitcoin and Ethereum are supported
     */
    static {
        supportedCurrencies.add("BTC");
        supportedCurrencies.add("ETH");
    }

    private final RequestQueue queue;

    public RequestQueueCurrencyPriceChecker(@ApplicationContext Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Fill the queue with currencies
     * @param currencyMap the Map to fill with Currency objects
     */
    @Override
    public void fill(Map<String, Currency> currencyMap) {
        JsonObjectRequest jsonObjectRequest =
            new JsonObjectRequest( Request.Method.GET, MARKET_URL, null, response -> {
            try {
                JSONArray dataArray = response.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);

                    String symbol = dataObject.getString("symbol");

                    // Add to currency only if it's not contained
                    if (!supportedCurrencies.contains(symbol)) {
                        continue;
                    }

                    String name = dataObject.getString("name");
                    JSONObject quote = dataObject.getJSONObject("quote");
                    JSONObject valueJSON = quote.getJSONObject("USD");
                    double value = valueJSON.getDouble("price");

                    Currency curr = new Currency(name, symbol, value);

                    currencyMap.put(symbol, curr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not get currencies");
            }
        }, error -> {
            throw new RuntimeException("Could not get currencies");
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(API, KEY);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
