package com.sdp.swiftwallet.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionAdapter;

import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        List<Transaction> transactionHistory = Transaction.genDummyHistory();

        RecyclerView recyclerView = findViewById(R.id.transactionsRecyclerView);
        recyclerView.setAdapter(new TransactionAdapter(this, transactionHistory));
        recyclerView.setHasFixedSize(true);
    }
}