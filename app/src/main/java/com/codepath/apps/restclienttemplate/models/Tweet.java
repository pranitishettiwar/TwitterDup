package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import static com.codepath.apps.restclienttemplate.helper.ParseRelativeDate.getRelativeTimeAgo;

/**
 * Created by praniti on 9/27/17.
 */

@Parcel
public class Tweet {

    public String body;
    public long uid;  //database ID for the tweet
    public User user;
    public String createdAt;
    public String timeAgo;

    public Tweet(){

    }

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.timeAgo = getRelativeTimeAgo(tweet.createdAt);
        return tweet;
    }


}
