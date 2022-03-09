package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

public class ProfileActivity extends AppCompatActivity {

    private ClientAuth clientAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        clientAuth = new FirebaseAuthImpl();
        checkUser();

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientAuth.signOut();
                checkUser();
            }
        });
    }

    /**
     * Checks if a user is logged
     */
    private void checkUser() {
        if (!clientAuth.isCurrUserChecked()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            String email = clientAuth.getCurrUserEmail();
            TextView emailTv = findViewById(R.id.email);
            emailTv.setText(email);
        }
    }
}