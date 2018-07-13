package com.example.angsala.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

  EditText createdUsername;
  EditText createdPassword;
  Button createAccountButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);

    getSupportActionBar().setTitle("Create an Account");

    createdUsername = (EditText) findViewById(R.id.createUsername);
    createdPassword = (EditText) findViewById(R.id.createPassword);
    createAccountButton = (Button) findViewById(R.id.buttonCreateAccount);

    createAccountButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            final String cUsername = createdUsername.getText().toString();
            final String cPassword = createdPassword.getText().toString();
            create_account_helper(cUsername, cPassword);
          }
        });
  }

  private void create_account_helper(String username, String password) {
    ParseUser user = new ParseUser();
    user.setUsername(username);
    user.setPassword(password);
    user.signUpInBackground(
        new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Intent intent = new Intent(CreateAccountActivity.this, HomeTimelineActivity.class);
              startActivity(intent);
            } else {
              e.printStackTrace();
            }
          }
        });
  }
}
