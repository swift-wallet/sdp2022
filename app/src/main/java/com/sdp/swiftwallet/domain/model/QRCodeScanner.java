package com.sdp.swiftwallet.domain.model;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
/*
    QRCodeScanner class
    Launches a QR code scanning activity using the available camera
 */
public class QRCodeScanner {
    //A simple interface to define a lambda callback
    public interface ScannedCallback{
        void callBack(String result);
    }
    ActivityResultLauncher<Integer> mGetContent;

    /*
    *   QRCode scanner constructor takes a callback that will be called with the result scanned string,
    *   and the activity context.
     */
    public QRCodeScanner(ScannedCallback callback, AppCompatActivity context){
        mGetContent = context.registerForActivityResult(new QRCodeScannerContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String result) {
                callback.callBack(result);
            }
        });
    }
    //Use this function to launch the scanning process
    public void launch(){
        mGetContent.launch(0);
    }
    private static class QRCodeScannerContract extends ActivityResultContract<Integer, String> {
        /* Creating the intent for the qrcode scanning, for now no parameters allowed */
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer input) {
            IntentIntegrator intentIntegrator = new IntentIntegrator((AppCompatActivity) context);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            intentIntegrator.setBeepEnabled(false);
            return intentIntegrator.createScanIntent();
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(resultCode, intent);
            return intentResult.getContents();
        }
    }
}
