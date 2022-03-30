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
import com.sdp.swiftwallet.domain.repository.TransactionHistoryProducer;
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
    private TransactionHistoryProducer producer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent i = getIntent();
        producer = (TransactionHistoryProducer) i.getParcelableExtra(getString(R.string.transactionHistoryProducerExtraKey));

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
     * Getter for the producer of transaction histories
     *
     * @return the producer of transaction histories
     */
    public TransactionHistoryProducer getProducer() {
        return producer;
    }
}