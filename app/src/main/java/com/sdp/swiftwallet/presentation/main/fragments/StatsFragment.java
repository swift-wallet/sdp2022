package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.TransactionHistoryActivity;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //This is temporary
        ((Button)getActivity().findViewById(R.id.cryptovalues_button)).setOnClickListener((v) -> startCryptovalues());
        ((Button)getActivity().findViewById(R.id.transaction_history_button)).setOnClickListener((v) -> startTransactionHistory());
    }

    private void startCryptovalues(){
        Intent intent = new Intent(getContext(), CryptoValuesActivity.class);
        startActivity(intent);
    }
    private void startTransactionHistory(){
        Intent intent = new Intent(getContext(), TransactionActivity.class);
        startActivity(intent);
    }
}