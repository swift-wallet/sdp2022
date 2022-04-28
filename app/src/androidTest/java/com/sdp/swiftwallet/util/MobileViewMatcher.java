package com.sdp.swiftwallet.util;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Root;
import org.hamcrest.Matcher;

/**
 * Container for checking for the is Toast message
 */
public class MobileViewMatcher {

  public static Matcher<Root> isToast() {
    return new ToastMatcher();
  }

  public void isToastMessageDisplayed(int textId) {
    onView(withText(textId)).inRoot(MobileViewMatcher.isToast()).check(matches(isDisplayed()));
  }
}
