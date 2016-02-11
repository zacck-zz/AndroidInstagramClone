package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //show menu on actionbar
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }


    //handle menu item clicks

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.share:
                //make a photo intent
                Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //go to the event
                //using start activity for result
                startActivityForResult(photoIntent, 1/*use this int to check if the intent returning is the one yoou sent*/);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void alert(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
    }

    //a listener for the result of start activity for result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //validate the intent
        if(requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            //collect the returned iMage
            try {
                Uri selectedImage = data.getData();
                Bitmap mBitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //lets put the image in the image view
                //mImageView.setImageBitmap(mBitmapImage);
                Log.i(getPackageName(), "Image Recieved");
                //attempt Image upload
                //convert image into byte array
                ByteArrayOutputStream mImageStream = new ByteArrayOutputStream();
                mBitmapImage.compress(Bitmap.CompressFormat.PNG, 100, mImageStream);
                byte[] mImageBytes = mImageStream.toByteArray();

                ParseFile mImage = new ParseFile("zacckscat.png", mImageBytes);

                ParseObject mImageUploadObject = new ParseObject("images");
                mImageUploadObject.put("username", ParseUser.getCurrentUser().getUsername());
                mImageUploadObject.put("image", mImage);

                mImageUploadObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            alert("Your Image has been Posted :)");
                        } else
                        {
                            alert(e.getMessage());
                        }
                    }
                });


            }
            catch(Exception e)
            {
                alert(e.getMessage());
            }
        }
    }
}
