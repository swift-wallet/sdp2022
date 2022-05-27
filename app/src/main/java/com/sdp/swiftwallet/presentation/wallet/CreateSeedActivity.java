package com.sdp.swiftwallet.presentation.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseActivity;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * The activity to be able to setup the seed of the wallet
 */
@AndroidEntryPoint
public class CreateSeedActivity extends BaseActivity {

    private String seed;
    private EditText seedView;
    private SeedGenerator seedGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seed);
        seedGenerator = new SeedGenerator();
        seed = seedGenerator.getSeed();
        findViewById(R.id.generate_seed_button).setOnClickListener((v) -> reGenerateSeed());
        findViewById(R.id.save_seed_button).setOnClickListener((v) -> saveSeed());
        seedView = findViewById(R.id.seed_view);
        seedView.setText(seed);
    }

    /**
     * Generate a new seed and update the UI
     */
    private void reGenerateSeed() {
        seedGenerator.reGenerateSeed();
        seed = seedGenerator.getSeed();
        seedView.setText(seed);
    }

    /**
     * Saving the seed saves it in the shared preferences and finishes the activity
     */
    private void saveSeed() {
        String[] seedArray = seed.split(" ");
        seedGenerator.saveSeed(this, seedArray);
        this.finish();
    }
}