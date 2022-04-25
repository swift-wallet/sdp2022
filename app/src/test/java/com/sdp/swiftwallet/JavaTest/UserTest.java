package com.sdp.swiftwallet.JavaTest;

import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.BASIC;
import static org.junit.Assert.assertEquals;

import com.sdp.swiftwallet.domain.model.User;
import org.junit.Test;

public class UserTest {

  public static User u = new User("admin@epfl.ch", BASIC);

  @Test
  public void isCorrectlyDefined() {
    assertEquals(u.getEmail(), "admin");
  }

  @Test(expected = NullPointerException.class)
  public void errorOnCreation() {
    User u = new User(null, BASIC);
  }

  @Test
  public void getEmailTest() {
    assertEquals("admin@epfl.ch", u.getEmail());
  }

  @Test
  public void modifyUserNameOnSucces() {
    u.setEmail("anton");
    assertEquals("anton", u.getEmail());
  }

}
