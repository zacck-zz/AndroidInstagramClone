/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText etName;
    EditText etPassword;
    TextView changeSignupModetv;
    Button btSignUp;
    boolean signUpModeActive = true;
    RelativeLayout base;
    ImageView ivLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        if(ParseUser.getCurrentUser() != null)
        {
            Log.i("LoggedIn", "UserLogged In");
            showUserList();
            //ParseUser.logOut();;

        }
        etName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        //btLogin = (Button) findViewById(R.id.btlogin);
        btSignUp = (Button) findViewById(R.id.btsignup);
        changeSignupModetv = (TextView)findViewById(R.id.tvLogin);
        base = (RelativeLayout)findViewById(R.id.relativeLayout);
        ivLogo = (ImageView)findViewById(R.id.ivlogo);

        base.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
        changeSignupModetv.setOnClickListener(this);

        etPassword.setOnKeyListener(this);
        //if user is logged in just got to list




    }

    public void showUserList()
    {
        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);
    }


    public void alert(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
    }

    public void userSignup(View view)
    {
        String uname = etName.getText().toString();
        String pword = etPassword.getText().toString();
        if(signUpModeActive)
        {
            if (uname.isEmpty() || pword.isEmpty()) {
                alert("Please input both username and password fields");
            } else {
                ParseUser mParseUser = new ParseUser();
                mParseUser.setUsername(uname);
                mParseUser.setPassword(pword);


                mParseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUp", "Succesfull");
                            showUserList();
                        } else {
                            alert(e.getMessage());

                        }
                    }
                });

            }

        }
        else
        {
            if (uname.isEmpty() || pword.isEmpty()) {
                alert("Please input both username and password fields");
            } else {
                ParseUser.logInInBackground(uname, pword, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("Login", "Success");
                            showUserList();
                        } else {
                            Log.i("Login", "unsuccessful");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvLogin:
                if(signUpModeActive)
                {
                    signUpModeActive = false;
                    changeSignupModetv.setText("Sign Up");
                    btSignUp.setText("Log In");
                }
                else
                {
                    signUpModeActive = true;
                    changeSignupModetv.setText("Log In");
                    btSignUp.setText("Sign Up");
                }
                break;
            case R.id.relativeLayout:
                InputMethodManager mImm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                break;
            case R.id.ivlogo:
                InputMethodManager mImm2 = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                mImm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                break;

        }
    }

    @Override
    public boolean onKey(View view, int code, KeyEvent keyEvent) {
        //check if enter key has been pressed
        if(code == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN)
        {
            userSignup(view);
        }
        return false;
    }
    //working with parseusers

      /*Logging Users In
      //signing up

      */

     /* Advanced Querying with Parse

      //ParseQuery<ParseObject> mScoreQuery = ParseQuery.getQuery("Score");
      //get specific objects and update them
      mScoreQuery.whereEqualTo("username", "Zacck");
      mScoreQuery.setLimit(1);
      mScoreQuery.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {
              if(e == null)
              {
                  Log.i("FindInBackground", "Retrieved "+ objects.size()+" results");
                  for(ParseObject mObject : objects)
                  {
                      Log.i("Score", String.valueOf(mObject.get("score")));
                      //mObject.put("score",259);
                      //mObject.saveInBackground();
                  }
              }
          }
      });

      get a full list
      mScoreQuery.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {
              if(e == null)
              {
                  Log.i("FindInBackground", "Retrieved "+ objects.size()+" results");
                  for(ParseObject mObject : objects)
                  {
                      Log.i("UserName", String.valueOf(mObject.get("username")));
                  }

              }
              else
              {



              }

          }
      });



    ParseObject score = new ParseObject("Score");
      score.put("username", "Zacck");
      score.put("points", 1999);
      score.saveInBackground(new SaveCallback() {
          @Override
          public void done(ParseException e) {
              if(e == null)
              {
                  Log.i("SaveinBackgroud", "Was successful");
              }
              else{
                  Log.i("SaveinBackgroundFailed", e.toString()+" Occured");
              }
          }
      });
      */

    //updating Parse data
      /*we are querying the object we made
      ParseQuery<ParseObject> mParseQuery = ParseQuery.getQuery("Score");
      mParseQuery.getInBackground("9SfVA6v0lm", new GetCallback<ParseObject>() {
          @Override
          public void done(ParseObject object, ParseException e) {
              if(e == null)
              {
                  object.put("score","259");
                  object.saveInBackground();
              }
              else
              {
                  Log.i("Exception Occured", e.toString());

              }
          }
      });

      ParseQuery<ParseObject> mQuery = ParseQuery.getQuery("Score");
      mQuery.getInBackground("9SfVA6v0lm", new GetCallback<ParseObject>() {
          @Override
          public void done(ParseObject object, ParseException e) {
              if(e == null)
              {
                  object.put("username","zacck");
                  object.saveInBackground();
              }
              else
              {
                  Log.i("Exception Occured", e.toString());

              }
          }
      });
      */


}
