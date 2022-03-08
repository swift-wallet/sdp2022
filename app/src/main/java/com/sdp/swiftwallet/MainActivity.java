package com.sdp.swiftwallet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="com.sdp.swift-wallet.NAME";

    private ClientAuth clientAuth;
    private GoogleSignInClient googleSignInClient;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
        clientAuth = new FirebaseAuthImpl();

        SignInButton signInBtn = findViewById(R.id.googleSignInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launch Google Sign In");
                Intent it = googleSignInClient.getSignInIntent();
                startActivityForResult(it, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google Sign In intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                clientAuth.signInWithGoogleAccount(account, MainActivity.this, new ProfileActivity(), TAG);
            } catch (Exception e) {
                Log.d(TAG, "onActivityResult: "+e.getMessage());
            }
        }
    }

    public void startQR(View view){
        Intent intent = new Intent(this, QRActivity.class);
        startActivity(intent);
    }
}