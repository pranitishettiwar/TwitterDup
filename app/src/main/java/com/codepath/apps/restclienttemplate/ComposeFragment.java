package com.codepath.apps.restclienttemplate;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by praniti on 10/1/17.
 */

public class ComposeFragment extends DialogFragment {

    private EditText etCompose;
    private TextView tvCounter, tvProfileName;
    private ImageView ivProfileImage;
    private Button btnTweet;
    private User user;
    private TwitterClient client;

    public ComposeFragment() {

    }

    public static ComposeFragment newInstance(User user) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("jsonData", user.userData.toString());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getRestClient();
        // Get field from view
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage1);
        btnTweet = (Button) view.findViewById(R.id.buttonTweet);

        try {
            JSONObject currentUserJSON = new JSONObject(getArguments().getString("jsonData"));
            user = User.fromJSON(currentUserJSON);
        } catch (JSONException e) {
            log.d("JSON parsing exception when creating user", e.toString());
        }

        String str = new String(user.profileImageUrl);
        str = str.replace("_normal", "");

        tvProfileName.setText(user.screenName);
        Glide.with(getContext()).load(str).apply(bitmapTransform(new RoundedCornersTransformation(10, 0,
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
                client.postTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = new Tweet();
                        tweet.user = user;
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
            }

        });
    }


    public interface OnSuccessTweetUpdate {
        void onFinishTweetCompose(Tweet tweet);
    }

}

