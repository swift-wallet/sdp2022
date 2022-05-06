package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.CryptoValuesActivity;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    //This is for demo purposes *********************************************
    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Currency> currencyList = new ArrayList<>();

    static {
        currencyList.add(CURR_1);
        currencyList.add(CURR_2);
        currencyList.add(CURR_3);
        currencyList.add(CURR_4);
    } /////*******************************************************************

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
        getActivity().findViewById(R.id.cryptovalues_button)
            .setOnClickListener((v) -> startCryptovalues());
        getActivity().findViewById(R.id.transaction_history_button)
            .setOnClickListener((v) -> startTransactionHistory());
        getActivity().findViewById(R.id.create_transaction_button)
            .setOnClickListener((v) -> createDummyTransaction());
    }

    // Buttons for starting appropriate activities

    private void startCryptovalues() {
        Intent intent = new Intent(getContext(), CryptoValuesActivity.class);
        startActivity(intent);
    }

    private void startTransactionHistory() {
        Intent intent = new Intent(getContext(), TransactionActivity.class);
        startActivity(intent);
    }

    /**
     * Create a dummy transaction, sets it on the firestore
     */
    private void createDummyTransaction() {
        ((MainActivity) getActivity()).getIdlingResource().increment();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Random rand = new Random();
        double amount = -100 + rand.nextDouble() * 200;
        Currency c = currencyList.get(rand.nextInt(currencyList.size()));
        int id = rand.nextInt(1000);

        Transaction t = new Transaction.Builder()
            .setAmount(amount)
                .setCurr(c)
                .setMyWallet(MY_WALL)
                .setTheirWallet(THEIR_WALL)
                .setId(id)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("date", new Timestamp(new Date()));
        data.put("amount", t.getAmount());
        data.put("wallet1", t.getMyWallet());
        data.put("wallet2", t.getTheirWallet());
        data.put("currency", t.getCurr().getSymbol());
        data.put("id", t.getTransactionID());

        db.collection("transactions").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ((MainActivity) getActivity()).getIdlingResource().decrement();
                    Toast.makeText(getActivity().getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}