package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.QRCodeGenerator;
import com.sdp.swiftwallet.domain.model.QRCodeScanner;

public class QRActivity extends AppCompatActivity {

    private EditText resultEditText;
    private ImageView imageView;

    private QRCodeScanner scanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        //View variables initialization
        resultEditText = findViewById(R.id.qr_result_edittext);
        imageView = findViewById(R.id.qr_imageView);
        ((Button) findViewById(R.id.qr_button)).setOnClickListener(
                (View v) -> {
                    scanner.launch();
                }
        );
        ((Button) findViewById(R.id.qr_button_generate_qr)).setOnClickListener(
                (View v) ->{
                    generateQR();
                }
        );
        //Scanner initialization
        scanner = new QRCodeScanner(
                (String result) -> {
                    resultEditText.setText(result);
                },
                this
        );

    }

    private void generateQR(){
        String toConvert = resultEditText.getText().toString();
        Bitmap bitmap = null;
        try {
            bitmap = QRCodeGenerator.encodeAsBitmap(toConvert);
        }catch (WriterException exception){
            exception.printStackTrace();
        }
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }

}