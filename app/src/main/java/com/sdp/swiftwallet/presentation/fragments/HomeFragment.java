package com.sdp.swiftwallet.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.LoginActivity;
import com.sdp.swiftwallet.MainActivity;
import com.sdp.swiftwallet.QRActivity;
import com.sdp.swiftwallet.WalletActivity;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void startQR(View view){
        Intent intent = new Intent(getActivity(), QRActivity.class);
        startActivity(intent);
    }
    public void openWallet(View view) {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
    }

    public void startLogin(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void startCryptoValues(View view){
        Intent intent = new Intent(getActivity(), CryptoValuesActivity.class);
        startActivity(intent);
    }
}