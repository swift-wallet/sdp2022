package com.sdp.swiftwallet.presentation.signIn;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.checkPassword;
import static com.sdp.swiftwallet.common.HelperFunctions.checkUsername;
import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;
import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.BASIC;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.databinding.ActivityRegisterBinding;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String EMAIL_REGISTER_TAG = "EMAIL_REGISTER_TAG";

    private ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Used for debugging purpose and testing
    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Init auth client and db client
        mAuth = FirebaseUtil.getAuth();
        db = FirebaseUtil.getFirestore();

        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("Register Calls");

        setListeners();
    }

    /**
     * Set all listeners from registerActivity
     */
    private void setListeners() {
        binding.registerBtn.setOnClickListener(v -> registerUser());
    }

    /**
     * Enable loading icon for register button
     * @param isLoading flag to enable or disable loading
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.registerBtn.setVisibility(View.INVISIBLE);
            binding.registerBtnProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.registerBtnProgressBar.setVisibility(View.INVISIBLE);
            binding.registerBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Check for registration info validity and create user on client auth
     */
    private void registerUser() {
        String username = binding.registerInputUsername.getText().toString().trim();
        String email = binding.registerInputEmail.getText().toString().trim();
        String password = binding.registerInputPassword.getText().toString().trim();
        String confirmPassword = binding.registerInputConfirmPassword.getText().toString().trim();

        // Check validity of inputs
        if (!checkUsername(username, binding.registerInputUsername)) return;
        if (!checkEmail(email, binding.registerInputEmail)) return;
        if (!checkPassword(password, confirmPassword, binding.registerInputPassword)) return;

        // Increment counter before creating user
        mIdlingResource.increment();
        loading(true);
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
                            User user = new User(username, BASIC);

                            // Dev feedback only (intermediate step)
                            Log.d(EMAIL_REGISTER_TAG, "onComplete: User created for Auth");

                            // Increment before database step begins; decrement to end auth step
                            mIdlingResource.increment();
                            registerUserToDatabase(user);
                            mIdlingResource.decrement();
                        }
                        else {
                            // If user failed to create an account, decrement counter
                            mIdlingResource.decrement();

                            // User and Dev feedback
                            displayToast(getApplicationContext(), "User failed to register");
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
        db.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);

                    // User and Dev feedback
                    displayToast(getApplicationContext(), "User successfully registered");
                    Log.d(EMAIL_REGISTER_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    // Decrement counter if user successfully added to db
                    mIdlingResource.decrement();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                })
                .addOnFailureListener(e -> {
                    loading(false);
                    // Decrement counter if user failed to add to db
                    mIdlingResource.decrement();

                    // User and Dev feedback
                    displayToast(getApplicationContext(), "User failed to register");
                    Log.w(EMAIL_REGISTER_TAG, "Error adding document", e);
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