package com.sdp.swiftwallet.domain.model.object;

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
  //Hardcoded login methods names

  /**
   * Creates a user
   *
   * @param email       email
   * @param loginMethod login method
   */
  public User(@NonNull String uid, @NonNull String email, @NonNull LoginMethod loginMethod) {
    this.uid = uid;
    this.email = email;
  }

  @NonNull
  public String getUid() {
    return uid;
  }

  public void setUid(@NonNull String uid) {
    this.uid = uid;
  }

  //Random code to update/get info about the user
  @NonNull
  public String getEmail() {
    return email;
  }

  public void setEmail(@NonNull String email) {
    this.email = email;
  }

}

