package com.sdp.swiftwallet.data.repository;

import com.sdp.swiftwallet.domain.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary class for storing users before saving on cloud
 */
public class UserDatabase {

  List<User> userDatabase;

  public UserDatabase(){
    userDatabase = new ArrayList<>();
  }

  public void  addUse(){};
}
