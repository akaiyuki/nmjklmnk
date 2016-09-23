package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.GroupAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.Group;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class GroupActivity extends ActionBarActivity {

    RecyclerView recyclerView;
    public ArrayList<Group> groupList;
    GroupAdapter groupAdapter;
    ArrayList<Boolean> selectedArrayList;
    TextView okTextView, backTextView;
    ImageView profileImageView, settingImage;
    boolean firstId;
    Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.groupRecyclerView);
        okTextView = (TextView) findViewById(R.id.OkTextView);
        backTextView = (TextView) findViewById(R.id.backTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        settingImage = (ImageView) findViewById(R.id.settingImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getGroupList();


        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();


            }
        });


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flag.profilePage = true;
                Intent intent = new Intent(context, FashionHomeActivity.class);
                startActivity(intent);
                finish();

            }
        });


        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedArrayList.size(); i++) {
                    if (selectedArrayList.get(i)) {
                        if (!firstId)
                            NewParadeActivity.groupId = groupList.get(i).groupId;
                        else
                            NewParadeActivity.groupId = NewParadeActivity.groupId + "," + groupList.get(i).groupId;

                        firstId = true;

                    }

                }
                firstId = false;
                System.out.println("Group id" + NewParadeActivity.groupId);
                finish();
            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void getGroupList() {


        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.getGroup();
            User user = Utils.getUserFromPreference(context);

            String newURL = url + "userId=" + user.id;

            Log.i("Get Group URL", newURL);

            new GetGroupsService().execute(newURL);

        }
    }


    private class GetGroupsService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String errorCode = "";
        String message = "";

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


            Log.i("Get Group Response", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {
                   /* Toast.makeText(getActivity(),
                            "Successfully update", Toast.LENGTH_SHORT)
                            .show();*/

                    //Create and set new adapter

                    groupList = new ArrayList<>();
                    selectedArrayList = new ArrayList<>();

                    JSONArray groupJSONArray = jsonObject.getJSONArray("getGroupDetails");

                    for (int i = 0; i < groupJSONArray.length(); i++) {

                        JSONObject groupJSONObj = groupJSONArray.getJSONObject(i);

                        String groupId = groupJSONObj.getString("groupId");
                        String userId = groupJSONObj.getString("userId");
                        String groupName = groupJSONObj.getString("groupName");
                        String totalMembers = groupJSONObj.getString("totalMembers");
                        String isAdmin = groupJSONObj.getString("isAdmin");

                        //String photo=contactJSONObj.getString("");

                        if (isAdmin.equals("1")) {
                            Group group = new Group();
                            group.userId = userId;
                            group.groupName = groupName;
                            group.groupId = groupId;
                            group.totalMembers = totalMembers;
                            group.isAdmin = isAdmin;

                            groupList.add(group);
                            selectedArrayList.add(false);
                        }

                    }
                    FashionHomeActivity.group_clicked = false;

                    if (groupList.size() > 0) {

                        groupAdapter = new GroupAdapter(GroupActivity.this, groupList, selectedArrayList);
                        recyclerView.setAdapter(groupAdapter);
                    } else {

                    }

                } else {

                    finish();
                    NewParadeActivity.newParadeActivity.finish();
                    FashionHomeActivity.group_clicked = true;
//                    FashionHomeActivity.viewPager.setCurrentItem(4);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(GroupActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();

        }


    }

}
