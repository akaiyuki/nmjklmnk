package com.fasionparade.fasionparadeApp.Functions.Service;

import android.util.Log;

import com.fasionparade.fasionparadeApp.Functions.Object.PublicParade;
import com.fasionparade.fasionparadeApp.Functions.Object.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuresh on 4/4/2016.
 */
public class JsonParser {
    private static String TAG = JsonParser.class.getName();
    public static User getUser(JSONObject userObject) {
      //  List<User> userList = new ArrayList<User>();
        User user = new User();
        try {

            if (userObject.getString("errorCode").equals("200")) {

                JSONArray array = userObject.getJSONArray("userDetails");
                JSONObject jObject;// = array.getJSONObject(0);

                for (int i = 0; i < array.length(); ++i) {

                    //User user = new User();
                    jObject = array.getJSONObject(i);
                    user.id=jObject.getString("userId");
                    user.contactName = jObject.getString("userName");
                    user.mail = jObject.getString("emailId");
                    user.password = jObject.getString("password");
                    user.mobile = jObject.getString("phoneNumber");
                    user.website = jObject.getString("website");
                    user.bio = jObject.getString("bio");

                    user.countryCode = jObject.getString("countryCode");
                    user.profilePic = jObject.getString("profilePic");
                    user.userType = jObject.getString("userType");
                    user.followers = jObject.getString("followers");

                    user.followings = jObject
                            .getString("followings");
                    user.parades = jObject.getString("parades");
                    user.message="success";


                    //userList.add(user);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "e33=" + e.getMessage());
            user.message="failure";
        }
        return user;
    }
    public static List<PublicParade> getPPResult(JSONObject paradeObject) {
        List<PublicParade> paradeList = new ArrayList<PublicParade>();

        try {

            if (paradeObject.getString("errorCode").equals("200")) {

                JSONArray paradearray = paradeObject.getJSONArray("paradeDetails");
                JSONObject jObject;// = array.getJSONObject(0);

                for (int i = 0; i < paradearray.length(); ++i) {

                    PublicParade ppDate = new PublicParade();
                    jObject = paradearray.getJSONObject(i);
                    ppDate.userId=jObject.getString("userId");
                    ppDate.paradeId = jObject.getString("paradeId");
                    ppDate.paradeName = jObject.getString("paradeName").replaceAll("\\+", " ");
                    ppDate.sharedWith = jObject.getString("sharedWith");
                    ppDate.duration = jObject.getString("duration");
                    ppDate.aboutParade = jObject.getString("aboutParade").replaceAll("\\+", " ");
                    ppDate.tag = jObject.getString("tag");
                    ppDate.userName=jObject.getString("userName");
                    ppDate.profilePic=jObject.getString("profilePic");

                    ppDate.startTime = jObject.getString("startTime");
                    ppDate.endTime = jObject.getString("endTime");
                    ppDate.groupId = jObject.getString("groupId");
                    ppDate.followingStatus = jObject.getString("followingStatus");
                    ppDate.imagePathJson=jObject.getJSONArray("imageDetails");
                    ppDate.type="parade";


                    paradeList.add(ppDate);
                }

                JSONArray contactarray = paradeObject.getJSONArray("contactsDetails");
                JSONObject friendjObject;// = array.getJSONObject(0);

                for (int i = 0; i < contactarray.length(); ++i) {

                    PublicParade ppDate = new PublicParade();
                    friendjObject = contactarray.getJSONObject(i);
                    ppDate.friendId=friendjObject.getString("friendId");
                    ppDate.userName=friendjObject.getString("userName");
                    ppDate.profilePic=friendjObject.getString("profilePic");
                    ppDate.type="contact";
                    paradeList.add(ppDate);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "e33=" + e.getMessage());

        }
        return paradeList;
    }
    public static List<PublicParade> getParadeResult(JSONObject paradeObject) {
        List<PublicParade> paradeList = new ArrayList<PublicParade>();

        try {

            if (paradeObject.getString("errorCode").equals("200")) {

                JSONArray array = paradeObject.getJSONArray("paradeDetails");
                JSONObject jObject;// = array.getJSONObject(0);

                for (int i = 0; i < array.length(); ++i) {

                    PublicParade ppDate = new PublicParade();
                    jObject = array.getJSONObject(i);
                    ppDate.userId=jObject.getString("userId");
                    ppDate.paradeId = jObject.getString("paradeId");
                    ppDate.paradeName = jObject.getString("paradeName").replaceAll("\\+", " ");
                    ppDate.sharedWith = jObject.getString("sharedWith");
                    ppDate.duration = jObject.getString("duration");
                    ppDate.aboutParade = jObject.getString("aboutParade").replaceAll("\\+", " ");
                    ppDate.tag = jObject.getString("tag");
                    ppDate.startTime = jObject.getString("startTime");
                    ppDate.endTime = jObject.getString("endTime");
                    ppDate.groupId = jObject.getString("groupId");
                    ppDate.sharedWith = jObject.getString("sharedWith");
                    ppDate.completedStatus = jObject.getString("completedStatus");
                    ppDate.imagePathJson=jObject.getJSONArray("imageDetails");


                    paradeList.add(ppDate);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "e33=" + e.getMessage());

        }
        return paradeList;
    }
    public static List<PublicParade> getWinnerResult(JSONObject paradeObject) {
        List<PublicParade> paradeList = new ArrayList<PublicParade>();

        try {

            if (paradeObject.getString("errorCode").equals("200")) {

                JSONArray array = paradeObject.getJSONArray("paradeDetails");
                JSONObject jObject;// = array.getJSONObject(0);

                for (int i = 0; i < array.length(); ++i) {

                    PublicParade ppDate = new PublicParade();
                    jObject = array.getJSONObject(i);
                    ppDate.userId=jObject.getString("userId");
                    ppDate.paradeId = jObject.getString("paradeId");
                    ppDate.paradeName = jObject.getString("paradeName").replaceAll("\\+", " ");
                    ppDate.sharedWith = jObject.getString("sharedWith");
                    ppDate.duration = jObject.getString("duration");
                    ppDate.aboutParade = jObject.getString("aboutParade").replaceAll("\\+", " ");
                    ppDate.tag = jObject.getString("tag");

                    ppDate.startTime = jObject.getString("startTime");
                    ppDate.endTime = jObject.getString("endTime");
                    ppDate.groupId = jObject.getString("groupId");
                    ppDate.imagePathJson=jObject.getJSONArray("results");

                    paradeList.add(ppDate);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "e33=" + e.getMessage());

        }
        return paradeList;
    }
    public static List<User> getFollowResult(JSONObject paradeObject) {
        List<User> followerList = new ArrayList<User>();

        try {

            if (paradeObject.getString("errorCode").equals("200")) {

                JSONArray array = paradeObject.getJSONArray("followersDetails");
                JSONObject jObject;// = array.getJSONObject(0);

                for (int i = 0; i < array.length(); ++i) {

                    User ppDate = new User();
                    jObject = array.getJSONObject(i);
                    ppDate.contactName=jObject.getString("userName");
                    ppDate.mail = jObject.getString("emailId");
                    ppDate.id = jObject.getString("userId");
                    ppDate.profilePic = jObject.getString("profilePic");
                    ppDate.followers = jObject.getString("fType");
                    ppDate.followingsStatus = jObject.getString("followingStatus");
                    ppDate.chatBlockedStatus = jObject.getString("chatBlockedStatus");


                    followerList.add(ppDate);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "e33=" + e.getMessage());

        }
        return followerList;
    }
    // Call lastparades
    public static List<PublicParade> getlastparades(JSONObject paradeObject) {
        List<PublicParade> paradeList = new ArrayList<PublicParade>();

        try {

            if (paradeObject.getString("errorCode").equals("200")){

                JSONArray array = paradeObject.getJSONArray("paradeDetails");
                JSONObject jObject;// = array.getJSONObject(0);

                for (int i = 0; i < array.length(); ++i) {

                    PublicParade ppDate = new PublicParade();
                    jObject = array.getJSONObject(i);
                    ppDate.userId=jObject.getString("userId");
                    ppDate.paradeId = jObject.getString("paradeId");
                    ppDate.paradeName = jObject.getString("paradeName");
                    ppDate.sharedWith = jObject.getString("sharedWith");
                    ppDate.duration = jObject.getString("duration");
                    ppDate.aboutParade = jObject.getString("aboutParade");
                    ppDate.tag = jObject.getString("tag");

                    ppDate.startTime = jObject.getString("startTime");
                    ppDate.endTime = jObject.getString("endTime");
                    ppDate.groupId = jObject.getString("groupId");
                    ppDate.imagePathJson=jObject.getJSONArray("imageDetails");

                    paradeList.add(ppDate);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "e33=" + e.getMessage());

        }
        return paradeList;
    }
}
