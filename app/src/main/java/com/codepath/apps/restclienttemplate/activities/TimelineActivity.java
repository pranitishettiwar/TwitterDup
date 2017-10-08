package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapter.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.ComposeFragment;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener, ComposeFragment
    .OnSuccessTweetUpdate {

    private User user;
    long max_id;
    private TweetsPagerAdapter mTweetsPagerAdapter;
    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mTweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        //set the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);

        //set the adapter for view pager
        vpPager.setAdapter(mTweetsPagerAdapter);

        //set the TabLayout to use view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        //launch Profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }


    public void onCompose(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeDialogFragment = new ComposeFragment();
        composeDialogFragment.show(fm, "fragment_compose");

    }

    //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    //        fab.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                FragmentManager fm = getSupportFragmentManager();
    //                ComposeFragment composeDialogFragment = ComposeFragment.newInstance(user);
    //                composeDialogFragment.show(fm, "fragment_compose");
    //
    //            }
    //        });

    @Override
    public void onFinishTweetCompose(Tweet tweet) {
        HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) mTweetsPagerAdapter.getRegisteredFragment(0);

        homeTimelineFragment.insertTweetAtTop(tweet);
        vpPager.setCurrentItem(0);

        //Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT).show();

    }

    //    void populatePostDelayTimeline(final int count, final long maxId, long delayMillis) {
    //        final Handler handler = new Handler();
    //        handler.postDelayed(new Runnable() {
    //            @Override
    //            public void run() {
    //                populateTimeline(count, maxId);
    //            }
    //        }, delayMillis);
    //    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(tweet.user));
        startActivity(i);
    }

}
