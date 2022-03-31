package com.sdp.swiftwallet.presentation.main.fragments;

import static com.sdp.swiftwallet.common.HelperFunctions.checkEmail;
import static com.sdp.swiftwallet.common.HelperFunctions.checkPassword;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;

import javax.inject.Inject;

import dagger.hilt.DefineComponent;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private String PROFILE_TAG = "Profile update";

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
     * If user is logged show user infos, else return to login
     * @param view The view to display profile infos
     */
    private void checkUser(View view) {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        else {
            String email = mUser.getEmail();
            TextView emailTv = view.findViewById(R.id.email);
            emailTv.setText(email);
        }
    }

    /**
     * Update user's email
     * @param emailField emailField
     */
    private void updateEmail(@NonNull EditText emailField){
        String email = emailField.getText().toString().trim();
        boolean check = checkEmail(email, emailField);
        if (check && mUser != null){
            mUser.updateEmail(email).addOnSuccessListener( a -> {
                Log.d(PROFILE_TAG, "Email successfully updated \n"+email);
                Toast.makeText(getActivity(), "Email successfully updated !" , Toast.LENGTH_SHORT).show();
                //Start again login activity if successful
            }).addOnFailureListener( a -> {
                Log.d(PROFILE_TAG, "Something went wrong while updating the email \n"+email);
                Toast.makeText(getActivity(), "Something went wrong while updating your email ", Toast.LENGTH_SHORT).show();
            });
        }
    }

}