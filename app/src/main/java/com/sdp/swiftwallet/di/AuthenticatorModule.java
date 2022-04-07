package com.sdp.swiftwallet.di;

import com.sdp.swiftwallet.domain.repository.AuthenticatorFirebaseImpl;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class AuthenticatorModule {

    @FirebaseAuthenticator
    @Provides
    public static SwiftAuthenticator provideFirebaseAuthenticator() {
        //TODO
        return new AuthenticatorFirebaseImpl();
    }

    @GoogleAuthenticator
    @Provides
    public static SwiftAuthenticator provideGoogleAuthenticator() {
        //TODO
        return new AuthenticatorFirebaseImpl();
    }

}
