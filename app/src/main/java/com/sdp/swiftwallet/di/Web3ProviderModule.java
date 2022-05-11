package com.sdp.swiftwallet.di;

import com.sdp.swiftwallet.domain.model.object.Web3Requests;
import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt class for producer Web3Provider
 */
@Module
@InstallIn(SingletonComponent.class)
public class Web3ProviderModule {

    /**
     * Provides IWeb3Requests for injection
     */
    @Provides
    @Singleton
    public IWeb3Requests iWeb3RequestsProvider() {
        return new Web3Requests();
    }
}
