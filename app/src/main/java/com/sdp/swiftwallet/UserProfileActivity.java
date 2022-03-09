package com.sdp.swiftwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.presentation.user_profile.components.UserProfileView;

public class UserProfileActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    String username = intent.getStringExtra("username");
    String email = intent.getStringExtra("email");

    UserProfileView view =
        new UserProfileView(R.layout.activity_user_profile, username, email);

  }

  /**
   * Upon pushing button, go back on Main Activity
   */
  public void backMain(View view){
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

}
