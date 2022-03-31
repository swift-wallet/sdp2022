package com.sdp.swiftwallet.presentation.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;

import javax.inject.Inject;

import dagger.hilt.DefineComponent;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    @Inject
    protected FirebaseAuth mAuth;

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
     * If user is logged show user infos
     * @param view The view to display profile infos
     */
    private void checkUser(View view) {
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            TextView emailTv = view.findViewById(R.id.email);
            emailTv.setText(email);
        }
    }


}