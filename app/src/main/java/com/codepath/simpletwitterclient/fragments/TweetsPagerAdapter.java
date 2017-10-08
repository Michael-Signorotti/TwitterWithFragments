package com.codepath.simpletwitterclient.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by michaelsignorotti on 10/7/17.
 */

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"Home", "Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return the total number of fragments
    @Override
    public int getCount() {
        return 2;
    }


    //return the fragment to use depending on position
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            return new MentionsTimelineFragment();
        } else {
            return null;
        }
    }


    //return the title
    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }
}
