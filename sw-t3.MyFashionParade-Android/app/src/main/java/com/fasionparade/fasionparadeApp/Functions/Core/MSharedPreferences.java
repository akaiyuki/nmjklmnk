package com.fasionparade.fasionparadeApp.Functions.Core;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by CodeSyaona on 9/15/16.
 */
public class MSharedPreferences {
    private static SharedPreferences mSharedPreferences;
    private static Context mContext;

    private static final String APP_PREFS= "APP_SETTINGS";

    private MSharedPreferences() {}

    public static void init(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Flag.USER_DETAILS, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public static String getSomeStringValue(Context context, String key) {
        return mSharedPreferences.getString(key , "");
    }

    public static void setSomeStringValue(Context context, String key, String newValue) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key , newValue);
//        editor.commit();
        editor.apply();
    }

    public static void clearAllPreferences() {
//        mSharedPreferences.edit().clear().commit();
        mSharedPreferences.edit().clear().apply();
    }

    public static void saveJsonToSharedPref(JSONObject jsonObj, String keyOffset) {

        Iterator<String> iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                MSharedPreferences.setSomeStringValue(AppController.getInstance(), keyOffset+key, jsonObj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static User getUserFromPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Flag.USER_DETAILS, Context.MODE_PRIVATE);
        String userJson = preferences.getString(Flag.USER_DATA, null);
        User user =  Utils.getGson().fromJson(userJson, User.class);
        return user;

    }


}

