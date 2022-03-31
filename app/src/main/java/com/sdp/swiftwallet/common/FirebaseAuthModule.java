package com.sdp.swiftwallet.common;
import com.google.firebase.auth.FirebaseAuth;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class FirebaseAuthModule {
    @Provides
    public FirebaseAuth firebaseAuthProvider(){
        return FirebaseUtil.getAuth();
    }
}
