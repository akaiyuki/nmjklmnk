package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Models.Adapters.LatestparadesAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.PublicParade;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MFPUserDetailsActivity extends AppCompatActivity {

    private ImageView backToScreen, circleImageView_userImageMfp;


    private TextView User_name_mfp, Follower_txt_value, Following_txt_value, Website_txt, Bio_txt, Mail_txt;

    private CircleImageView circleImageView;
    static String userName = "", followerValue = "", followingValue = "", webValue = "", mailValue = "", bioValue = "", followerId = "", followerName = "", profilePic = "",followingStatus="";
    boolean follow_clicked;

    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    Context context;
    User user;
    ArrayList<ActiveParade> myParadeList;

    ArrayList<ActiveParade> myWinnerList;


    RecyclerView recyclerView, recyclerViewwinner;
    private ImageView active_parade, active_winner;


    RelativeLayout followersLinLayout, followingLinLayout;


    LinearLayout activeParadeLayout, winnerLayout, linearlayout_chat_mfp;
    TextView groupTextView, activeParadeTextView,followTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfpuserdetails);


        context = this;
        myParadeList = new ArrayList<>();
        myWinnerList = new ArrayList<>();
        circleImageView_userImageMfp = (ImageView) findViewById(R.id.circleImagView_userimagemfp);
        backToScreen = (ImageView) findViewById(R.id.backImageView_MFPUserdetails);
        User_name_mfp = (TextView) findViewById(R.id.textView_username_mfpuser);
        Follower_txt_value = (TextView) findViewById(R.id.followerTxt_mfp_values);
        Following_txt_value = (TextView) findViewById(R.id.followingTxt_mfpuserdetails_value);
        Website_txt = (TextView) findViewById(R.id.webTxt_mfpuser);
        Bio_txt = (TextView) findViewById(R.id.bioTxtmfpuser);
        Mail_txt = (TextView) findViewById(R.id.mailTxtmfpuser);
        winnerLayout = (LinearLayout) findViewById(R.id.winnerLayout_mfpuser);
        activeParadeLayout = (LinearLayout) findViewById(R.id.activeParadeLayout_mfpuser);


        followersLinLayout = (RelativeLayout) findViewById(R.id.followersLayout);

        followingLinLayout = (RelativeLayout) findViewById(R.id.followingLayout);

        active_parade = (ImageView) findViewById(R.id.activeParadeImageView_user);
        active_winner = (ImageView) findViewById(R.id.winnerImageView_user);

        followTextView=(TextView) findViewById(R.id.followTextView);

        linearlayout_chat_mfp = (LinearLayout) findViewById(R.id.chat_mfp);

        recyclerView = (RecyclerView) findViewById(R.id.list_myactive_parade);
        recyclerViewwinner = (RecyclerView) findViewById(R.id.list_mywinner_parade);
        recyclerViewwinner.setVisibility(View.GONE);


        groupTextView = (TextView) findViewById(R.id.groupTextViewmfpuser);
        activeParadeTextView = (TextView) findViewById(R.id.activeParadeTextViewmfpuser);


        followerId = getIntent().getStringExtra("followerid");
        followerName = getIntent().getStringExtra("followername");
        getMfpuserdetials();

