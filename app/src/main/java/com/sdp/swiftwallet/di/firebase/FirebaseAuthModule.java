package com.sdp.swiftwallet.di.firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.swiftwallet.common.FirebaseUtil;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;

/**
 * Authentication module for hilt
 */
@Module
@InstallIn(SingletonComponent.class)
public class FirebaseAuthModule {

    /**
     * @return firebase auth provider for authentication
     */
    @Provides
    public FirebaseAuth firebaseAuthProvider(){
        return FirebaseUtil.getAuth();
    }
}

