package com.sdp.swiftwallet.domain.model;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScanner extends ActivityResultContract<Integer, String> {

    /* Creating the intent for the qrcode scanning, for now no parameters allowed */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Integer input) {
        IntentIntegrator intentIntegrator = new IntentIntegrator((AppCompatActivity) context);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        return intentIntegrator.createScanIntent();
    }

    @Override
    public String parseResult(int resultCode, @Nullable Intent intent) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(resultCode, intent);
        return intentResult.getContents();
    }
}
