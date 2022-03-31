package com.sdp.swiftwallet.UiTest.testModules;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.swiftwallet.common.FirebaseAuthModule;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
        components = ActivityComponent.class,
        replaces = FirebaseAuthModule.class
)
public class MockFirebaseAuth {
    @Provides
    public FirebaseAuth firebaseAuthProvider(){
        FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
        FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getEmail()).thenReturn("Mock email");
        return firebaseAuth;
    }
}
