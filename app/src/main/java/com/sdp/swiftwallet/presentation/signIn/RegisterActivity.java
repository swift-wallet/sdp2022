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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.databinding.ActivityRegisterBinding;
import com.sdp.swiftwallet.common.Constants;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private static final String REGISTER_TAG = "EMAIL_REGISTER_TAG";

    private ActivityRegisterBinding binding;

    @Inject SwiftAuthenticator authenticator;
    private FirebaseFirestore db;

    // Used for debugging purpose and testing
    private CountingIdlingResource mIdlingResource;

    // Useful to have as global variable
    private EditText usernameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Init db client
        db = FirebaseUtil.getFirestore();

        usernameEt = binding.registerInputUsername;
        emailEt = binding.registerInputEmail;
        passwordEt = binding.registerInputPassword;
        confirmPasswordEt = binding.registerInputConfirmPassword;

        // Init counting resource for async call in test
        mIdlingResource = new CountingIdlingResource("Register Calls");

        setListeners();
    }

    /**
     * Set all listeners from registerActivity
     */
    private void setListeners() {
        binding.registerBtn.setOnClickListener(v -> register());
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

    private boolean inputsValid(String username, String email, String password, String confirmPassword) {
        boolean isValid = true;
        
        if (!checkUsername(username, usernameEt)) isValid = false;
        if (!checkEmail(email, emailEt)) isValid = false;
        if (!checkPassword(password,
                confirmPassword,
                passwordEt)) isValid = false;

        return isValid;
    }

    /**
     * Method associated to the Register button
     * Uses the injected SwiftAuthenticator
     */
    private void register() {
        String username = usernameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();

        if (inputsValid(username, email, password, confirmPassword)) {
            loading(true);
            SwiftAuthenticator.Result registerRes = authenticator.signUp(username, email, password,
                    this::addUserToDatabase,
                    () -> handleError(SwiftAuthenticator.Result.ERROR));

            if (registerRes != SwiftAuthenticator.Result.SUCCESS) {
                handleError(registerRes);
            }
        }
    }

    /**
     * Handle error from Swift Authenticator registration
     */
    private void handleError(SwiftAuthenticator.Result result) {
        loading(false);
        // TODO: refactor to integrate helper functions for checking
        switch (result) {
            case ERROR:
                displayToast(getApplicationContext(), "User failed to register");
                break;
            default:
                Log.d(REGISTER_TAG, "Unhandeled error in " + this.getClass().getName());
                break;
        }
    }

    /**
     * Add a user to the database and go back to login activity
     */
    private void addUserToDatabase() {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .add(authenticator.getUser())
                .addOnSuccessListener(documentReference -> {
                    loading(false);

                    // User and Dev feedback
                    displayToast(getApplicationContext(), "User successfully registered");
                    Log.d(REGISTER_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

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
                    Log.w(REGISTER_TAG, "Error adding document", e);
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