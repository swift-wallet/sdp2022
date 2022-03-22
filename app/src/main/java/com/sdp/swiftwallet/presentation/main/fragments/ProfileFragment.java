package com.sdp.swiftwallet.presentation.main.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View.OnClickListener;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.ClientAuth;
import java.util.Objects;
import com.sdp.swiftwallet.data.repository.UserDatabase;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View is accessible from this moment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logoutButton = view.findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
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
            getActivity().finish();
        }
        else {
            String email = mAuth.getCurrentUser().getEmail();
            TextView emailTv = view.findViewById(R.id.email);
            emailTv.setText(email);
        }
    }


}