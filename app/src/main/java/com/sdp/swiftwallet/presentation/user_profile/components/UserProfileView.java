package com.sdp.swiftwallet.presentation.user_profile.components;

import android.widget.TextView;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.sdp.cryptowalletapp.R;

/**
 * User Profile view destined for profiles
 */
public class UserProfileView extends AppCompatActivity {

  /**
   * Create a view of the user login screen
   * @param activity_id
   * @param user
   * @param email
   */
  public UserProfileView(int activity_id, String user, String email){
    setContentView(activity_id);
    setStaticContent(activity_id);
    setLoginInfo(activity_id, user, email);
  }


  /**
   * Set some basic content to the user view
   * @param id id of the view
   */
  private void setStaticContent(int id) {

    //set the pro
    setContentView(id);

    //Set up basic text representations for text view boxes
    //Some colors needs to be added...
    TextView titleView = findViewById(R.id.title);
    titleView.setText("User Profile");

    TextView usernameView = findViewById(R.id.username);
    usernameView.setText("Your username: ");

    TextView emailView = findViewById(R.id.email);
    emailView.setText("Your email: ");

    TextView random = findViewById(R.id.to_add);
    random.setText("more to come ...");

  }

  /**
   * Dynamic function for putting info content
   * @param id id of the user
   * @param user username to put
   * @param email email to put
   */
  private void setLoginInfo(int id, String user, String email){

    //Put username
    TextView userView = findViewById(R.id.username_input);
    userView.setText(user);

    //Put email
    TextView emailView = findViewById(R.id.email_input);
    emailView.setText(email);
  }

}
