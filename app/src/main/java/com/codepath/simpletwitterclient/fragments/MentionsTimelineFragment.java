package com.codepath.simpletwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.simpletwitterclient.application.TwitterApp;
import com.codepath.simpletwitterclient.models.Tweet;
import com.codepath.simpletwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by michaelsignorotti on 10/7/17.
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();

        //set the initial maxId for tweets.
        minTweetId = Long.MAX_VALUE;
        populateTimeline(minTweetId);
    }



    @Override
    public void populateTimeline(long minId) {
        client.getMentionsTimeline(minId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("HomeTimelineFragment", "" +response.length());
                Log.d("HomeTimelineFragment", response.toString());
                updateTweets(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }
        });
    }


    @Override
    public void fetchTimelineAsync(long recentId) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        client.getMentionsTimeline(recentId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Remember to CLEAR OUT old items before appending in the new ones
                tweets.clear();
                updateTweets(response);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TimelineActivity", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("TimelineActivity", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TimelineActivity", responseString);
                throwable.printStackTrace();
            }
        });
    }

    public void addMyTweet(Tweet tweet) {
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);

    }

    public void updateTweets(JSONArray response) {
        try {
            ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);

            updateMinTweetId(newTweets);

            tweets.addAll(newTweets);
            //notify the adapter that we have added an item
            tweetAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //update the id for the scroll listener
    public void updateMinTweetId(ArrayList<Tweet> newTweets) {
        for (Tweet t : newTweets) {
            long id = t.getUid();
            if (id < minTweetId) {
                minTweetId = id;
            }
        }
    }
}
