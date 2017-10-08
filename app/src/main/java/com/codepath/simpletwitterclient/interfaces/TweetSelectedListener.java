package com.codepath.simpletwitterclient.interfaces;

import com.codepath.simpletwitterclient.models.Tweet;

/**
 * Created by michaelsignorotti on 10/8/17.
 */

public interface TweetSelectedListener {
    void onProfileSelected(Tweet tweet);
}
