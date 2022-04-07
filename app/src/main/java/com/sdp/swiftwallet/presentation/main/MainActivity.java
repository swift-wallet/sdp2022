package com.sdp.swiftwallet.presentation.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.SwiftWalletApp;
import com.sdp.swiftwallet.domain.repository.FirebaseTransactionHistoryProducer;
import com.sdp.swiftwallet.presentation.friend.FriendFragment;
import com.sdp.swiftwallet.presentation.main.fragments.HomeFragment;
import com.sdp.swiftwallet.presentation.main.fragments.MessageFragment;
import com.sdp.swiftwallet.presentation.main.fragments.PaymentFragment;
import com.sdp.swiftwallet.presentation.main.fragments.ProfileFragment;
import com.sdp.swiftwallet.presentation.main.fragments.StatsFragment;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        ((SwiftWalletApp) getApplication()).setTransactionHistoryProducer(
                new FirebaseTransactionHistoryProducer(FirebaseFirestore.getInstance())
        );
}