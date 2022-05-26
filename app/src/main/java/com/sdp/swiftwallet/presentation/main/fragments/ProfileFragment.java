package com.sdp.swiftwallet.presentation.main.fragments;

import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;
import static com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator.Result.ERROR_NOT_ONLINE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.common.HelperFunctions;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator.Result;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

/**
 * Represents the profile menu fragment and view
 */
@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private String PROFILE_TAG = "Profile update";

    // To refactor
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText email;

    @Inject
    SwiftAuthenticator authenticator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseUtil.getAuth();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View is accessible from this moment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Sets up EditText fields
        email = view.findViewById(R.id.reset_email_field);

        //Click to logout
        Button logoutButton = view.findViewById(R.id.logout_Btn);
        logoutButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                authenticator.signOut(() -> signOutHandler(), () ->
                {
                    Log.d("Profile fragment:","Fail to log out the user");
                    HelperFunctions.displayToast(getActivity(),"An unexpected error happenned" +
                            "while trying to log out, please try again");
                });
            }
        });

        //Click to update email
        Button emailUpdateButton = view.findViewById(R.id.reset_email_Btn);
        emailUpdateButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                updateEmail(email);
            }
        });

        checkUser(view);
        return view;
    }

    /**
     * If user is logged show user infos
     * @param view The view to display profile infos
     */
    private void checkUser(View view) {
        if (mAuth.getCurrentUser() != null) {
            String email = mUser.getEmail();
            TextView emailTv = view.findViewById(R.id.email);
            emailTv.setText(email);
            // TODO: redirect in the future to login if not logged out
        }
    }

    /**
     * Update user's email
     *
     * @param emailField emailField
     */
    private void updateEmail(@NonNull EditText emailField) {
        String email = emailField.getText().toString().trim();
        Result result = authenticator.updateEmail(email, emailField,
            () -> {
                Log.d(PROFILE_TAG, "Email successfully updated \n" + email);
                displayToast(getActivity(), "Email successfully updated ! \n");
            },
            () -> updateEmailErrorHandler(Result.ERROR));
        if (result != Result.SUCCESS) {
            updateEmailErrorHandler(result);
        }
    }

    /**
     * Simple handler for error handling while updating the email
     *
     * @param error error to handle
     */
    private void updateEmailErrorHandler(Result error) {
        if (error == Result.ERROR) {
            Log.d(PROFILE_TAG, "Something went wrong while updating the email \n" + email);
            displayToast(getActivity(), "Something went wrong while updating your email\n");
        } else if (error == ERROR_NOT_ONLINE) {
            Log.d(PROFILE_TAG, "Error: reset email without online mode \n");
            displayToast(getActivity(), "Error: reset email without online mode \n");
        }
    }


    /**
     * Handles the sign out process, action to take after success
     */
    private void signOutHandler() {
        ((BaseApp) getActivity().getApplication()).setCurrUser(null);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}