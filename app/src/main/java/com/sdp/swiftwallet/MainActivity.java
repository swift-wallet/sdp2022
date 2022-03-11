package com.sdp.swiftwallet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private ActivityResultLauncher<Intent> googleSignInActivityResultLauncher;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init client authentication and launcher for google signIn
        clientAuth = new FirebaseAuthImpl();
        initGoogleSignInResultLauncher();

        SignInButton googleSignInBtn = findViewById(R.id.googleSignInBtn);
        googleSignInBtn.setOnClickListener(v -> startGoogleSignIn());
    }

    /**
     * Init google SignIn launcher,
     * Perform signIn through clientAuth by giving this activity and the result activity
     */
    private void initGoogleSignInResultLauncher() {
        googleSignInActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "onActivityResult: Google signIn intent result");
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                            clientAuth.signInWithGoogleAccount(account, MainActivity.this, new ProfileActivity(), TAG);
                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: " + e.getMessage());
                        }
                    }
                });
    }

    /**
     * Setup GoogleSignInClient and launch google signIn with intent
     */
    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        Log.d(TAG, "onClick: launch Google signIn");
        Intent it = googleSignInClient.getSignInIntent();
        googleSignInActivityResultLauncher.launch(it);
    }

    public void startQR(View view){
        Intent intent = new Intent(this, QRActivity.class);
        startActivity(intent);
    }

    public void startLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startCryptoValues(View view){
        Intent intent = new Intent(this, CryptoValuesActivity.class);
        startActivity(intent);
    }
}