package com.sdp.swiftwallet.presentation.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.domain.repository.TransactionHistoryGenerator;
import com.sdp.swiftwallet.presentation.transactions.fragments.TransactionHistoryFragment;
import com.sdp.swiftwallet.presentation.transactions.fragments.TransactionStatsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Transaction Activity screen
 */
public class TransactionActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment historyFragment, statsFragment;
    private Fragment activeFragment;
    private List<Transaction> transactions = new ArrayList<>();

    //This is for demo purposes *********************************************
    private final static Currency CURR_1 = new Currency("DumbCoin", "DUM", 5);
    private final static Currency CURR_2 = new Currency("BitCoin", "BTC", 3);
    private final static Currency CURR_3 = new Currency("Ethereum", "ETH", 4);
    private final static Currency CURR_4 = new Currency("SwiftCoin", "SWT", 6);
    private final static String MY_WALL = "MY_WALL";
    private final static String THEIR_WALL = "THEIR_WALL";
    private final static List<Transaction> dummyList = new ArrayList<>();

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
            dummyList.add(t);
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
                    i.getParcelableExtra(getString(R.string.transactionHistoryGeneratorExtraKey));
            transactions = historyGenerator.getTransactionHistory();
        } catch (Exception e) {
            transactions = dummyList;
        }
        //**********************************************************

        historyFragment = new TransactionHistoryFragment();
        statsFragment = new TransactionStatsFragment();
        activeFragment = historyFragment;
        fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.transaction_flFragment, historyFragment, TransactionHistoryFragment.class.getName())
                .add(R.id.transaction_flFragment, statsFragment, TransactionStatsFragment.class.getName())
                .hide(statsFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    /**
     * Function used to switch to the history fragment
     *
     * @param view the current View
     */
    public void openHistory(View view) {
        fragmentManager
                .beginTransaction()
                .hide(activeFragment)
                .show(historyFragment)
                .setReorderingAllowed(true)
                .commit();
        activeFragment = historyFragment;
    }

    /**
     * Function used to switch to the stats fragment
     *
     * @param view the current View
     */
    public void openStats(View view) {
        fragmentManager
                .beginTransaction()
                .hide(activeFragment)
                .show(statsFragment)
                .setReorderingAllowed(true)
                .commit();
        activeFragment = statsFragment;
    }

    /**
     * Getter for the list of transactions.
     * This is used by the history and stats fragments to get the list of transactions
     *
     * @return the list of transactions
     */
    public List<Transaction> getList() {
        return new ArrayList<>(transactions);
    }
}