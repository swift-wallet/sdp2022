package com.sdp.swiftwallet;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;

public class QRActivity extends AppCompatActivity {

    ActivityResultLauncher<Integer> mGetContent = registerForActivityResult(new QRCodeScanner(), new ActivityResultCallback<String>() {
        @Override
        public void onActivityResult(String result) {
            TextView textView = findViewById(R.id.qr_result_textview);
            textView.setText(result);
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        Button qrButton = findViewById(R.id.qr_button);
        qrButton.setOnClickListener(
                (View v) -> {
                    mGetContent.launch(0);
                }
        );
    }

}