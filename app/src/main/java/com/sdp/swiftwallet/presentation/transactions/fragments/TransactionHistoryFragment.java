package com.sdp.swiftwallet.presentation.transactions.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionAdapter;
import com.sdp.swiftwallet.domain.repository.FirebaseTransactionHistoryProducer;
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
//        rootAct = (TransactionActivity) getActivity();
//        recyclerView = rootAct.findViewById(R.id.transaction_recyclerView);
//        ((SwiftWalletApp) rootAct.getApplication()).getTransactionHistoryProducer().subscribe(this);

        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        rootAct = (TransactionActivity) getActivity();
        recyclerView = rootAct.findViewById(R.id.transaction_recyclerView);
        transactions = new ArrayList<>();
        adapter = new TransactionAdapter(rootAct, transactions);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("transactions").orderBy("id").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Transactions Firestore", error.getMessage());
//                    return;
//                }
//
//                transactions.clear();
//                for (DocumentSnapshot snapshot : value) {
//                    double amount = snapshot.getDouble("amount");
//                    Currency curr = FirebaseTransactionHistoryProducer.currencyMap.get(
//                            snapshot.get("currency")
//                    );
//                    int id = snapshot.getLong("id").intValue();
//                    String myWallet = snapshot.getString("wallet1");
//                    String theirWallet = snapshot.getString("wallet2");
//
//                    Transaction t = new Transaction(amount, curr, myWallet, theirWallet, id);
//                    transactions.add(t);
//                }
//
//                recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions));
//            }
//        });

        while (!((SwiftWalletApp) rootAct.getApplication()).getTransactionHistoryProducer().subscribe(this));

    }

    //TODO check if extra steps needed to update the RecyclerView
    @Override
    public void receiveTransactions(List<Transaction> transactions) {
        rootAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions));
            }
        });
//        recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions));
//        recyclerView.setAdapter(new TransactionAdapter(rootAct, transactions));
//        recyclerView.setHasFixedSize(true);
    }
}