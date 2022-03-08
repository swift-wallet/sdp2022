package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.sdp.cryptowalletapp.R;

public class UserProfileActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Set the view of the XML file
    setStaticContent(R.layout.activity_user_profile);

    //Get the login activity state and extract the username of our profile.
    //Intent intent = Intent.getIntent(usr);
    //String message = intent.getStringExtra(EXTRA_MESSAGE);

    TextView textView = findViewById(R.id.mainGreeting);
    //textView.setText(message);
  }

  /**
   * Set some basic content to the user view
   * @param activity_greeting id of the view
   */
  private void setStaticContent(int activity_greeting) {

    //set the pro
    setContentView(activity_greeting);

    //Set up basic text representations for text view boxes
    TextView titleView = findViewById(R.id.title);
    titleView.setText("User Profile");

    TextView usernameView = findViewById(R.id.username);
    titleView.setText("Your username: ");

    TextView emailView = findViewById(R.id.email);
    titleView.setText("Your email: ");

  }


}
