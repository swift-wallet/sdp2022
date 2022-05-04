package com.sdp.swiftwallet.domain.model;

import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.BASIC;
import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.GOOGLE;

import androidx.annotation.NonNull;
import com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod;
import java.util.Objects;

/**
 * Class representing a generic user
 */
public class User {

  @NonNull
  private String email;
  //Hardcoded login methods names

  /**
   * Creates a user
   *
   * @param email       email
   * @param loginMethod login method
   */
  public User(@NonNull String email, @NonNull LoginMethod loginMethod) {
    this.email = email;
  }

  // Random code to update/get info about the user
  public String getEmail() {
    return email;
  }

  public void setEmail(@NonNull String email) {
    this.email = email;
  }

}

