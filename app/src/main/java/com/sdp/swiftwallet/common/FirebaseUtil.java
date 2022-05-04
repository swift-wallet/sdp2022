package com.sdp.swiftwallet.common;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Firebase authentication and Firestore related static methods
 */
public class FirebaseUtil {

    // Use emulators only in debug builds
    private static final boolean sUseEmulators = false;
    private static final int FIREBASE_AUTH_EMULATOR_HOST = 9099;
    private static final int FIREBASE_FIRESTORE_EMULATOR_HOST = 8080;
    // Special android domain for emulator to connect to the localhost
    private static final String ANDROID_HOST =  "10.0.2.2";

    // Firebase Auth  and Firestore db
    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;

    /**
     * Gets the current used firestore
     * @return instance of Firestore if non null
     */
    public static FirebaseFirestore getFirestore() {
        if (FIRESTORE == null) {
            FIRESTORE = FirebaseFirestore.getInstance();
            if (sUseEmulators) {
                FIRESTORE.useEmulator(ANDROID_HOST, FIREBASE_FIRESTORE_EMULATOR_HOST);
            }
        }
        return FIRESTORE;
    }

    /**
     * Gets the current Firebase Authentication object
     * @return the current Firebase Authentication if non null
     */
    public static FirebaseAuth getAuth() {
        if (AUTH == null) {
            AUTH = FirebaseAuth.getInstance();
            if (sUseEmulators) {
                AUTH.useEmulator(ANDROID_HOST, FIREBASE_AUTH_EMULATOR_HOST);
            }
        }
        return AUTH;
    }

}
