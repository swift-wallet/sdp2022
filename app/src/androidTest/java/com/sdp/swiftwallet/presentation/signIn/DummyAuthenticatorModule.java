package com.sdp.swiftwallet.presentation.signIn;

import com.sdp.swiftwallet.di.AuthenticatorModule;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator;

import dagger.Module;
import dagger.Provides;
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

    @Provides
    public static SwiftAuthenticator provideDummyAuthenticator() {
        return DummyAuthenticator.INSTANCE;
    }

}
