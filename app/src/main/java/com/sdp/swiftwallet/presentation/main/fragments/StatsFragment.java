package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        ((Button)getActivity().findViewById(R.id.create_transaction_button)).setOnClickListener((v) -> createDummyTransaction());
    }

    private void startCryptovalues(){
        Intent intent = new Intent(getContext(), CryptoValuesActivity.class);
        startActivity(intent);
    }
    private void startTransactionHistory(){
        Intent intent = new Intent(getContext(), TransactionActivity.class);
        startActivity(intent);
    }

    private void createDummyTransaction() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Random rand = new Random();

        Map<String, String> map = new HashMap<>();
        map.put("number", Integer.toString(rand.nextInt()));

        db.collection("transactions").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}