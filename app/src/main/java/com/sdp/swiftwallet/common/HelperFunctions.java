package com.sdp.swiftwallet.common;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class HelperFunctions {

  private static final int SHORTENED_ADDRESS_LENGTH = 4;
  private static final String SHORTENED_ADDRESS_JOINER = "...";

  //EMAIL_PATTERN matches any string, that doesnt contains special characters nor the arobase special
  //chars
  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

  //PASSWORD_PATTERN matches any string, that contains at least upper case,
  //one lower case letter and one digit, and 6 - 10 character long
  private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,10}$";

  //USERNAME_PATTERN to complete (no stupid char + more than 3 characters)
  private static final String USERNAME_PATTERN = "[a-zA-Z0-9\\._\\-]{3,}";

  /**
   * Check if an email is valid
   *
   * @param email    some email
   * @param textView Edit Text on which to take actions for regex
   * @return true if valid, false otherwise
   */
  public static Boolean checkEmail(String email, EditText textView) {

    boolean matchPattern = Pattern.compile(EMAIL_PATTERN)
        .matcher(email).matches();
    if (email.isEmpty()) {
      textView.setError("Email required");
      textView.requestFocus();
      return false;
    } else if (!matchPattern) {
      textView.setError("Your email should not use special characters and use '@' in it");
      textView.requestFocus();
      return false;
    }
    return true;
  }


  /**
   * Check if an email is valid
   *
   * @param pw password
   * @param textView Edit Text on which to take actions for regex
   * @return true if valid, false otherwise
   */
  public static Boolean checkPassword(String pw, String confirmPW, EditText textView) {

    boolean matchPattern = Pattern.compile(PASSWORD_PATTERN)
        .matcher(pw).matches();
    if (pw.isEmpty()) {
      textView.setError("Password required");
      textView.requestFocus();
      return false;
    } else if (!matchPattern) {
      textView.setError(
          "Check that your password contains at least one upper case, "
              + "one lower case letter and one digit, "
              + "and is 6 - 10 character long");
      textView.requestFocus();
      return false;
    } else if (!pw.equals(confirmPW)) {
      textView.setError("Passwords doesn't match");
      textView.requestFocus();
      return false;
    }
    return true;
  }


  /**
   * Check if an email is valid
   *
   * @param username some email
   * @param textView Edit Text on which to take actions for regex
   * @return true if valid, false otherwise
   */
  public static Boolean checkUsername(String username, EditText textView) {

    //Pattern match username
    boolean matchPattern = Pattern.compile(USERNAME_PATTERN)
        .matcher(username).matches();
    boolean isCorrect = true;

    if (username.isEmpty()) {
      textView.setError("Username required");
      textView.requestFocus();
      isCorrect = false;
    } else if (username.length() < 3) {
      textView.setError("Username is at least 3 chars");
      textView.requestFocus();
      isCorrect = false;
    } else if (username.length() > 20) {
      textView.setError("Username is at most 20 chars");
      textView.requestFocus();
      isCorrect = false;
    } else if (!matchPattern) {
      textView.setError("Username should use conventional naming and have more than 3 characters");
      textView.requestFocus();
      isCorrect = false;
    }
    return isCorrect;
  }

  /**
   * Shorten and reformat the given address
   */
  public static String toShortenedFormatAddress(String fullHexAddress) {
    int len = fullHexAddress.length();
    return fullHexAddress.substring(0, 2 + SHORTENED_ADDRESS_LENGTH) + SHORTENED_ADDRESS_JOINER +
        fullHexAddress.substring(len - SHORTENED_ADDRESS_LENGTH, len);
  }


  /**
   * Displays a basic Toast with
   *
   * @param context the target context
   * @param message message to display
   */
  public static void displayToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
}
