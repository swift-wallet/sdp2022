package com.sdp.swiftwallet.common;
import com.google.firebase.auth.FirebaseAuth;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FirebaseAuthModule {
    @Provides
    public FirebaseAuth firebaseAuthProvider(){
        return FirebaseUtil.getAuth();
    }
}
