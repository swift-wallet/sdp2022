package com.sdp.swiftwallet.presentation.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.QRCodeGenerator;

/**
 * The activity to show information about a specific wallet
 */
public class WalletInfoActivity extends AppCompatActivity {
    public static final String ADDRESS_EXTRA = "ADDRESS";
    public static final String BALANCE_EXTRA = "BALANCE";

    private String address;
    private ImageView qrView;
    // Those might change or be updated
    private String balance;
    private TextView balanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_info);

        balance = getIntent().getStringExtra(BALANCE_EXTRA);
        balanceView = findViewById(R.id.wallet_info_balance);
        balanceView.setText(balance);

        address = getIntent().getStringExtra(ADDRESS_EXTRA);
        ((TextView)findViewById(R.id.wallet_info_address)).setText(address);
        qrView = findViewById(R.id.qr_info_view);
        generateQR();
    }

    /**
     * Generate the QR code
     */
    private void generateQR(){
        Bitmap bitmap = null;
        try {
            bitmap = QRCodeGenerator.encodeAsBitmap(address);
        }catch (WriterException exception){
            exception.printStackTrace();
        }
        if(bitmap != null){
            qrView.setImageBitmap(bitmap);
        }
    }

}