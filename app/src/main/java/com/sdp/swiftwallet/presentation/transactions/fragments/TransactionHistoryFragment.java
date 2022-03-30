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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionHistoryFragment} factory method to
 * create an instance of this fragment.
 */
public class TransactionHistoryFragment extends Fragment implements TransactionHistorySubscriber {
    private TransactionActivity rootAct;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootAct = (TransactionActivity) getActivity();
        recyclerView = rootAct.findViewById(R.id.transaction_recyclerView);
        ((SwiftWalletApp) rootAct.getApplication()).getTransactionHistoryProducer().subscribe(this);

        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

//    @Override
//    public void onStart() {
//        recyclerView = rootAct.findViewById(R.id.transaction_recyclerView);
//        super.onStart();
//    }

    //TODO check if extra steps needed to update the RecyclerView
    @Override
    public void receiveTransactions(List<Transaction> transactions) {
        recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions));
        recyclerView.setHasFixedSize(true);
    }
}