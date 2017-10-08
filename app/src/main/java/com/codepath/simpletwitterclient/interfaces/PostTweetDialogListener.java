package com.codepath.simpletwitterclient.interfaces;

import com.codepath.simpletwitterclient.models.Tweet;

/**
 * Created by michaelsignorotti on 10/1/17.
 */

public interface PostTweetDialogListener {
    void onPostTweet(Tweet tweet);
}
