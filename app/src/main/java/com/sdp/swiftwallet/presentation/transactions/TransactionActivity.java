package com.sdp.swiftwallet.presentation.transactions;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.transactions.fragments.TransactionHistoryFragment;
import com.sdp.swiftwallet.presentation.transactions.fragments.TransactionStatsFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Transaction Activity screen
 */
@AndroidEntryPoint
public class TransactionActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment historyFragment, statsFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

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
}