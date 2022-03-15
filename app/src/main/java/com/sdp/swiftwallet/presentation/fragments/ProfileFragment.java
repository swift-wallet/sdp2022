package com.sdp.swiftwallet.presentation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.LoginActivity;
import com.sdp.swiftwallet.MainActivity;
import com.sdp.swiftwallet.data.repository.FirebaseAuthImpl;
import com.sdp.swiftwallet.domain.repository.ClientAuth;

public class ProfileFragment extends Fragment {

    private ClientAuth clientAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Checks if a user is logged
     */
    private void checkUser(View view) {
        if (!clientAuth.currUserIsChecked()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        else {
            String email = clientAuth.getCurrUserEmail();
            Activity act = getActivity();
            TextView emailTv = view.findViewById(R.id.email);
            emailTv.setText(email);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View is accessible from this moment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // setup fragment screen as soon as view is ready
        clientAuth = new FirebaseAuthImpl();
        checkUser(view);

        Button logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            clientAuth.signOut();
            checkUser(view);
        });

        return view;
    }
}