package com.sdp.swiftwallet.di;

import android.content.Context;

import com.sdp.swiftwallet.domain.repository.AuthenticatorFirebaseImpl;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt Module to inject SwiftAuthenticator
 */
@Module
@InstallIn(SingletonComponent.class)
public class AuthenticatorModule {

    @Provides
    public static SwiftAuthenticator provideFirebaseAuthenticator(@ApplicationContext Context context) {
        return new AuthenticatorFirebaseImpl(context);
    }

}
