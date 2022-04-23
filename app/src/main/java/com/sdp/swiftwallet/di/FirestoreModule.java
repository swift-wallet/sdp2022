package com.sdp.swiftwallet.di;


import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.swiftwallet.common.FirebaseUtil;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FirestoreModule {

    @Provides
    public static FirebaseFirestore provideFirestore() {
        return FirebaseUtil.getFirestore();
    }

}
