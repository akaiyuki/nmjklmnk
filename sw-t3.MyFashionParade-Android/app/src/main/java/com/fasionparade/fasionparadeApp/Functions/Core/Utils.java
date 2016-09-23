package com.fasionparade.fasionparadeApp.Functions.Core;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.google.gson.Gson;

/**
 * Created by ksuresh on 4/5/2016.
 */
public class Utils {
    private static final String IS_LOGIN = "IsLoggedIn";
    static SharedPreferences sharedPreferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    public static Gson getGson() {
        Gson gson = new Gson();
        return gson;
    }
    public static User getUserFromPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Flag.USER_DETAILS, Context.MODE_PRIVATE);
        String userJson = preferences.getString(Flag.USER_DATA, null);
        User user =  Utils.getGson().fromJson(userJson, User.class);
        return user;

    }

    public static boolean isLoggedIn(Context context){
        sharedPreferences = context.getSharedPreferences(Flag.USER_DETAILS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
    public static boolean isCameraIn(Context context){
        sharedPreferences = context.getSharedPreferences(Flag.CAMERA_DETAILS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Flag.IS_CAMERA, false);
    }
}
