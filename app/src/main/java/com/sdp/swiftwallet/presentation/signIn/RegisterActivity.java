package com.sdp.swiftwallet.presentation.signIn;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.checkPassword;
import static com.sdp.swiftwallet.common.HelperFunctions.checkUsername;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

        // Init auth client and db client
        mAuth = FirebaseUtil.getAuth();
        db = FirebaseUtil.getFirestore();

        // Find EditText view and registerBtn
        registerUsernameEt = findViewById(R.id.registerUsernameEt);
        registerEmailEt = findViewById(R.id.registerEmailEt);
        registerPasswordEt = findViewById(R.id.registerPasswordEt);
        Button registerBtn = findViewById(R.id.registerBtn);


        Button goBack = findViewById(R.id.goBackRegister);
        goBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // Set registerBtn listener
        registerBtn.setOnClickListener(v -> registerUser());
    }

    /**
     * Check for registration infos validity and create user on client auth
     */
    private void registerUser() {
        String username = registerUsernameEt.getText().toString().trim();
        String email = registerEmailEt.getText().toString().trim();
        String password = registerPasswordEt.getText().toString().trim();

        //Check validity of inputs
        if (!checkUsername(username, registerUsernameEt)) return;
        // TODO: add a username database
        if (!checkEmail(email, registerEmailEt)) return;
        if (!checkPassword(password, registerPasswordEt)) return;

        createUserWithEmailAndPassword(username, email, password);
    }


    /**
     * create user with username, email and password with client auth
     * @param username user username
     * @param email user email
     * @param password user password
     */
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

    /**
     * Add a user to the database and go back to login activity
     * @param user the user to add to the database
     */
    private void registerUserToDatabase(User user) {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegisterActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                        Log.d(EMAIL_REGISTER_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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