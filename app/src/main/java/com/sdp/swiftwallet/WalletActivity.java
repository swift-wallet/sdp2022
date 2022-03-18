package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;

import java.security.KeyStore;

public class WalletActivity extends AppCompatActivity {

    /**
     * Methods called on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        createText();
    }

    private void createText() {
        LinearLayout walletLayout = findViewById(R.id.wallet_layout2);

        final int N = 20; // total number of linear layouts
        final String[] labels = new String[]{"BitCoin", "Ethereum", "apple", "oranges","BitCoin", "Ethereum", "apple", "oranges","BitCoin", "Ethereum", "apple", "oranges","BitCoin", "Ethereum", "apple", "oranges","BitCoin", "Ethereum", "apple", "oranges"};
        final double[] balances = {0, 0, 0, 1,0, 0, 0, 1,0, 0, 0, 1,0, 0, 0, 1,0, 0, 0, 1};
        final LinearLayout[] currencyList = new LinearLayout[N];
        LinearLayout listlayout = new LinearLayout(this);
        listlayout.setOrientation(LinearLayout.VERTICAL);
        listlayout.setShowDividers(LinearLayout.SHOW_DIVIDER_END);
        listlayout.canScrollHorizontally(LinearLayout.HORIZONTAL);
        listlayout.setPadding(100,0,0,0);

        for (int i = 0; i < N; i++) {
            // create layout for currency
            LinearLayout currencylayout = new LinearLayout(this);
            currencylayout.setOrientation(LinearLayout.HORIZONTAL);
            currencylayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            TextView currencyLabel = new TextView(this);
            currencyLabel.setText(labels[i]);
            currencyLabel.setTextSize(24);
            currencyLabel.setWidth(400);
            LinearLayout.LayoutParams labellayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            labellayoutParams.setMargins(0, 20, 30, 0);

            currencyLabel.setLayoutParams(labellayoutParams);
            TextView currencyBalance = new TextView(this);
            currencyBalance.setText(String.format("%s", balances[i]));
            currencyBalance.setTextSize(24);
            LinearLayout.LayoutParams balancelayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            balancelayoutParams.setMargins(100, 20, 30, 0);
            balancelayoutParams.setMarginEnd(LinearLayout.TEXT_ALIGNMENT_VIEW_END);
            currencyBalance.setLayoutParams(balancelayoutParams);

            currencylayout.addView(currencyLabel);
            currencylayout.addView(currencyBalance);
            // save a reference for later
            currencyList[i] = currencylayout;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(50, 20, 30, 0);
            listlayout.addView(currencylayout, layoutParams);

            Log.e("Ran", "layout"+":"+listlayout.getChildCount());
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(listlayout, layoutParams);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        walletLayout.addView(scroll, scrollParams);
    }
}
