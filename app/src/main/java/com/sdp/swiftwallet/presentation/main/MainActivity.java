package com.sdp.swiftwallet.presentation.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

    private FragmentManager fragmentManager;
    Fragment selectedFragment = null;
    HomeFragment homeFragment;
    StatsFragment statsFragment;
    PaymentFragment paymentFragment;
    MessageFragment messageFragment;
    FriendFragment friendFragment;
    ProfileFragment profileFragment;

    Fragment activeFragment;

    /**
     * Methods that are called on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((SwiftWalletApp) getApplication()).setTransactionHistoryProducer(
                new FirebaseTransactionHistoryProducer(FirebaseFirestore.getInstance())
        );

        ((BottomNavigationView)findViewById(R.id.bottom_bar)).setOnItemSelectedListener(navigationItemSelectedListener);
        //Creating all the navigation fragments
        homeFragment = new HomeFragment();
        statsFragment = new StatsFragment();
        friendFragment = new FriendFragment();
        paymentFragment = new PaymentFragment();
        profileFragment = new ProfileFragment();

        activeFragment = homeFragment;

        fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container,homeFragment, HomeFragment.class.getName())
                .add(R.id.fragment_container,statsFragment, StatsFragment.class.getName())
                .add(R.id.fragment_container,friendFragment, FriendFragment.class.getName())
                .add(R.id.fragment_container,paymentFragment, PaymentFragment.class.getName())
                .add(R.id.fragment_container,profileFragment, ProfileFragment.class.getName())
                .hide(statsFragment).hide(friendFragment).hide(paymentFragment).hide(profileFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    /**
     * Setup bottom navigation listener on the menu
     */
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener =

        //Sets up on click listener to fragments of the home activity
        new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bar_home:
                    selectedFragment = homeFragment;
                    break;
                case R.id.bar_stats:
                    selectedFragment = statsFragment;
                    break;
                case R.id.bar_payment:
                    selectedFragment = paymentFragment;
                    break;
                case R.id.bar_message:
                    selectedFragment = friendFragment;
                    break;
                case R.id.bar_profile:
                    selectedFragment = profileFragment;
                    break;
            }

            if (selectedFragment != null) {
                fragmentManager
                        .beginTransaction()
                        .hide(activeFragment)
                        .show(selectedFragment)
                        .setReorderingAllowed(true)
                        .commit();
                activeFragment = selectedFragment;
                }
                    return true;
        }
        };

}