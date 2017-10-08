package com.codepath.simpletwitterclient.models;

import android.content.res.Resources;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.simpletwitterclient.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by michaelsignorotti on 9/30/17.
 */

public class Tweet {

    //list out the attributes
    public String body;
    //Database id for the Tweet
    public long uid;
    public User user;
    public String createdAt;
    public String relativeTimestamp;


    //deserialize the JSON
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.relativeTimestamp = TimeUtils.getRelativeTimeAgo(tweet.createdAt);

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        //for each entry, deserialize the json object
        for (int i = 0; i < jsonArray.length(); i++) {
            //convert each object to a tweet model
            Tweet tweet = null;
                tweet = (Tweet) Tweet.fromJson(jsonArray.getJSONObject(i));


                //add the tweet to the data source
                tweets.add(tweet);
        }

        return tweets;
    }

    public long getUid() {
        return uid;
    }
}
