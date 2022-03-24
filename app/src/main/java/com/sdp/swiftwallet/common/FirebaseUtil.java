package com.sdp.swiftwallet.common;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.BuildConfig;

public class FirebaseUtil {
    /** Use emulators only in debug builds **/
    private static final boolean sUseEmulators = false;
    private static final int FIREBASE_AUTH_EMULATOR_HOST = 9099;
    private static final int FIREBASE_FIRESTORE_EMULATOR_HOST = 8080;

    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;

    public static FirebaseFirestore getFirestore() {
        if (FIRESTORE == null) {
            FIRESTORE = FirebaseFirestore.getInstance();

            // Connect to the Cloud Firestore emulator when appropriate. The host '10.0.2.2' is a
            // special IP address to let the Android emulator connect to 'localhost'.
            if (sUseEmulators) {
                FIRESTORE.useEmulator("10.0.2.2", FIREBASE_FIRESTORE_EMULATOR_HOST);
            }
        }

        return FIRESTORE;
    }

    public static FirebaseAuth getAuth() {
        if (AUTH == null) {
            AUTH = FirebaseAuth.getInstance();

            // Connect to the Firebase Auth emulator when appropriate. The host '10.0.2.2' is a
            // special IP address to let the Android emulator connect to 'localhost'.
            if (sUseEmulators) {
                AUTH.useEmulator("10.0.2.2", FIREBASE_AUTH_EMULATOR_HOST);
            }
        }

        return AUTH;
    }

}
