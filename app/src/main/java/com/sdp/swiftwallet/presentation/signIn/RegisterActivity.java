package com.sdp.swiftwallet.presentation.signIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String EMAIL_REGISTER_TAG = "EMAIL_REGISTER_TAG";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText registerUsernameEt, registerEmailEt, registerPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseUtil.getAuth();
        db = FirebaseUtil.getFirestore();

        registerUsernameEt = findViewById(R.id.registerUsernameEt);
        registerEmailEt = findViewById(R.id.registerEmailEt);
        registerPasswordEt = findViewById(R.id.registerPasswordEt);
        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = registerUsernameEt.getText().toString().trim();
        String email = registerEmailEt.getText().toString().trim();
        String password = registerPasswordEt.getText().toString().trim();

        if (!isUserValid(username)) return;
        if (!isEmailValid(email)) return;
        if (!isPasswordValid(password)) return;

        createUserWithEmailAndPassword(username, email, password);
    }

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

    private Boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            registerEmailEt.setError("Email required");
            registerEmailEt.requestFocus();
            return false;
        }

        return true;
    }

    private Boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            registerPasswordEt.setError("Password required");
            registerPasswordEt.requestFocus();
            return false;
        }

        return true;
    }

    private void createUserWithEmailAndPassword(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, email, "BASIC");
                            Log.d(EMAIL_REGISTER_TAG, "onComplete: User created for Auth");
                            registerUserToDatabase(user);
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                            Log.w(EMAIL_REGISTER_TAG, "Error from task", task.getException());
                        }
                    }
                });
    }

    private void registerUserToDatabase(User user) {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegisterActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                        Log.d(EMAIL_REGISTER_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        RegisterActivity.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                        Log.w(EMAIL_REGISTER_TAG, "Error adding document", e);
                    }
                });
    }

}