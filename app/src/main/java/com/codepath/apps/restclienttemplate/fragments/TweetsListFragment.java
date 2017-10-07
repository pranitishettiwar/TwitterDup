package com.codepath.apps.restclienttemplate.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.helper.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by praniti on 10/4/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    long max_id;
    static final int PAGE_SIZE = 5;

    private EndlessRecyclerViewScrollListener scrollListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragments_tweet_list, container, false);

        //find the RecyclerView
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //init the arraylist(data source)
        tweets = new ArrayList<>();

        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets, this);

        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(linearLayoutManager);
        //set adapter
        rvTweets.setAdapter(tweetAdapter);

        // Retain an instance so that you can call `resetState()` for fresh searches
        //        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
        //            @Override
        //            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        //                if (isNetworkAvailable()) {
        //                    Toast.makeText(getContext(), "Loading more", Toast.LENGTH_SHORT).show();
        //                    populatePostDelayTimeline(PAGE_SIZE, max_id, 500);
        //                } else {
        //                    Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        //                }
        //            }
        //
        //        };

        //rvTweets.addOnScrollListener(scrollListener);

        max_id = -1;

        //userInfo();
        //populatePostDelayTimeline(PAGE_SIZE, max_id, 500);

        return v;
    }

    //    private Boolean isNetworkAvailable() {
    //        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    //        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    //        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    //    }

    //    private void getNewMaxId(long uid) {
    //        if (max_id < 0 || uid < max_id) {
    //            max_id = uid - 1;
    //        }
    //    }

    public void addItems(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            //convert each object to a tweet model
            // add that tweet model to data source
            //notify adapter that we have added an item
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);

                //getNewMaxId(tweet.uid);

                tweetAdapter.notifyItemInserted(tweets.size() - 1);
                Log.d("DeBUG", tweets.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        Toast.makeText(getContext(), tweet.body, Toast.LENGTH_SHORT).show();
    }
}
