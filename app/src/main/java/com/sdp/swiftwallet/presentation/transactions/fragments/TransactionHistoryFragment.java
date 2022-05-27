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
import com.sdp.swiftwallet.domain.model.transaction.Transaction;
import com.sdp.swiftwallet.domain.model.transaction.TransactionAdapter;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistoryProducer;
import com.sdp.swiftwallet.domain.repository.transaction.TransactionHistorySubscriber;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionHistoryFragment} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class TransactionHistoryFragment extends Fragment implements TransactionHistorySubscriber {
    private TransactionActivity rootAct;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactions;

    @Inject
    TransactionHistoryProducer producer;

    @Inject
    SwiftAuthenticator authenticator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootAct = (TransactionActivity) getActivity();

        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    /**
     * On start, fix the view and set up the transactions
     */
    @Override
    public void onStart() {
        super.onStart();

        recyclerView = rootAct.findViewById(R.id.transaction_recyclerView);
        transactions = new ArrayList<>();
        String uid = authenticator.getUid() != null ? authenticator.getUid() : "";
        adapter = new TransactionAdapter(rootAct, transactions, uid);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        while (!producer.subscribe(this));
    }

    @Override
    public void onStop() {
        while (!producer.unsubscribe(this));
        super.onStop();
    }

    @Override
    public void receiveTransactions(List<Transaction> transactions) {
        rootAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String uid = authenticator.getUid() != null ? authenticator.getUid() : "";
                recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions, uid));
            }
        });
    }
}