package com.codepath.simpletwitterclient.interfaces;

import android.view.View;

/**
 * Created by michaelsignorotti on 10/8/17.
 */

public interface TweetAdapterListener {
    void onItemSelected(View view, int position);
    void onScreenNameSelected(String screenName);
}
