package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.ContactsAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ContactsActivity extends ActionBarActivity {

    RecyclerView recyclerView;
    ArrayList<Contacts> contactList;
    ContactsAdapter contactAdapter;
    ArrayList<Boolean> selectedArrayList;
    TextView OkTextView,backTextView;
    ImageView profileImageView,settingImage;
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
        OkTextView = (TextView) findViewById(R.id.OkTextView);
        backTextView = (TextView) findViewById(R.id.backTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        settingImage= (ImageView) findViewById(R.id.settingImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ContactsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myFriends();

        OkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedArrayList.size(); i++) {
                    if (selectedArrayList.get(i)) {
                        if (!firstId)
                            NewParadeActivity.groupId = contactList.get(i).getId();
                        else
                            NewParadeActivity.groupId = NewParadeActivity.groupId + "," + contactList.get(i).getId();

                        firstId = true;

                    }

                }
                firstId = false;
                System.out.println("Contacts id" + NewParadeActivity.groupId);
                finish();
            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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




    }


    private void myFriends()
    {
        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.myfriends();
            User user = Utils.getUserFromPreference(context);

            String newURL = url + "userId="+user.id;

            Log.i("My Friend URL", newURL);

            new MyFriendsService().execute(newURL);

        }
    }


    private class MyFriendsService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String errorCode="";
        String message="";
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


            Log.i("My Friends Response", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                errorCode = jsonObject.getString("errorCode");

                message = jsonObject.getString("message");

                if (errorCode.equals("200"))
                {
                    // Toast.makeText(getActivity(), "Successfully update", Toast.LENGTH_SHORT).show();

                    //Create and set new adapter
                    contactList=new ArrayList<>();
                    selectedArrayList = new ArrayList<>();
                    JSONArray friendsHJSONArray = jsonObject.getJSONArray("friendsList");

                    for(int i=0;i<friendsHJSONArray.length();i++)
                    {

                        JSONObject contactJSONObj=friendsHJSONArray.getJSONObject(i);
                        Contacts contacts=new Contacts();
                        contacts.setId(contactJSONObj.getString("userId"));
                        contacts.setName(contactJSONObj.getString("userName"));
                        contacts.setPhoto(contactJSONObj.getString("profilePic"));
                        contacts.setCountrycode(contactJSONObj.getString("countryCode"));
                        contacts.setNumber(contactJSONObj.getString("phoneNumber"));
                        contactList.add(contacts);
                        selectedArrayList.add(false);
                    }

                    contactAdapter=new ContactsAdapter(ContactsActivity.this,contactList,selectedArrayList);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(context,1);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(contactAdapter);


                }

                else {

                    finish();
                    NewParadeActivity.newParadeActivity.finish();
//                    FashionHomeActivity.viewPager.setCurrentItem(4);




                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();



            }
            pDialog.cancel();

        }


    }



}