//
        followersLinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MFPUserDetailsActivity.this, FollowerListActivity.class);
                i.putExtra("friendid", followerId);
                i.putExtra("status", "followers");
                startActivity(i);
            }
        });


        followingLinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MFPUserDetailsActivity.this, FollowerListActivity.class);
                i.putExtra("friendid", followerId);
                i.putExtra("status", "following");
                startActivity(i);
            }
        });

        linearlayout_chat_mfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MFPUserDetailsActivity.this, ChatDetailActivity.class);
                intent.putExtra("RECEIVER_ID", followerId);
                intent.putExtra("NAME", followerName);
                startActivity(intent);
            }
        });

        followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUnfollowParadeRequest();
            }
        });


        backToScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });


        activeParadeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                active_parade.setImageResource(R.drawable.parade_icon_white_color);
                active_winner.setImageResource(R.drawable.winner_icon);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                    winnerLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteBackground));
                    groupTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGroupText));
                } else {
                    activeParadeLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
                    winnerLayout.setBackgroundColor(getResources().getColor(R.color.colorWhiteBackground));
                    groupTextView.setTextColor(getResources().getColor(R.color.colorGroupText));
                }

                recyclerViewwinner.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                getMyParadeList();

            }
        });


        winnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                active_parade.setImageResource(R.drawable.parade_icon);
                active_winner.setImageResource(R.drawable.winner_icon_whitecolor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteBackground));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorGroupText));
                    winnerLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                    groupTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                } else {
                    activeParadeLayout.setBackgroundColor(getResources().getColor(R.color.colorWhiteBackground));
                    activeParadeTextView.setTextColor(getResources().getColor(R.color.colorGroupText));
                    winnerLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                    groupTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
                }

                recyclerViewwinner.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                getMyWinnerParadeList();

            }
        });


    }


    public void getMfpuserdetials() {
        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {


            String url = ResourceManager.getMyProfile();
            user = Utils.getUserFromPreference(context);
            String newURL = url + "userId=" + user.id+"&friendId="+followerId;
            Log.i("MFP Response", newURL);
            new MFPUserDetails().execute(newURL);

        }

    }


    private void getMyParadeList() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.myActiveParade();
            url = url + "userId=" + followerId + "&visitorId=" + user.id;
            new getMyParede().execute(url);
            Log.i("myActiveparade", url);

        }
    }


    private void getMyWinnerParadeList() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.myWinnerParade();
            url = url + "userId=" + followerId + "&visitorId=" + user.id;
            new myWinnerParade().execute(url);
            Log.i("myWinnerParade", url);

        }
    }

    private class myWinnerParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        String paradeName;
        String startTime;
        String endTime;
        String image;
        String userId;
        String paradeId;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.getData(params[0]);


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {

                    pParade = JsonParser.getWinnerResult(jsonObject);
                    int length = pParade.size();
                    if (length > 0) {
                        myWinnerList = new ArrayList<ActiveParade>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++) {
                            ActiveParade activeParade = new ActiveParade();
                            JSONArray imageArray = pParade.get(j).imagePathJson;
                            imageObject = imageArray.getJSONObject(0);

                            paradeName = pParade.get(j).paradeName;
                            startTime = pParade.get(j).startTime;
                            endTime = pParade.get(j).endTime;
                            userId = pParade.get(j).userId;
                            paradeId = pParade.get(j).paradeId;
                            image = imageObject.getString("fileName");
                            activeParade.imagePathJson = imageArray;
                            activeParade.paradeName = paradeName;
                            activeParade.startTime = startTime;
                            activeParade.endTime = endTime;
                            activeParade.imagePath = image;
                            activeParade.userId = userId;
                            activeParade.paradeId = paradeId;
                            activeParade.tag = pParade.get(j).tag;
                            activeParade.aboutParade = pParade.get(j).aboutParade;
                            activeParade.sharedWith = pParade.get(j).sharedWith;
                            activeParade.groupId = pParade.get(j).groupId;
                            myWinnerList.add(activeParade);
                        }


                        LatestparadesAdapter latestparadesAdapter = new LatestparadesAdapter(MFPUserDetailsActivity.this, myWinnerList, false);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MFPUserDetailsActivity.this, 3);
                        recyclerViewwinner.setLayoutManager(gridLayoutManager);

                        //adding item decoration
                        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
                        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, spacing, true);
                        recyclerViewwinner.addItemDecoration(gridSpacingItemDecoration);
                        recyclerViewwinner.setAdapter(latestparadesAdapter);


                        latestparadesAdapter.SetOnItemClickListener(new LatestparadesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                user = Utils.getUserFromPreference(context);
                                String userid = myWinnerList.get(position).userId;
                                if (userid.equalsIgnoreCase(user.id)) {
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(myWinnerList);
                                    Intent intent = new Intent(context, LastWinnersActivity.class);
                                    intent.putExtra("data", jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(myWinnerList.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    Toast.makeText(MFPUserDetailsActivity.this,"winner",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);

                                } else {
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(myWinnerList);
                                    Intent intent = new Intent(context, LatestWinnerUserActivity.class);
                                    intent.putExtra("data", jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(myWinnerList.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("profilePic", profilePic);
                                    intent.putExtra("followingStatus", followingStatus);
                                    Toast.makeText(MFPUserDetailsActivity.this,"winner other",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);

                                }
                            }
                        });


                    } else {

                        Toast.makeText(MFPUserDetailsActivity.this, "No Records Found", Toast.LENGTH_SHORT)
                                .show();
                    }


                } else {
                    Toast.makeText(MFPUserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MFPUserDetailsActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();
        }
    }


    private class getMyParede extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        String paradeName;
        String startTime;
        String endTime;
        String image;
        String userId;
        String paradeId;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.getData(params[0]);


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {

                    pParade = JsonParser.getParadeResult(jsonObject);
                    int length = pParade.size();
                    if (length > 0) {
                        myParadeList = new ArrayList<ActiveParade>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++) {
                            ActiveParade activeParade = new ActiveParade();
                            JSONArray imageArray = pParade.get(j).imagePathJson;
                            imageObject = imageArray.getJSONObject(0);

                            paradeName = pParade.get(j).paradeName;
                            startTime = pParade.get(j).startTime;
                            endTime = pParade.get(j).endTime;
                            userId = pParade.get(j).userId;
                            paradeId = pParade.get(j).paradeId;
                            image = imageObject.getString("fileName");
                            activeParade.imagePathJson = imageArray;
                            activeParade.paradeName = paradeName;
                            activeParade.startTime = getDate(startTime);
                            activeParade.endTime = getDate(endTime);
                            activeParade.imagePath = image;
                            activeParade.userId = userId;
                            activeParade.paradeId = paradeId;
                            activeParade.userName=userName;
                            activeParade.profilePic=profilePic;
                            activeParade.tag = pParade.get(j).tag;
                            activeParade.aboutParade = pParade.get(j).aboutParade;
                            activeParade.sharedWith = pParade.get(j).sharedWith;
                            activeParade.groupId = pParade.get(j).groupId;
                            activeParade.followingStatus = followingStatus;
                            myParadeList.add(activeParade);
                        }


                        LatestparadesAdapter latestparadesAdapter = new LatestparadesAdapter(MFPUserDetailsActivity.this, myParadeList, false);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MFPUserDetailsActivity.this, 3);
                        recyclerView.setLayoutManager(gridLayoutManager);

                        //adding item decoration
                        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
                        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, spacing, true);
                        recyclerView.addItemDecoration(gridSpacingItemDecoration);
                        recyclerView.setAdapter(latestparadesAdapter);


                        latestparadesAdapter.SetOnItemClickListener(new LatestparadesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                user = Utils.getUserFromPreference(context);
                                String userid = myParadeList.get(position).userId;
                                if (userid.equalsIgnoreCase(user.id)) {

                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(myParadeList);
                                    Intent intent = new Intent(context, MyParadePage_prof1.class);
                                    intent.putExtra("data", jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(myParadeList.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    startActivity(intent);

                                } else {
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(myParadeList);

                                    Intent intent = new Intent(context, InboxPageProfileActivity.class);
                                    intent.putExtra("data", jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(myParadeList.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    startActivity(intent);

                                }
                            }
                        });

                    } else {

                        Toast.makeText(MFPUserDetailsActivity.this, "No Records Found", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(MFPUserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MFPUserDetailsActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();
        }
    }


    private class MFPUserDetails extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MFPUserDetailsActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {

                    Toast.makeText(MFPUserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();


                    JSONArray mfpuserdetails_itemsJsonarry = jsonObject.getJSONArray("userDetails");
                    for (int i = 0; i < mfpuserdetails_itemsJsonarry.length(); i++) {


                        JSONObject mfpuserdetails_itemsJSONObject = mfpuserdetails_itemsJsonarry.getJSONObject(i);
                        userName = mfpuserdetails_itemsJSONObject.getString("userName");
                        followerValue = mfpuserdetails_itemsJSONObject.getString("followers");
                        followingValue = mfpuserdetails_itemsJSONObject.getString("followings");
                        webValue = mfpuserdetails_itemsJSONObject.getString("website");
                        mailValue = mfpuserdetails_itemsJSONObject.getString("emailId");
                        bioValue = mfpuserdetails_itemsJSONObject.getString("bio");
                        profilePic = mfpuserdetails_itemsJSONObject.getString("profilePic");
                        followingStatus = mfpuserdetails_itemsJSONObject.getString("followingStatus");

                    }

                    User_name_mfp.setText(userName);
                    Follower_txt_value.setText(followerValue);
                    Following_txt_value.setText(followingValue);
                    Website_txt.setText(webValue);
                    Bio_txt.setText(bioValue);
                    Mail_txt.setText(mailValue);
                    if (profilePic != null && !profilePic.isEmpty())

                    {

                        Picasso.with(context)
                                .load(profilePic)
                                .placeholder(R.drawable.no_image)
                                .error(R.drawable.actionbar_profileicon)
                                .into(circleImageView_userImageMfp);

                    }
                    if (followingStatus != null && !followingStatus.isEmpty()) {
                        if (followingStatus.equals("0")) {
                            followTextView.setText("+Follow");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                            } else {
                                followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                            }

                            follow_clicked = false;
                        } else if (followingStatus.equals("1")) {
                            followTextView.setText("Following");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
                            } else {
                                followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
                            }

                            follow_clicked = true;
                        }
                    }
                    //circleImagView_userimagemfp
                    getMyParadeList();

                } else {
                    Toast.makeText(MFPUserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MFPUserDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();

        }

    }

    private void followUnfollowParadeRequest() {
        mConnectionCheck = new ConnectionCheck(MFPUserDetailsActivity.this);
        User user = Utils.getUserFromPreference(MFPUserDetailsActivity.this);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(MFPUserDetailsActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = "";
            if (!follow_clicked)
                url = ResourceManager.Followmtpuser();
            else
                url = ResourceManager.Unfollowmtpuser();

            url = url + "userId=" + user.id + "&followingId=" + followerId;
            new followUnfollowParade().execute(url);
        }
    }

    private class followUnfollowParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MFPUserDetailsActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {
                    Toast.makeText(MFPUserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (!follow_clicked) {
                        followTextView.setText("Following");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
                        } else {
                            followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
                        }

                        follow_clicked = true;
                    } else {
                        followTextView.setText("+Follow");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                        } else {
                            followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                        }

                        follow_clicked = false;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MFPUserDetailsActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    private String getDate(String OurDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

            Log.d("OurDate", OurDate);
        } catch (Exception e) {
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
