package com.sdp.swiftwallet.presentation.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;

public class WalletInfoActivity extends AppCompatActivity {
    private WalletKeyPair walletKeyPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_info);
    }

}