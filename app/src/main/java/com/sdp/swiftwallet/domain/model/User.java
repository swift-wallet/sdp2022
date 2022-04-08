package com.sdp.swiftwallet.domain.model;


import dagger.hilt.android.scopes.FragmentScoped;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Class representing a generic user
 */

public class User {

  //Some basic attributes representing a user
  private String username;
  private String email;
  //Hardcoded login methods names
  private final String loginMethods;


  /**
   * Creates a user
   * @param username
   * @param email
   * @param loginMethod
   */
  public User(String username, String email, String loginMethod){
    Objects.requireNonNull(username);
    Objects.requireNonNull(email);
    Objects.requireNonNull(loginMethod);

    if (!loginMethod.equals("GOOGLE") && !loginMethod.equals("BASIC")) {
      throw new IllegalArgumentException();
    }

    this.loginMethods=loginMethod;
    this.username=username;
    this.email=email;

  }

  //Random code to update/get info about the user
  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getLoginMethod() {
    return loginMethods;
  }


  public void setEmail(String email) {
    Objects.requireNonNull(email);
    this.email = email;
  }

  public void modifyUsername(String username) {
    this.username = username;
  }
}

