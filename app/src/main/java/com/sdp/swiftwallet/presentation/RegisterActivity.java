package com.sdp.swiftwallet.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

import org.web3j.abi.datatypes.Bool;

public class RegisterActivity extends AppCompatActivity {

    private ClientAuth clientAuth;
    private EditText registerUsernameEt, registerEmailEt, registerPasswordEt;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clientAuth = new FirebaseAuthImpl();

        registerUsernameEt = findViewById(R.id.registerUsernameEt);
        registerEmailEt = findViewById(R.id.registerEmailEt);
        registerPasswordEt = findViewById(R.id.registerPasswordEt);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> registerUser());
    }

    /**
     * Registers the user
     */
    private void registerUser() {
        String username = registerUsernameEt.getText().toString().trim();
        String email = registerEmailEt.getText().toString().trim();
        String password = registerPasswordEt.getText().toString().trim();

        if (!isUserValid(username)) return;
        if (!isEmailValid(email)) return;
        if (!isPasswordValid(password)) return;

        clientAuth.createUserWithEmailAndPassword(username, email, password, RegisterActivity.this, new LoginActivity());
    }

    /**
     * Check if the username is correctly formatted
     */
    private Boolean isUserValid(String username) {
        if (username.isEmpty()) {
            registerUsernameEt.setError("Username required");
            registerUsernameEt.requestFocus();
            return false;
        }
        if (username.length() < 3) {
            registerUsernameEt.setError("Username is at least 3 chars");
            registerUsernameEt.requestFocus();
            return false;
        }
        if (username.length() > 20) {
            registerUsernameEt.setError("Username is at most 20 chars");
            registerUsernameEt.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Check if email is correctly formatted
     */
    private Boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            registerEmailEt.setError("Email required");
            registerEmailEt.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Check if the password is correctly formatted
     */
    private Boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            registerPasswordEt.setError("Password required");
            registerPasswordEt.requestFocus();
            return false;
        }
        return true;
    }


}