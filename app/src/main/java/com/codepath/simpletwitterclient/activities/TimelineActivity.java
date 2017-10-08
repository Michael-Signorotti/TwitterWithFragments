package com.codepath.simpletwitterclient.activities;

import android.app.Fragment;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.simpletwitterclient.adapters.TweetAdapter;
import com.codepath.simpletwitterclient.application.TwitterApp;
import com.codepath.simpletwitterclient.fragments.HomeTimelineFragment;
import com.codepath.simpletwitterclient.fragments.PostTweetDialogFragment;
import com.codepath.simpletwitterclient.fragments.TweetsListFragment;
import com.codepath.simpletwitterclient.fragments.TweetsPagerAdapter;
import com.codepath.simpletwitterclient.interfaces.PostTweetDialogListener;
import com.codepath.simpletwitterclient.interfaces.TweetSelectedListener;
import com.codepath.simpletwitterclient.models.User;
import com.codepath.simpletwitterclient.network.TwitterClient;
import com.codepath.simpletwitterclient.models.Tweet;
import com.codepath.simpletwitterclient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.fragment;
import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.codepath.apps.restclienttemplate.R.id.rvTweets;
import static com.codepath.apps.restclienttemplate.R.id.swipeContainer;
import static com.codepath.apps.restclienttemplate.R.string.user;

public class TimelineActivity extends AppCompatActivity implements PostTweetDialogListener, TweetSelectedListener {

    Toolbar toolbar;
    TwitterClient client;

    ViewPager vpPager;
    TabLayout tabLayout;

    TweetsPagerAdapter tweetsPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        client = TwitterApp.getRestClient();

        //get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);

        //set the adapter for the view pager
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(tweetsPagerAdapter);

        //setup the tab layout to use the viewpager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
        //fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);

        //getUserInformation();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.miPerson) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.miCreate) {
            showPostTweetDialog();
        }
        return true;
    }

    public void showPostTweetDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostTweetDialogFragment postTweetDialogFragment = new PostTweetDialogFragment();
        postTweetDialogFragment.show(fm, "fragment_post_tweet");
    }

    @Override
    public void onPostTweet(Tweet tweet) {

        /*
        HomeTimelineFragment fragment = (HomeTimelineFragment) tweetsPagerAdapter.getItem(0);
        fragment.addMyTweet(tweet);
        */
        HomeTimelineFragment fragment = (HomeTimelineFragment) tweetsPagerAdapter.getRegisteredFragment(0);
        fragment.addMyTweet(tweet);
        vpPager.setCurrentItem(0);
    }

    @Override
    public void onProfileSelected(Tweet tweet) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(tweet.user));
        startActivity(i);

    }
}
