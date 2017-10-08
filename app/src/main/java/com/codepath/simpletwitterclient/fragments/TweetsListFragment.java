package com.codepath.simpletwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.simpletwitterclient.adapters.TweetAdapter;
import com.codepath.simpletwitterclient.application.TwitterApp;
import com.codepath.simpletwitterclient.interfaces.TweetAdapterListener;
import com.codepath.simpletwitterclient.interfaces.TweetSelectedListener;
import com.codepath.simpletwitterclient.models.Tweet;
import com.codepath.simpletwitterclient.network.TwitterClient;
import com.codepath.simpletwitterclient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by michaelsignorotti on 10/7/17.
 */

public abstract class TweetsListFragment extends Fragment implements TweetAdapterListener {

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    LinearLayoutManager linearLayoutManager;

    long minTweetId;

    // Store a member variable for the listener
    EndlessRecyclerViewScrollListener scrollListener;

    SwipeRefreshLayout swipeContainer;

    TwitterClient client;

    //inflation happens in onCreateView


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);


        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        tweets = new ArrayList<Tweet>();

        tweetAdapter = new TweetAdapter(tweets, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        Log.d("TweetsListFragment", "set the adapter");

        //set the initial maxId for tweets.
        //minTweetId = Long.MAX_VALUE;


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        client = TwitterApp.getRestClient();


        //populateTimeline(minTweetId);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi();
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(Long.MAX_VALUE);
            }
        });

        return v;
    }

    public void loadNextDataFromApi() {
        populateTimeline(minTweetId);
    }

    public void populateTimeline(long id){

    }

    public void fetchTimelineAsync(long id) {

    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);

        ((TweetSelectedListener) getActivity()).onProfileSelected(tweet);
    }
}
