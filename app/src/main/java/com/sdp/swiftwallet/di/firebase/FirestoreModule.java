package com.sdp.swiftwallet.di.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.swiftwallet.common.FirebaseUtil;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Firestore module for hilt
 */
@Module
@InstallIn(SingletonComponent.class)
public class FirestoreModule {

    /**
     * @return FirebaseFirestore instance
     */
    @Provides
    public static FirebaseFirestore provideFirestore() {
        return FirebaseUtil.getFirestore();
    }

}
