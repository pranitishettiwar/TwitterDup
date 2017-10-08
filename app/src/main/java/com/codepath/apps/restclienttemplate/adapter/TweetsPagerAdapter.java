package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;

/**
 * Created by praniti on 10/5/17.
 */

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;
    private String tabTitles[] = new String[]{ "Home", "Mentions" };
    private Context context;


    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return total # of fragment
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    //return the fragment to use depending the position

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

    //return title

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
