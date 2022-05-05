package com.sdp.swiftwallet.hilt;

import com.sdp.swiftwallet.BaseApp;

import dagger.hilt.android.testing.CustomTestApplication;

/**
 * Required to use SwiftWalletApp in tests
 * See: https://stackoverflow.com/questions/68850381/android-tests-failing-when-using-hilt-caused-by-classcastexception
 */
@CustomTestApplication(BaseApp.class)
public interface HiltTestApplication { }
