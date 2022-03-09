package com.sdp.swiftwallet.domain.model;

import android.net.Uri;
import java.util.Objects;

/**
 * Class representing a generic user
 */
public class User {

  //Some basic attributes representing a user
  private String username;
  private String email;
  private Uri profilePic;

  /**
   * Creates a user
   * @param username username
   * @param email email
   * @param profilePic profile picture
   */
  public User(String username, String email, Uri profilePic){
    Objects.requireNonNull(username);
    Objects.requireNonNull(email);
    Objects.requireNonNull(profilePic);

    this.username=username;
    this.email=email;
    this.profilePic=profilePic;
  }

  public User(String username, String email){
    Objects.requireNonNull(username);
    Objects.requireNonNull(email);

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

  public Uri getProfilePic() {
    return profilePic;
  }

  public void setEmail(String email) {
    Objects.requireNonNull(email);
    this.email = email;
  }

  public void setProfilePic(Uri profilePic) {
    Objects.requireNonNull(profilePic);
    this.profilePic = profilePic;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}




