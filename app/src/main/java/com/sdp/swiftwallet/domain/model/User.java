package com.sdp.swiftwallet.domain.model;

import androidx.annotation.NonNull;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator.LoginMethod;

/**
 * Class representing a generic user
 */
public class User {

  @NonNull
  private String uid;
  @NonNull
  private String email;

  /**
   * Creates a user
   *
   * @param uid user uid
   * @param email user email
   */
  public User(@NonNull String uid, @NonNull String email) {
    this.uid = uid;
    this.email = email;
  }

  /**
   * Getter for user uid
   * @return user uid
   */
  @NonNull
  public String getUid() {
    return uid;
  }

  /**
   * Setter for user uid
   * @param uid user uid
   */
  public void setUid(@NonNull String uid) {
    this.uid = uid;
  }

  /**
   * Getter for user email
   * @return user email
   */
  @NonNull
  public String getEmail() {
    return email;
  }

  /**
   * Setter for user email
   * @param email user email
   */
  public void setEmail(@NonNull String email) {
    this.email = email;
  }

}

