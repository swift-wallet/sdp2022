package com.sdp.swiftwallet.JavaTest;

import static org.junit.Assert.assertEquals;

import com.sdp.swiftwallet.data.repository.UserDatabase;
import com.sdp.swiftwallet.domain.model.User;
import org.junit.Test;

public class DatabaseTest {

  UserDatabase db = new UserDatabase();

  @Test
  public void addAndUpdateUserCorrectlyWorks(){
    User u1 = new User("admin", "admin", "BASIC");
    db.addAndUpdate(u1);
    assertEquals(0, db.getLoggedUserIndex());
    assertEquals(false, db.isEmpty());
    User u2 = new User("John", "john@epfl.ch", "BASIC");
    db.addAndUpdate(u2);
    assertEquals(1, db.getLoggedUserIndex());
    assertEquals(0, db.addAndUpdate(u1));
  }

  @Test
  public void functionalityTest(){
    User u1 = new User("admin", "admin", "BASIC");
    db.addAndUpdate(u1);
    assertEquals("admin", db.getUser(0).getUsername());
  }

}
