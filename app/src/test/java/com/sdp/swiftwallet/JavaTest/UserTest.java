package com.sdp.swiftwallet.JavaTest;

import static com.sdp.swiftwallet.domain.repository.SwiftAuthenticator.LoginMethod.BASIC;
import static org.junit.Assert.assertEquals;

import com.sdp.swiftwallet.domain.model.User;
import org.junit.Test;

public class UserTest {

  public static User u = new User("admin@epfl.ch", BASIC);

  @Test
  public void isCorrectlyDefined() {
    assertEquals("admin@epfl.ch", u.getEmail());
  }

  @Test
  public void modifyUserNameOnSucces() {
    User u2 = new User("admin@epfl.ch", BASIC);
    u2.setEmail("anton");
    assertEquals("anton", u2.getEmail());
  }

}
