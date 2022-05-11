package com.sdp.swiftwallet.domain.object;

import static com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator.LoginMethod.BASIC;
import static org.junit.Assert.assertEquals;

import com.sdp.swiftwallet.domain.model.object.User;
import org.junit.Test;

public class UserTest {

  public static User u = new User("testUid123", "admin@epfl.ch", BASIC);

  @Test
  public void isCorrectlyDefined() {
    assertEquals("testUid123", u.getUid());
    assertEquals("admin@epfl.ch", u.getEmail());
  }

  @Test
  public void modifyUsernameOnSucces() {
    User u2 = new User("testUid123", "admin@epfl.ch", BASIC);
    u2.setEmail("anton");
    assertEquals("anton", u2.getEmail());
  }

  @Test
  public void modifyUidOnSucces() {
    User u2 = new User("testUid123", "admin@epfl.ch", BASIC);
    u2.setUid("modifiedTestUid123");
    assertEquals("modifiedTestUid123", u2.getUid());
  }

}
