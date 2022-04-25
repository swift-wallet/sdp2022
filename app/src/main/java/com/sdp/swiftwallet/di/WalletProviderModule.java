package com.sdp.swiftwallet.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class WalletProviderModule {

    @Provides
    @Singleton
    public WalletProvider provideWalletProvider(@ApplicationContext Context context){
        return new WalletProvider(context);
    }
}
