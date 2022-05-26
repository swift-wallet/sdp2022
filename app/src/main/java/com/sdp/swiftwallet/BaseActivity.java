package com.sdp.swiftwallet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.swiftwallet.domain.model.User;


public class BaseActivity extends AppCompatActivity {
    private final String GLOBAL_USER_TAG = "GLOBAL_USER_TAG";
    private SharedPreferences sp;
    private User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sp = getSharedPreferences("UserProfile", MODE_PRIVATE);

        currUser = ((BaseApp) getApplication()).getCurrUser();
        String uid = sp.getString("uid", currUser.getUid());
        String email = sp.getString("email", currUser.getEmail());
        currUser.setUid(uid);
        currUser.setEmail(email);
        Log.d(GLOBAL_USER_TAG, "user infos retrieved");
    }

    @Override
    protected void onPause() {
        super.onPause();

        sp = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        currUser = ((BaseApp) getApplication()).getCurrUser();
        editor.putString("uid", currUser.getUid());
        editor.putString("email", currUser.getEmail());
        editor.apply();
        Log.d(GLOBAL_USER_TAG, "user infos stored");
    }
}