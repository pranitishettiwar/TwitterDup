package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by praniti on 9/27/17.
 */

@Parcel
public class User {

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    //public JSONObject userData;
    public String tagLine;
    public int followersCount;
    public int followingCount;

    public static User currentUser;

    public User(){

    }

    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        //user.userData = json;
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        user.tagLine = json.getString("description");
        user.followersCount = json.getInt("followers_count");
        user.followingCount = json.getInt("friends_count");
        return user;

    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {

        String str = new String(profileImageUrl);
        str = str.replace("_normal", "");

        return str;
    }
}
