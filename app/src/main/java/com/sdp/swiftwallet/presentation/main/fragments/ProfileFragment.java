package com.sdp.swiftwallet.presentation.main.fragments;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.displayToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.BaseApp;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Represents the profile menu fragment and view
 */
@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private final static String EMAIL_UPDATE_TAG = "PROFILE_EMAIL_UPDATE_TAG";

    @Inject
    SwiftAuthenticator authenticator;

    private EditText emailView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View is accessible from this moment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Sets up EditText fields
        emailView = view.findViewById(R.id.update_emailEt);

        setListeners(view);
        displayUserEmail(view);

        return view;
    }

    /**
     * Set all listeners for Profile Fragment
     * @param view the fragment view
     */
    private void setListeners(View view) {
        //Click to logout
        Button logoutButton = view.findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(v -> {
            authenticator.signOut();
            ((BaseApp) getActivity().getApplication()).setCurrUser(null);
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        //Click to update email
        Button emailUpdateButton = view.findViewById(R.id.update_email_btn);
        emailUpdateButton.setOnClickListener(v -> checkAndUpdate(emailView.getText().toString().trim()));
    }

    /**
     * If user is logged in show user email
     * @param view the view to display profile infos
     */
    private void displayUserEmail(View view) {
        User currUser = authenticator.getUser();
        if (currUser != null) {
            String email = currUser.getEmail();
            TextView emailTv = view.findViewById(R.id.profile_userEmailTv);
            emailTv.setText(email);
        }
    }

    /**
     * If email is valid start email update
     * @param email new user email
     */
    private void checkAndUpdate(@NotNull String email){
        if (checkEmail(email, emailView)) {
            updateEmail(email);
        }
    }

    /**
     * Update user email
     * @param email new user email
     */
    private void updateEmail(String email){
        Log.d(EMAIL_UPDATE_TAG, "start email update with email: " + email);
        SwiftAuthenticator.Result res = authenticator.updateUserEmail(email,
                this::sendSuccess,
                this::sendFailure);

        if (res != SwiftAuthenticator.Result.SUCCESS) {
            sendFailure();
        }
    }

    /**
     * Callback for successful email updating
     */
    private void sendSuccess() {
        displayToast(getActivity(), "Email successfully updated!");
    }

    /**
     * Callback for failed email updating
     */
    private void sendFailure() {
        emailView.setError("Email failed updating");
        emailView.requestFocus();
    }

}