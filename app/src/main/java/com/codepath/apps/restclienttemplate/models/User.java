package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by praniti on 9/27/17.
 */

public class User {

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public JSONObject userData;


    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        user.userData = json;
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        return user;

    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
