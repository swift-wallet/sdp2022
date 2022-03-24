package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.model.TransactionAdapter;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionHistoryActivity extends AppCompatActivity {

    //This is for demo purposes ***
    private final static Currency CURR = new Currency("DumbCoin", "DUM", 9.5);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Transaction> list = new ArrayList<>();

    static {
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            double amount = -100 + r.nextDouble() * 200;
            Transaction t = new Transaction(amount, CURR, MY_WALL, THEIR_WALL, i);
            list.add(t);
        }
    } /////***

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Intent i = getIntent();
        List<Transaction> transactionHistory;
        try{
            TransactionHistoryGenerator historyGenerator =
                    (TransactionHistoryGenerator) i.getParcelableExtra(getString(R.string.transactionHistoryGeneratorExtraKey));
            transactionHistory = historyGenerator.getTransactionHistory();
        }
        catch(Exception e){
           transactionHistory = list;
        }
        RecyclerView recyclerView = findViewById(R.id.transaction_recyclerView);
        recyclerView.setAdapter(new TransactionAdapter(this, transactionHistory));
        recyclerView.setHasFixedSize(true);
    }
}