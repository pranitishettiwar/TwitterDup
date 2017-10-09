package com.codepath.apps.restclienttemplate.fragments;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.FragmentComposeBinding;
import com.codepath.apps.restclienttemplate.helper.TwitterApp;
import com.codepath.apps.restclienttemplate.helper.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by praniti on 10/1/17.
 */

public class ComposeFragment extends DialogFragment {

    private EditText etCompose;
    private TextView tvCounter, tvProfileName;
    private ImageView ivProfileImage;
    private Button btnTweet;
    private User mUser;
    private TwitterClient client;
    private FragmentComposeBinding binding;

    public ComposeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_compose, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getRestClient();
        // Get field from view
        etCompose = binding.etCompose;
        tvCounter = binding.tvCounter;
        tvProfileName = binding.tvProfileName;
        ivProfileImage = binding.ivProfileImage1;
        btnTweet = binding.buttonTweet;

        mUser = new User().currentUser;

        if (mUser != null) {
            composeTweet();
        }
        else{
            client = TwitterApp.getRestClient();

            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        mUser = User.fromJSON(response);
                        User.currentUser = mUser;

                        composeTweet();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }

    }

    private void composeTweet() {
        tvProfileName.setText(mUser.screenName);
        Glide.with(getContext()).load(mUser.getProfileImageUrl()).apply(bitmapTransform(new RoundedCornersTransformation(10, 0,
            RoundedCornersTransformation.CornerType.ALL))).into(ivProfileImage);

        etCompose.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int currentLength = etCompose.getText().length();
                int maxLength = 140;
                int left = maxLength - currentLength;
                tvCounter.setText(left + "");
                return false;
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String composeTweet = etCompose.getText().toString();
                Log.d("debug", "Post status: " + composeTweet);
                if (isNetworkAvailable()) {
                client.postTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = new Tweet();
                        tweet.user = mUser;
                        tweet.body = composeTweet;
                        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
                        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
                        Date dt = new Date();
                        tweet.createdAt = sf.format(dt);
                        Toast.makeText(getContext(), "Tweet posted !", Toast.LENGTH_LONG).show();
                        OnSuccessTweetUpdate listener = (OnSuccessTweetUpdate) getActivity();
                        listener.onFinishTweetCompose(tweet);
                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("debug", "Tweet post failed " + errorResponse.toString());
                    }
                }, composeTweet);
                } else {
                    Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public interface OnSuccessTweetUpdate {
        void onFinishTweetCompose(Tweet tweet);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context
            .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}

