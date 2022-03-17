package com.sdp.swiftwallet.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.SignInButton;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.LoginActivity;
import com.sdp.swiftwallet.MainActivity;
import com.sdp.swiftwallet.QRActivity;
import com.sdp.swiftwallet.WalletActivity;

/**
 * Home screen
 */
public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Assign proper methods upon creating view (for buttons)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Set up listeners for button related to activities
        Button walletButton = view.findViewById(R.id.walletButton);
        walletButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openWallet();
            }
        });

        //assign
        Button qrButton = view.findViewById(R.id.qr_goto_button);
        qrButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startQR();
            }
        });

        //assign crypto activities
        Button cryptoButton = view.findViewById(R.id.cryptoValues);
        cryptoButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startCryptoValues();
            }
        });

        return view;
    }
  
    /**
     * start QR activity
     */
    public void startQR(){
        Intent intent = new Intent(getActivity(), QRActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the wallet
     */
    public void openWallet() {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the crypto values
     */
    public void startCryptoValues(){
        Intent intent = new Intent(getActivity(), CryptoValuesActivity.class);
        startActivity(intent);
    }
}