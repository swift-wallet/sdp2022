package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.fragments.HomeFragment;
import com.sdp.swiftwallet.presentation.fragments.MessageFragment;
import com.sdp.swiftwallet.presentation.fragments.PaymentFragment;
import com.sdp.swiftwallet.presentation.fragments.ProfileFragment;
import com.sdp.swiftwallet.presentation.fragments.StatsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    Fragment selectedFragment = null;
    HomeFragment homeFragment;
    StatsFragment statsFragment;
    PaymentFragment paymentFragment;
    MessageFragment messageFragment;
    ProfileFragment profileFragment;
    /**
     * Methods that are called on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
        //Creating all the navigation fragments
        homeFragment = new HomeFragment();
        statsFragment = new StatsFragment();
        messageFragment = new MessageFragment();
        paymentFragment = new PaymentFragment();
        profileFragment = new ProfileFragment();

        fragmentManager = getSupportFragmentManager();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .addToBackStack(null)
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
                    selectedFragment = messageFragment;
                    break;
                case R.id.bar_profile:
                    selectedFragment = profileFragment;
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null)
                        .commit();
                }
                    return true;
        }
        };

}