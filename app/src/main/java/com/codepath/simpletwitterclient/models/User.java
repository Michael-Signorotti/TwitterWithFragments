package com.codepath.simpletwitterclient.models;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by michaelsignorotti on 9/30/17.
 */

@Parcel
public class User {

    //list the attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    public String tagLine;
    public int followersCount;
    public int followingCount;


    // empty constructor needed by the Parceler library
    public User() {
    }

    //Deserialize from JSON

    public static User fromJson(JSONObject jsonObject) throws JSONException {

        User user = new User();
        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url");
        user.tagLine = jsonObject.getString("description");
        user.followersCount = jsonObject.getInt("followers_count");
        user.followingCount = jsonObject.getInt("friends_count");

        return user;
    }

}
