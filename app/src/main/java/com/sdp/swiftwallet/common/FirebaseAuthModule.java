package com.sdp.swiftwallet.common;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.swiftwallet.presentation.main.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;

@Module
@InstallIn(MainActivity.class)
public abstract class FirebaseAuthModule {
    @Binds
    public FirebaseAuth bindFirebaseAuth(){
        return FirebaseUtil.getAuth();
    }
}
