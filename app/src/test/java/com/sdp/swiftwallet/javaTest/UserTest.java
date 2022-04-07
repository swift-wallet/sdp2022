package com.sdp.swiftwallet.javaTest;

import static org.junit.Assert.assertEquals;

import com.sdp.swiftwallet.domain.model.User;
import org.junit.Test;

public class UserTest {

  @Test
  public void isCorrectlyDefined(){
    User u = new User("admin", "admin", "BASIC");
    assertEquals(u.getEmail(), "admin");
    assertEquals(u.getUsername(), "admin");
    assertEquals(u.getLoginMethod(), "BASIC");
  }

  @Test(expected = NullPointerException.class)
  public void errorOnCreation(){
    User u = new User(null, "admin", "BASIC");
  }
  @Test(expected = NullPointerException.class)
  public void errorOnCreationUserEmail(){
    User u = new User("admin", null, "BASIC");
  }
  @Test(expected = NullPointerException.class)
  public void errorOnCreationLoginMethod(){
    User u = new User("ddd", "admin", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidLoginMethod(){
    User u = new User("ddd", "admin", "GRRR");
  }

  @Test
  public void modifyUserNameOnSucces(){
    User u = new User("admin", "admin", "BASIC");
    u.modifyUsername("anton");
    assertEquals("anton", u.getUsername());
  }

}
