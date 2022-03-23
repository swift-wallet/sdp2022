package com.sdp.swiftwallet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.sdp.swiftwallet.presentation.RegisterActivity;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.sdp.swiftwallet.LOGIN";
    private static final String WELCOME_MESSAGE = "Welcome to SwiftWallet!";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    private TextView attemptsTextView;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    private ClientAuth clientAuth;
    private ActivityResultLauncher<Intent> googleSignInActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attemptsTextView = (TextView) findViewById(R.id.attemptsMessage);
        attemptsTextView.setText("");

        //Init client authentication and launcher for google signIn
        clientAuth = new FirebaseAuthImpl();
        clientAuth.setLanguage("en", "en_gb");

        initGoogleSignInResultLauncher();

        //Sets up google sign in button
        SignInButton googleSignInBtn = findViewById(R.id.googleSignInBtn);
        googleSignInBtn.setOnClickListener(v -> startGoogleSignIn());

        //Sets up register user option
        TextView registerTv = findViewById(R.id.register);
        registerTv.setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class))
        );

        //Sets up the forgot password recover option for users
        TextView forgoPwTv = findViewById(R.id.forgotPassword);
        forgoPwTv.setOnClickListener(v ->
            startActivity(new Intent(this, ForgotPasswordActivity.class))
        );

        //Sets up sign in button
        Button signInButton = findViewById(R.id.loginButton);
        signInButton.setOnClickListener(v->
            login(v));
    }

    /**
     * Login method which is launched by the LOGIN button on the login screen
     * @param view current View of the user
     */
    public void login(View view) {
        //Retrieve username and password from login screen
        EditText editText = (EditText) findViewById(R.id.loginUsername);
        String username = editText.getText().toString();
        editText = (EditText) findViewById(R.id.loginPassword);
        String password = editText.getText().toString();

        clientAuth
            .signInWithEmailAndPassword(username, password, LoginActivity.this, new MainActivity());

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
                            clientAuth.signInWithGoogleAccount(account, LoginActivity.this, new MainActivity());
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
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        Log.d(TAG, "onClick: launch Google signIn");
        Intent it = googleSignInClient.getSignInIntent();
        googleSignInActivityResultLauncher.launch(it);
    }


}