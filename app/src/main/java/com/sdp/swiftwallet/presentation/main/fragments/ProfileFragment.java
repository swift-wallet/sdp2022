package com.sdp.swiftwallet.presentation.main.fragments;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;

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
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

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
                mAuth.signOut();
                ((BaseApp) getActivity().getApplication()).setCurrUser(null);
                startActivity(new Intent(getActivity(), LoginActivity.class));
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
     * @param emailField emailField
     */
    private void updateEmail(@NonNull EditText emailField){
        String email = emailField.getText().toString().trim();
        boolean check = checkEmail(email, emailField);
        if (check && mUser != null) {
            mUser.updateEmail(email).addOnSuccessListener(a -> {
                Log.d(PROFILE_TAG, "Email successfully updated \n" + email);
                displayToast(getActivity(), "Email successfully updated ! \n");
            }).addOnFailureListener(a -> {
                Log.d(PROFILE_TAG, "Something went wrong while updating the email \n" + email);
                displayToast(getActivity(), "Something went wrong while updating your email\n");
            });
        } else {
            Log.d(PROFILE_TAG, "Error: reset email without online mode \n");
            displayToast(getActivity(), "Error: reset email without online mode \n");
        }
    }

}