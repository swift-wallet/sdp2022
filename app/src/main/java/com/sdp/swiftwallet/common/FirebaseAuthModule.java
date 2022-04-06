package com.sdp.swiftwallet.common;
import com.google.firebase.auth.FirebaseAuth;


import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class FirebaseAuthModule {

    /**
     * For hilt
     */
    @Inject
    public FirebaseAuthModule(){ }

    /**
     * Provides an instance of the FirebaseAuth, @singleton avoid creating multiple times the mock function
     * @return
     */
    @Singleton
    @Provides
    public static FirebaseAuth firebaseAuthProvider(){
        return FirebaseUtil.getAuth();
    }
}
