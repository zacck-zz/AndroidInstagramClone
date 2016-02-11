package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class UserFeeds extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feeds);

        Intent mIntent = getIntent();
        String activeUsername = mIntent.getStringExtra("username");
        Log.i(getPackageName(), activeUsername);
        setTitle(activeUsername+"'s Feed");

    }
}
