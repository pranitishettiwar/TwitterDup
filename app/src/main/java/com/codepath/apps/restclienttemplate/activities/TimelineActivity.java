package com.codepath.apps.restclienttemplate.activities;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.ComposeFragment;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.helper.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.OnSuccessTweetUpdate {

    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    long max_id;
    private User user;
    static final int PAGE_SIZE = 5;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeFragment composeDialogFragment = ComposeFragment.newInstance(user);
                composeDialogFragment.show(fm, "fragment_compose");

            }
        });

        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //init the arraylist(data source)
        tweets = new ArrayList<>();

        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);

        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(linearLayoutManager);
        //set adapter
        rvTweets.setAdapter(tweetAdapter);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isNetworkAvailable()) {
                    Toast.makeText(TimelineActivity.this, "Loading more", Toast.LENGTH_SHORT).show();
                    populatePostDelayTimeline(PAGE_SIZE, max_id, 500);
                } else {
                    Toast.makeText(TimelineActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }

        };

        rvTweets.addOnScrollListener(scrollListener);

        max_id = -1;

        userInfo();
        populatePostDelayTimeline(PAGE_SIZE, max_id, 500);

    }

    @Override
    public void onFinishTweetCompose(Tweet tweet) {
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void getNewMaxId(long uid) {
        if (max_id < 0 || uid < max_id) {
            max_id = uid - 1;
        }
    }

    void populatePostDelayTimeline(final int count, final long maxId, long delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateTimeline(count, maxId);
            }
        }, delayMillis);
    }

    private void userInfo() {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = User.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
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
        });
    }

    private void populateTimeline(int count, long max_id) {
        client.getHomeTimeline(count, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                //iterate through JSON array
                // for each entry, deserialize the JSON object

                for (int i = 0; i < response.length(); i++) {
                    //convert each object to a tweet model
                    // add that tweet model to data source
                    //notify adapter that we have added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);

                        getNewMaxId(tweet.uid);

                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                        Log.d("DeBUG", tweets.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
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
        });

    }

}
