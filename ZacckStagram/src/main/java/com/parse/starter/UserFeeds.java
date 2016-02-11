package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeeds extends AppCompatActivity {
    LinearLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feeds);
        mLayout = (LinearLayout)findViewById(R.id.mLinLayout);

        Intent mIntent = getIntent();
        String activeUsername = mIntent.getStringExtra("username");
        Log.i(getPackageName(), activeUsername);
        setTitle(activeUsername+"'s Feed");

        //get the user's feed of Images
        ParseQuery<ParseObject> mImageQuery = new ParseQuery<ParseObject>("images");
        mImageQuery.whereEqualTo("username",activeUsername);
        //newest first
        mImageQuery.orderByAscending("createdAt");

        mImageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() > 0)
                    {
                        for(ParseObject mImage: objects)
                        {
                            ParseFile mFile = (ParseFile)mImage.get("image");
                            //download the image file
                            mFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null)
                                    {
                                        //add an image view every time we find a new Image
                                        Bitmap mImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        ImageView mImageView = new ImageView(getApplicationContext());
                                        mImageView.setImageBitmap(mImageBitmap);
                                        mImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));
                                        //add imageview to layout
                                        mLayout.addView(mImageView);
                                    }
                                    else
                                    {
                                        alert(e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                    else
                    {
                        alert("User has no posts yet");
                    }
                }
                else
                {
                    alert(e.getMessage());
                }
            }
        });
    }

    public void alert(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
    }

}
