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
import androidx.test.espresso.idling.CountingIdlingResource;

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

    // Used for debugging purpose
    private CountingIdlingResource mIdlingResource;

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

        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("Register Calls");

        // Init listeners
        Button goBack = findViewById(R.id.goBackRegister);
        goBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerBtn.setOnClickListener(v -> registerUser());
    }

    /**
     * Check for registration infos validity and create user on client auth
     */
    private void registerUser() {
        String username = registerUsernameEt.getText().toString().trim();
        String email = registerEmailEt.getText().toString().trim();
        String password = registerPasswordEt.getText().toString().trim();

        // Check validity of inputs
        if (!checkUsername(username, registerUsernameEt)) return;
        if (!checkEmail(email, registerEmailEt)) return;
        if (!checkPassword(password, registerPasswordEt)) return;

        // Increment counter before creating user
        mIdlingResource.increment();
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
                            // If user successfuly created the account,
                            // Increment before adding to db and decrement because user is created
                            mIdlingResource.increment();
                            registerUserToDatabase(user);
                            mIdlingResource.decrement();
                        }
                        else {
                            // If user failed to create an account, decrement counter
                            mIdlingResource.decrement();
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

                        // Decrement counter if user successfully added to db
                        mIdlingResource.decrement();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Decrement counter if user failed to add to db
                        mIdlingResource.decrement();
                        Toast.makeText(RegisterActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                        Log.w(EMAIL_REGISTER_TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * Getter for the idling resource (used only in testCase normally)
     * @return the idling resource used by RegisterActivity
     */
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

}