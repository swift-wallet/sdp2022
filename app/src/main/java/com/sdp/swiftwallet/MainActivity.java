package com.sdp.swiftwallet;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    Fragment selectedFragment = null;

    /**
     * Methods that are called on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
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
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.bar_stats:
                    selectedFragment = new StatsFragment();
                    break;
                case R.id.bar_payment:
                    selectedFragment = new PaymentFragment();
                    break;
                case R.id.bar_message:
                    selectedFragment = new MessageFragment();
                    break;
                case R.id.bar_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
                }
                    return true;
        }
        };
}