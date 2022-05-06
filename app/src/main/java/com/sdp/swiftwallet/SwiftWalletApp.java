package com.sdp.swiftwallet;

import dagger.hilt.android.HiltAndroidApp;

/**
 * This class is only required for the @HiltAndroidApp tag to make injection work
 * If you want to add global attributes/methods for the app, see BaseApp.
 *
 * The tag cannot be simply put in BaseApp as it would break hilt,
 * See: https://stackoverflow.com/questions/68850381/android-tests-failing-when-using-hilt-caused-by-classcastexception
 * See: https://github.com/google/dagger/issues/2033
 */
@HiltAndroidApp
public class SwiftWalletApp extends BaseApp { }
