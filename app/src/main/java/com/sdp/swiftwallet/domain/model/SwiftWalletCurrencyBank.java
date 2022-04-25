package com.sdp.swiftwallet.domain.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class SwiftWalletCurrencyBank implements CurrencyBank {
    private final Map<String, Currency> currencyMap;

    public SwiftWalletCurrencyBank(CurrencyBank.PriceChecker priceChecker) {
        currencyMap = new HashMap<>();
        priceChecker.fill(currencyMap);
    }

    @Override
    public Currency getCurrency(String symbol) {
        return currencyMap.get(symbol);
    }
}
