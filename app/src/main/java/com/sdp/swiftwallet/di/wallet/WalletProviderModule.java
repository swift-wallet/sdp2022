package com.sdp.swiftwallet.di.wallet;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Wallet module for hilt
 */
@Module
@InstallIn(SingletonComponent.class)
public class WalletProviderModule {

    /**
     * Provides wallet for Hilt injection
     */
    @Provides
    @Singleton
    public WalletProvider provideWalletProvider(@ApplicationContext Context context){
        return new WalletProvider(context);
    }
}
