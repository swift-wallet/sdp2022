package com.sdp.swiftwallet.presentation.main.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.presentation.signIn.LoginActivity;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseUtil.getAuth();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View is accessible from this moment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logoutButton = view.findViewById(R.id.logoutBtn);
        Button editButton = view.findViewById(R.id.editBtn);
        logoutButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        editButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (view.findViewById(R.id.langauge).getVisibility() == View.VISIBLE) {
                    editButton.setText("SAVE");

                    view.findViewById(R.id.langauge).setVisibility(View.GONE);
                    view.findViewById(R.id.langaugeEdit).setVisibility(View.VISIBLE);
                    ((EditText)view.findViewById(R.id.langaugeEdit)).setText(((TextView)view.findViewById(R.id.langauge)).getText());
                    view.findViewById(R.id.username).setVisibility(View.GONE);
                    view.findViewById(R.id.usernameEdit).setVisibility(View.VISIBLE);
                    ((EditText)view.findViewById(R.id.usernameEdit)).setText(((TextView)view.findViewById(R.id.username)).getText());
                } else {
                    editButton.setText("EDIT");

                    view.findViewById(R.id.langauge).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.langaugeEdit).setVisibility(View.GONE);
                    view.findViewById(R.id.username).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.usernameEdit).setVisibility(View.GONE);

                    Map<String, Object> data = new HashMap<>();
                    data.put("Language", ((EditText)view.findViewById(R.id.langaugeEdit)).getText().toString());
                    data.put("UserName", ((EditText)view.findViewById(R.id.usernameEdit)).getText().toString());
                    UpdateProfile(data);
                    displayData(view);
                }
            }
        });


        checkUser(view);
        InitUser();
        displayData(view);
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
            String email = mAuth.getCurrentUser().getEmail();
            TextView emailTv = view.findViewById(R.id.email);
            emailTv.setText(email);
        }
        Log.d(TAG, mAuth.getCurrentUser().getEmail() + " => " + mAuth.getCurrentUser().getUid());
    }

    private void InitUser() {
        Map<String, Object> data = new HashMap<>();
        data.put("Email", mAuth.getCurrentUser().getEmail());
        data.put("UserName", mAuth.getCurrentUser().getDisplayName());
        data.put("Language", "English");

        UpdateProfile(data);
    }

    private void UpdateProfile(Map<String, Object> data) {
        db.collection("user_profile").document(mAuth.getCurrentUser().getUid())
                .set(data, SetOptions.merge());
    }

    private void displayData(View view) {
        Task<DocumentSnapshot> users = db.collection("user_profile")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                TextView Language = view.findViewById(R.id.langauge);
                                Language.setText((String)document.getData().get("Language"));
                                TextView UserName = view.findViewById(R.id.username);
                                UserName.setText((String)document.getData().get("UserName"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}