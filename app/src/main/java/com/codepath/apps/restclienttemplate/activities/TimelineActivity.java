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
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    private User user;
    long max_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //set the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        //set the adapter for view pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

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

//            @Override
//            public void onFinishTweetCompose (Tweet tweet){
//                tweets.add(0, tweet);
//                tweetAdapter.notifyItemInserted(0);
//                rvTweets.scrollToPosition(0);
//            }

    //    void populatePostDelayTimeline(final int count, final long maxId, long delayMillis) {
    //        final Handler handler = new Handler();
    //        handler.postDelayed(new Runnable() {
    //            @Override
    //            public void run() {
    //                populateTimeline(count, maxId);
    //            }
    //        }, delayMillis);
    //    }

    //    private void userInfo() {
    //        client.getUserInfo(new JsonHttpResponseHandler() {
    //            @Override
    //            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    //                try {
    //                    user = User.fromJSON(response);
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //
    //            @Override
    //            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
    //                Log.d("TwitterClient", response.toString());
    //            }
    //
    //            @Override
    //            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
    //                Log.d("TwitterClient", responseString);
    //                throwable.printStackTrace();
    //            }
    //
    //            @Override
    //            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
    //                Log.d("TwitterClient", errorResponse.toString());
    //                throwable.printStackTrace();
    //            }
    //
    //            @Override
    //            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
    //                Log.d("TwitterClient", errorResponse.toString());
    //                throwable.printStackTrace();
    //            }
    //        });
    //    }
    @Override
    public void onTweetSelected(Tweet tweet) {
        //Toast.makeText(this, tweet.user.screenName, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        //i.putExtra("screenName", tweet.user.screenName);
        i.putExtra("user", Parcels.wrap(tweet.user));
        startActivity(i);
    }

}
