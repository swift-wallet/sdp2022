package com.sdp.swiftwallet.presentation.transactions.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionAdapter;
import com.sdp.swiftwallet.domain.repository.TransactionHistorySubscriber;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionHistoryFragment} factory method to
 * create an instance of this fragment.
 */
public class TransactionHistoryFragment extends Fragment implements TransactionHistorySubscriber {
    private TransactionActivity rootAct;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactions;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootAct = (TransactionActivity) getActivity();

        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView = rootAct.findViewById(R.id.transaction_recyclerView);
        transactions = new ArrayList<>();
        adapter = new TransactionAdapter(rootAct, transactions);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        while (!((SwiftWalletApp) rootAct.getApplication()).getTransactionHistoryProducer().subscribe(this))
            ;
    }

    @Override
    public void onStop() {
        while (!((SwiftWalletApp) rootAct.getApplication()).getTransactionHistoryProducer().unsubscribe(this))
            ;
        super.onStop();
    }

    @Override
    public void receiveTransactions(List<Transaction> transactions) {
        rootAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions));
            }
        });
    }
}