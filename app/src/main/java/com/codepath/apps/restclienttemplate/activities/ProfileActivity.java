package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.helper.TwitterApp;
import com.codepath.apps.restclienttemplate.helper.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    private ActivityProfileBinding binding;
    TwitterClient client;
    User mUser = null;
    UserTimelineFragment userTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        mUser = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        //String screenName = getIntent().getStringExtra("screenName");

        if (mUser != null) {
            populateUserHeadline(mUser);
        } else {
            client = TwitterApp.getRestClient();

            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        mUser = User.fromJSON(response);
                        User.currentUser = mUser;
                        //set the title of actionBar
                        getSupportActionBar().setTitle(mUser.screenName);
                        //populate user headline
                        populateUserHeadline(mUser);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    public void populateUserHeadline(User user) {

        // Display user timeline fragment inside container(dynamically)
        userTimelineFragment = UserTimelineFragment.newInstance(mUser.getScreenName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();

        TextView tvName = binding.tvName;
        TextView tvTagline = binding.tvTagline;
        TextView tvFollowers = binding.tvFollowers;
        TextView tvFollowing = binding.tvFollowing;

        ImageView ivProfileImage = binding.ivHeaderProfileImage;
        tvName.setText(user.name);

        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following");

        Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        //Toast.makeText(this, tweet.user.screenName, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
        //i.putExtra("screenName", tweet.user.screenName);
        i.putExtra("user", Parcels.wrap(tweet.user));
        startActivity(i);
    }

}
