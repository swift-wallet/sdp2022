package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionAdapter;
import com.sdp.swiftwallet.domain.model.TransactionHistoryGenerator;

import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Intent i = getIntent();
        TransactionHistoryGenerator historyGenerator =
                (TransactionHistoryGenerator) i.getParcelableExtra(getString(R.string.transactionHistoryGeneratorExtraKey));

        List<Transaction> transactionHistory = historyGenerator.getTransactionHistory();

        RecyclerView recyclerView = findViewById(R.id.transactionsRecyclerView);
        recyclerView.setAdapter(new TransactionAdapter(this, transactionHistory));
        recyclerView.setHasFixedSize(true);
    }
}