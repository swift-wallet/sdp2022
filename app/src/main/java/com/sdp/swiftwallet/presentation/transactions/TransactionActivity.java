package com.sdp.swiftwallet.presentation.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionActivity extends AppCompatActivity {

    //This is for demo purposes *********************************************
    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Transaction> list = new ArrayList<>();

    static {
        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(CURR_1);
        currencies.add(CURR_2);
        currencies.add(CURR_3);
        currencies.add(CURR_4);
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            double amount = -100 + r.nextDouble() * 200;
            int curr = r.nextInt(4);
            Transaction t = new Transaction(amount, currencies.get(curr), MY_WALL, THEIR_WALL, i);
            list.add(t);
        }
    } /////*******************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent i = getIntent();
        //**********************************************************************
        List<Transaction> transactionHistory;
        try {
            TransactionHistoryGenerator historyGenerator =
                    (TransactionHistoryGenerator) i.getParcelableExtra(getString(R.string.transactionHistoryGeneratorExtraKey));
            transactionHistory = historyGenerator.getTransactionHistory();
        } catch (Exception e) {
            transactionHistory = list;
        }
        //**********************************************************

//        RecyclerView recyclerView = findViewById(R.id.transactionsRecyclerView);
//        recyclerView.setAdapter(new TransactionAdapter(this, transactionHistory));
//        recyclerView.setHasFixedSize(true);
    }

    public List<Transaction> getList() {
        return new ArrayList<>(list);
    }
}