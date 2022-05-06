package com.sdp.swiftwallet.hilt;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

// A custom runner to set up the instrumented application class for tests.
public final class CustomTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // Uses HiltTestApplication_Application which is generated at build time (normal if red)
        return super.newApplication(cl, HiltTestApplication_Application.class.getName(), context);
    }
}
