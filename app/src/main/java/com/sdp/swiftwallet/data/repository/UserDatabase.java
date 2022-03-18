package com.sdp.swiftwallet.data.repository;

import android.app.Application;
import android.app.ApplicationErrorReport;
import com.sdp.swiftwallet.domain.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary class for storing users before saving on cloud
 */
public class UserDatabase extends Application {

  List<User> userDatabase;
  //Indice of the user in database
  int currentlyLogged;

  public UserDatabase(){
    userDatabase = new ArrayList<>();
  }

  public Boolean isEmpty(){
    return userDatabase.isEmpty();
  }

  public Boolean isPresent(User u){
    return userDatabase.contains(u);
  }

  /**
   * Add and update the user's currently logged indic in the database
   * @param u
   * @return
   */
  public int addAndUpdate(User u){
    if (isPresent(u)) {
      currentlyLogged = userDatabase.indexOf(u);
    } else {
      if (isEmpty()) { currentlyLogged = 0;
      } else { currentlyLogged += 1; }
      userDatabase.add(u);
    }
    return currentlyLogged;
  }

  /**
   * returns the user with index i
  */
  public User getUser(int i){
    if (i < 0 || i > userDatabase.size()) throw new IllegalArgumentException();
    return userDatabase.get(i);
  }

  /**
   * returns index of currently logged user
   */
  public int getLoggedUserIndex(){
    return currentlyLogged;
  }

}

