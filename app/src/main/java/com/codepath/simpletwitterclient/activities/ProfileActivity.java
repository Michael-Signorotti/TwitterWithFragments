package com.codepath.simpletwitterclient.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.simpletwitterclient.application.TwitterApp;
import com.codepath.simpletwitterclient.fragments.UserTimelineFragment;
import com.codepath.simpletwitterclient.models.User;
import com.codepath.simpletwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.string.screenName;
import static com.codepath.apps.restclienttemplate.R.string.user;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TwitterClient client;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        client = TwitterApp.getRestClient();

        Intent intent = getIntent();

        String screenName = null;

        if (intent.hasExtra("user")) {
            user = (User) Parcels.unwrap(intent.getParcelableExtra("user"));
        } else if (intent.hasExtra("screen_name")) {
            screenName = intent.getStringExtra("screen_name");
        }

        if (user != null) {
            screenName = user.screenName;
        }

        //create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

        //display the user timeline fragment inside the container dynamically
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //make changes
        ft.replace(R.id.flContainer, userTimelineFragment);
        //commit
        ft.commit();

        if (user == null && screenName == null) {
            getUserInfo();
        } else if (user != null) {
            populateUserHeadline(user);
        } else {
            getUserInfo(screenName);
        }
    }


    private void getUserInfo() {
        client.getUserInformation(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //deserialize the user object
                try {
                    User user = User.fromJson(response);
                    //update the title of the actionbar
                    populateUserHeadline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void getUserInfo(String screenName) {
        client.getUserInformation(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    JSONObject jsonObject= (JSONObject) response.get(0);
                    User user = User.fromJson(jsonObject);
                    //update the title of the actionbar
                    populateUserHeadline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvProfileUserName);
        TextView tvTagline = (TextView) findViewById(R.id.tvProfileUserTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileUserImage);

        tvName.setText(user.name);
        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following");

        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);

        getSupportActionBar().setTitle("@" + user.screenName);
    }
}
