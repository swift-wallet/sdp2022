package com.sdp.swiftwallet.presentation.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.cryptography.SeedGenerator;

public class CreateSeedActivity extends AppCompatActivity {
    String seed;
    TextView seedView;
    SeedGenerator seedGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seed);
        seedGenerator = new SeedGenerator();
        seed = seedGenerator.getSeed();
        findViewById(R.id.generate_seed_button).setOnClickListener((v) -> reGenerateSeed());
        findViewById(R.id.save_seed_button).setOnClickListener((v) -> saveSeed());
        seedView = (TextView)findViewById(R.id.seed_view);
        seedView.setText(seed);
    }
    private void reGenerateSeed() {
        seedGenerator.reGenerateSeed();
        seed = seedGenerator.getSeed();
        seedView.setText(seed);
    }
    private void saveSeed() {
        seedGenerator.saveSeed(this);
        this.finish();
    }
}