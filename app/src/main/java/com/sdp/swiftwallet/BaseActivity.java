package com.sdp.swiftwallet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.swiftwallet.common.FirebaseUtil;
import com.sdp.swiftwallet.domain.model.User;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class BaseActivity extends AppCompatActivity {
    private final String GLOBAL_USER_TAG = "GLOBAL_USER_TAG";
    private SharedPreferences sp;
    private User currUser;

    @Inject
    SwiftAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currUser == null && authenticator.getUser() != null) {
            sp = getSharedPreferences("UserProfile", MODE_PRIVATE);

            String uid = sp.getString("uid", "");
            String email = sp.getString("email", "");
            User user = new User(uid, email);

            ((BaseApp) getApplication()).setCurrUser(user);
            Log.d(GLOBAL_USER_TAG, "user infos retrieved");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        currUser = ((BaseApp) getApplication()).getCurrUser();
        if (currUser != null) {
            sp = getSharedPreferences("UserProfile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("uid", currUser.getUid());
            editor.putString("email", currUser.getEmail());
            editor.apply();
            Log.d(GLOBAL_USER_TAG, "user infos stored");
        }
    }

}