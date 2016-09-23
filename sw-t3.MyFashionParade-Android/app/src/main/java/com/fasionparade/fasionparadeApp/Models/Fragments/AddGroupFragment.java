package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.AddGroupMembersAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.Group;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vishnu on 8/4/2016.
 */
public class AddGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GROUP = "group";
    private static final String GROUP_LIST = "GROUP_LIST";
    private static final String POSITION = "POSITION";
    private static final String MY_CONTACT_ARRAYLIST="MY_CONTACT_ARRAYLIST";

    // TODO: Rename and change types of parameters
    private String groupName;
    private String groupId;

    private TextView Groupname,AddGroupname,Empty_textview;
    RecyclerView mfpMembersRecyclerView;

    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();

    Group group;
    ArrayList<Group> groupArrayList;
    int position;
    ArrayList<Contacts> myFriendsContactsList = new ArrayList<>();
    ImageView profileImageView,Back_button;




    // TODO: Rename and change types and number of parameters
    public static AddGroupFragment newInstance(Group group, ArrayList<Group> groupList, int position,    ArrayList<Contacts> myFriendsContactsList) {
        AddGroupFragment fragment = new AddGroupFragment();
        Bundle args = new Bundle();
        //   args.putString(ARG_PARAM1, contactsFragment);
        //    args.putString(ARG_PARAM2, group);
        args.putParcelable(GROUP, group);
        args.putParcelableArrayList(GROUP_LIST, groupList);
        args.putInt(POSITION, position);
        args.putParcelableArrayList(MY_CONTACT_ARRAYLIST, myFriendsContactsList);

        fragment.setArguments(args);
        return fragment;
    }

    public AddGroupFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            group = getArguments().getParcelable(GROUP);
            groupArrayList = getArguments().getParcelableArrayList(GROUP_LIST);
            position = getArguments().getInt(POSITION);

            myFriendsContactsList=getArguments().getParcelableArrayList(MY_CONTACT_ARRAYLIST);

            groupName = group.groupName;
            groupId = group.groupId;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_add_group, container, false);
        Back_button = (ImageView)view.findViewById(R.id.backImageView_addgroup);
        profileImageView=(ImageView)view.findViewById(R.id.profileImageView_addgroup);
        mfpMembersRecyclerView = (RecyclerView) view.findViewById(R.id.mfpMembersRecyclerView);
        AddGroupname = (TextView)view.findViewById(R.id.textView_addtogroup);
        Groupname =(TextView)view.findViewById(R.id.textView_groupname);
        Empty_textview =(TextView)view.findViewById(R.id.textView_recycleviewempty);

        Empty_textview.setVisibility(View.GONE);

        Groupname.setText(groupName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mfpMembersRecyclerView.setLayoutManager(linearLayoutManager);

        ///By default everything is deselected
        final ArrayList<Boolean> selectedArrayList = new ArrayList<Boolean>();

        if(myFriendsContactsList != null)
        {
            for (int i = 0; i < myFriendsContactsList.size(); i++)
            {
                selectedArrayList.add(false);
            }


            AddGroupMembersAdapter addGroupMembersAdapter = new AddGroupMembersAdapter(getActivity(), myFriendsContactsList, selectedArrayList);
            mfpMembersRecyclerView.setAdapter(addGroupMembersAdapter);

        }

        else
        {
            Empty_textview.setVisibility(View.VISIBLE);
            mfpMembersRecyclerView.setVisibility(View.GONE);
        }




        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backOnCall();

            }
        });


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().finish();
            }
        });


        AddGroupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                StringBuilder membersSeperatedByComma = new StringBuilder();
                boolean enteredOnce = false;

                for (int i = 0; i < selectedArrayList.size(); i++) {

                    if (selectedArrayList.get(i) == true) {

                        membersSeperatedByComma.append(myFriendsContactsList.get(i).id + ",");

                        enteredOnce = true;
                    }
                }

                String finalString = membersSeperatedByComma.toString();

                //removing last comma
                if (enteredOnce) {

                    if (finalString != null && finalString.length() > 0 && finalString.charAt(finalString.length() - 1) == ',') {
                        finalString = finalString.substring(0, finalString.length() - 1);
                    }

                }


                if (enteredOnce) {

                    addListOfMembersToGroup(finalString);

                } else
                {

                    Toast.makeText(getActivity(), "Please select atleast one friend.", Toast.LENGTH_SHORT).show();
                }




            }
        });

        return view;

    }


    public void addListOfMembersToGroup(String membersSeperatedByComma){

        cd = new ConnectionCheck(getActivity());
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.addgroupmembers();
            User user = Utils.getUserFromPreference(getActivity());

            String newURL = url + "userIds=" + membersSeperatedByComma + "&groupId=" + groupId +"&userId=" +user.id;

            Log.i("Add Mem To Group URL", newURL);

            new AddListOfMembersToGroupService().execute(newURL);

        }
    }



    private class AddListOfMembersToGroupService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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

            Log.i("Add Mem To Group Resp", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {

                    message=jsonObject.getString("message");

                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    backOnCall();


                } else
                {

                    message=jsonObject.getString("message");
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();

        }

    }



    public  void backOnCall()

    {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().onBackPressed();
        }


    }


}
