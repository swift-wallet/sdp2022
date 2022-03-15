package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    // Setup bottom navigation listener
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener =
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

    public void startQR(View view){
        Intent intent = new Intent(getActivity(), QRActivity.class);
        startActivity(intent);
    }
    public void openWallet(View view) {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
    }

    public void startLogin(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void startCryptoValues(View view){
        Intent intent = new Intent(getActivity(), CryptoValuesActivity.class);
        startActivity(intent);
    }
}