package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdp.cryptowalletapp.R;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SecureRandom;

public class CryptographyActivity extends AppCompatActivity {
    TextView puK;
    TextView pK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptography);
        puK = findViewById(R.id.public_key_view);
        pK = findViewById(R.id.private_key_view);
        ((Button)findViewById(R.id.generate_keypair_button)).setOnClickListener((v)->{CreateAddress();});
    }

    public void CreateAddress(){
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);

        SecureRandom secureRandom = new SecureRandom(); // Could implement the seed by passing bytes into the constructor
        byte[] privKey = new byte[32];
        secureRandom.nextBytes(privKey);
        BigInteger privKeyInt = (new BigInteger(privKey)).abs();
        BigInteger pubKeyInt = Sign.publicKeyFromPrivate(privKeyInt);
        puK.setText(privKeyInt.toString(16));
        pK.setText(pubKeyInt.toString(16));
        try{
            byte[] encodedPK = Hash.sha3(pubKeyInt.toString(16).getBytes());
            byte[] finalPK = new byte[20];
            for(int i = 0; i<20; i++){
                finalPK[i] = encodedPK[12 + i];
            }
            puK.setText(Numeric.toHexString(finalPK));
            ((EditText)findViewById(R.id.copy_addr)).setText(Numeric.toHexString(finalPK));
            toast.setText("Length of encoded: "+encodedPK.length+" length of pub: "+pubKeyInt.toString(16).getBytes().length);
            toast.show();
        }
        catch(Exception e){
            toast.setText(e.toString());
            toast.show();
        }

    }
}