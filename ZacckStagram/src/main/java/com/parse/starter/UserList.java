package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {
    ListView userList;
    ArrayList<String> usernames;
    ArrayAdapter mUserAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        usernames = new ArrayList<String>();
        mUserAdp = new ArrayAdapter<String>(UserList.this, android.R.layout.simple_list_item_1,usernames);

        userList = (ListView)findViewById(R.id.mUserList);
        ParseQuery<ParseUser> mUserQuery = ParseUser.getQuery();
        //show other users not current
        mUserQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        mUserQuery.addAscendingOrder("username");

        mUserQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() > 0)
                    {

                        for(ParseUser user: objects)
                        {
                            usernames.add(user.getUsername());
                        }


                        userList.setAdapter(mUserAdp);


                    }

                }
            }
        });
    }
}
