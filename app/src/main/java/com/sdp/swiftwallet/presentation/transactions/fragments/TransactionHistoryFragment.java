package com.sdp.swiftwallet.presentation.transactions.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.TransactionHistoryActivity;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionAdapter;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionHistoryFragment} factory method to
 * create an instance of this fragment.
 */
public class TransactionHistoryFragment extends Fragment {
    private TransactionActivity rootAct;
    private List<Transaction> list;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootAct = (TransactionActivity) getActivity();
        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    @Override
    public void onStart() {
        recyclerView = rootAct.findViewById(R.id.transactionsRecyclerView);
        list = rootAct.getList();
        recyclerView.setAdapter(new TransactionAdapter(rootAct, list));
        recyclerView.setHasFixedSize(true);
        super.onStart();
    }
}