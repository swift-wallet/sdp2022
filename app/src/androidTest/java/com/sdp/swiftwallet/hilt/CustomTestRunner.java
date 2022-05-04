//package com.sdp.swiftwallet.hilt;
//
//import android.app.Application;
//import android.content.Context;
//import androidx.test.runner.AndroidJUnitRunner;
//import com.sdp.swiftwallet.SwiftWalletApp;
//import dagger.hilt.android.testing.CustomTestApplication;
//import dagger.hilt.android.testing.HiltTestApplication;
//
//// A custom runner to set up the instrumented application class for tests.
//public final class CustomTestRunner extends AndroidJUnitRunner {
//
//    @Override
//    public Application newApplication(ClassLoader cl, String className, Context context)
//            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        return super.newApplication(cl, HiltTestApplication.class.getName(), context);
//    }
//}
