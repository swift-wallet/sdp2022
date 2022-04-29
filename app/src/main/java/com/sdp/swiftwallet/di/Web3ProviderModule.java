package com.sdp.swiftwallet.di;

import com.sdp.swiftwallet.data.repository.Web3Requests;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class Web3ProviderModule {
    @Provides
    @Singleton
    public IWeb3Requests iWeb3RequestsProvider() {
        return new Web3Requests();
    }
}
