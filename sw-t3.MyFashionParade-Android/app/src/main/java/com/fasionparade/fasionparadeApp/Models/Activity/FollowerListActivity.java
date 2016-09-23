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
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Models.Adapters.FollowingAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.FollowerAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowerListActivity extends AppCompatActivity {

    private ImageView backtoscreen;
    LinearLayout activeParadeLayout, winnerLayout, linearlayout_chat_mfp, following_mfp_layout;
    TextView groupTextView, activeParadeTextView;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    Context context;
    RecyclerView followerRecyclerView, followingRecyclerView;
    User user;
    ArrayList<Contacts> followersList;

    ArrayList<Contacts> followingList;

    FollowerAdapter followerAdapter;

    FollowingAdapter followingAdapter;

    static String followerId = "";
    static String followers = "";
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followes_followerlist);


        String followers = "";
        context = this;
        followerId = getIntent().getStringExtra("friendid");
        status = getIntent().getStringExtra("status");


        backtoscreen = (ImageView) findViewById(R.id.backImageView_followes_followers);
        activeParadeLayout = (LinearLayout) findViewById(R.id.Layout_follows);
        following_mfp_layout = (LinearLayout) findViewById(R.id.Layout_following);
        groupTextView = (TextView) findViewById(R.id.groupTextView_following);
        activeParadeTextView = (TextView) findViewById(R.id.activefollows);
        followerRecyclerView = (RecyclerView) findViewById(R.id.Follower_RecyclerView);
        followingRecyclerView = (RecyclerView) findViewById(R.id.Following_RecyclerView);

        if (status.equals("followers")) {

            followingRecyclerView.setVisibility(View.GONE);
            followerRecyclerView.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                activeParadeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                following_mfp_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                groupTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGroupText));
            } else {
                activeParadeLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                activeParadeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
                following_mfp_layout.setBackgroundColor(getResources().getColor(R.color.colorWhiteText));
                groupTextView.setTextColor(getResources().getColor(R.color.colorGroupText));
            }

        } else {

            followingRecyclerView.setVisibility(View.VISIBLE);
            followerRecyclerView.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                activeParadeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGroupText));
                following_mfp_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                groupTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
            } else {
                activeParadeLayout.setBackgroundColor(getResources().getColor(R.color.colorWhiteText));
                activeParadeTextView.setTextColor(getResources().getColor(R.color.colorGroupText));
                following_mfp_layout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                groupTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
            }
        }

        followerParade();
        activeParadeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                followingRecyclerView.setVisibility(View.GONE);
                followerRecyclerView.setVisibility(View.VISIBLE);
                followerParade();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                    following_mfp_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteBackground));
                    groupTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGroupText));
                } else {
                    activeParadeLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
                    following_mfp_layout.setBackgroundColor(getResources().getColor(R.color.colorWhiteBackground));
                    groupTextView.setTextColor(getResources().getColor(R.color.colorGroupText));
                }

            }
        });


        following_mfp_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                followersList.clear();

                followingRecyclerView.setVisibility(View.VISIBLE);
                followerRecyclerView.setVisibility(View.GONE);
                followerParade();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteBackground));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGroupText));
                    following_mfp_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                    groupTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                } else {
                    activeParadeLayout.setBackgroundColor(getResources().getColor(R.color.colorWhiteBackground));
                    activeParadeTextView.setTextColor(getResources().getColor(R.color.colorGroupText));
                    following_mfp_layout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                    groupTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
                }


            }
        });
        backtoscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });

    }

    private void followerParade() {
        mConnectionCheck = new ConnectionCheck(context);

        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.Friendfollower();
            user = Utils.getUserFromPreference(context);
            url = url + "userId=" + user.id + "&friendId=" + followerId;
            System.out.println("Friend followers status " + url);
            new Followerlist().execute(url);

        }
    }


    private class Followerlist extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
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


            Log.i("My Friends Response", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {

                    //Create and set new adapter
                    followersList = new ArrayList<>();
                    followingList = new ArrayList<>();
                    JSONArray friendsHJSONArray = jsonObject.getJSONArray("followersDetails");
                    for (int i = 0; i < friendsHJSONArray.length(); i++) {

                        JSONObject contactJSONObj = friendsHJSONArray.getJSONObject(i);
                        Contacts contacts = new Contacts();
                        followers = contactJSONObj.getString("fType");
                        if (followers.equals("1")) {
                            contacts.setId(contactJSONObj.getString("userId"));
                            contacts.setName(contactJSONObj.getString("name"));
                            contacts.setNumber(contactJSONObj.getString("phoneNumber"));
                            contacts.setPhoto(contactJSONObj.getString("profilePic"));
                            contacts.setFollowingstatus(contactJSONObj.getString("followingStatus"));
                            followersList.add(contacts);

                        } else if (followers.equals("2")) {
                            contacts.setId(contactJSONObj.getString("userId"));
                            contacts.setName(contactJSONObj.getString("name"));
                            contacts.setNumber(contactJSONObj.getString("phoneNumber"));
                            contacts.setPhoto(contactJSONObj.getString("profilePic"));
                            contacts.setFollowingstatus(contactJSONObj.getString("followingStatus"));
                            followingList.add(contacts);

                        }
                    }


                    followerAdapter = new FollowerAdapter(FollowerListActivity.this, followersList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(FollowerListActivity.this, 1);
                    followerRecyclerView.setLayoutManager(gridLayoutManager);

//                    //adding item decoration
                    int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
                    GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, spacing, true);
                    followerRecyclerView.addItemDecoration(gridSpacingItemDecoration);
                    followerRecyclerView.setAdapter(followerAdapter);


                    followingAdapter = new FollowingAdapter(FollowerListActivity.this, followingList);
                    GridLayoutManager gridLayoutManagerfoll = new GridLayoutManager(FollowerListActivity.this, 1);
                    followingRecyclerView.setLayoutManager(gridLayoutManagerfoll);


//                    //adding item decoration
                    int spacings = getResources().getDimensionPixelSize(R.dimen.spacing);
                    GridSpacingItemDecoration gridSpacingItemDecorations = new GridSpacingItemDecoration(1, spacings, true);
                    followingRecyclerView.addItemDecoration(gridSpacingItemDecorations);
                    followingRecyclerView.setAdapter(followingAdapter);


                    followingAdapter.SetOnItemClickListener(new FollowingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent i = new Intent(FollowerListActivity.this, MFPUserDetailsActivity.class);
                            i.putExtra("followerid", followingList.get(position).getId());
                            startActivity(i);

                        }
                    });


                    followerAdapter.SetOnItemClickListener(new FollowerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent i = new Intent(FollowerListActivity.this, MFPUserDetailsActivity.class);
                            i.putExtra("followerid", followersList.get(position).getId());
                            startActivity(i);
                        }
                    });


                } else {

                    Toast.makeText(FollowerListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(FollowerListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();

        }


    }
}
