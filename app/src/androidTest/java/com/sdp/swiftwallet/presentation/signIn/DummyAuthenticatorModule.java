package com.sdp.swiftwallet.presentation.signIn;

import com.sdp.swiftwallet.di.AuthenticatorModule;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

/**
 * Hilt Module to replace SwiftAuthenticator
 */
@Module
@TestInstallIn(
        components = SingletonComponent.class,
        replaces = AuthenticatorModule.class
)
public class DummyAuthenticatorModule {

    @Singleton
    @Provides
    public static SwiftAuthenticator provideDummyAuthenticator() {
        return new DummyAuthenticator();
    }

}
