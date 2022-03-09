package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

public class ProfileActivity extends AppCompatActivity {

    private ClientAuth clientAuth;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate: Profile launched");
        clientAuth = new FirebaseAuthImpl();
        checkUser();

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            clientAuth.signOut();
            checkUser();
        });
    }

    /**
     * Check if there is a current user,
     * If yes, display email
     * If no, return to MainActivity
     */
    private void checkUser() {
        if (clientAuth.currUserIsChecked()) {
            String email = clientAuth.getCurrUserEmail();
            TextView emailTv = findViewById(R.id.email);
            emailTv.setText(email);
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}