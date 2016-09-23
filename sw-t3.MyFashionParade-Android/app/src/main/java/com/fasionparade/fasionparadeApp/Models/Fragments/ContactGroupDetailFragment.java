package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.ContactGroupMembersAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.Group;
import com.fasionparade.fasionparadeApp.Functions.Object.GroupMember;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactGroupDetailFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match

    private static final String GROUP = "group";
    private static final String GROUP_LIST = "GROUP_LIST";
    private static final String POSITION = "POSITION";
    private static final String MY_CONTACT_ARRAYLIST="MY_CONTACT_ARRAYLIST";

    // TODO: Rename and change types of parameters
    private String groupName;
    private String groupId;

    TextView renameTextView;

    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";
    EditText groupNameEditText;
    RelativeLayout addContactsLayout, deleteGroupLayout;
    RecyclerView contactsRecyclerView;
    ImageView backImageView;
    ConnectionCheck connectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    Group group;
    AlertDialog alertDialog;
    TextClicked mCallback;
    ProgressDialog pDialog;
    ArrayList<Group> groupArrayList;
    int position;
    ArrayList<Contacts> myFriendsContactsList;

    ImageView profileImageView;


    public interface TextClicked {
        public void sendText(String text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TextClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }


    public ContactGroupDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactGroupDetailFragment.
     * @ param1 Parameter 1.
     * @ param2 Parameter 2.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactGroupDetailFragment newInstance(Group group, ArrayList<Group> groupList, int position,    ArrayList<Contacts> myFriendsContactsList) {
        ContactGroupDetailFragment fragment = new ContactGroupDetailFragment();
        Bundle args = new Bundle();
        //   args.putString(ARG_PARAM1, contactsFragment);
        //    args.putString(ARG_PARAM2, group);
        args.putParcelable(GROUP, group);
        args.putParcelableArrayList(GROUP_LIST, groupList);
        args.putInt(POSITION, position);
        args.putParcelableArrayList(MY_CONTACT_ARRAYLIST,myFriendsContactsList);

        fragment.setArguments(args);
        return fragment;
    }

    public boolean inEditMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            //      groupName = getArguments().getString(ARG_PARAM1);
            //      groupId = getArguments().getString(ARG_PARAM2);
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_group_detail, container, false);

        contactsRecyclerView = (RecyclerView) view.findViewById(R.id.contactRecyclerView);
        groupNameEditText = (EditText) view.findViewById(R.id.groupNameEditText);
        renameTextView = (TextView) view.findViewById(R.id.renameTextView);

        addContactsLayout = (RelativeLayout) view.findViewById(R.id.addContactsLayout);
        deleteGroupLayout = (RelativeLayout) view.findViewById(R.id.deleteGroupLayout);

        backImageView = (ImageView) view.findViewById(R.id.backImageView_addgroup);

        groupNameEditText.setEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            groupNameEditText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlackText));
        } else {
            groupNameEditText.setTextColor(getActivity().getResources().getColor(R.color.colorBlackText));
        }

        groupNameEditText.setText(groupName);

        profileImageView=(ImageView)view.findViewById(R.id.profileImageView_addgroup);


        renameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inEditMode = !inEditMode;

                if (inEditMode) {

                    renameTextView.setText("Done");
                    groupNameEditText.setEnabled(true);

                    groupNameEditText.requestFocus();

                    groupNameEditText.setSelection(groupNameEditText.getText().length());

                    InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                } else {
                    renameTextView.setText("Rename");
                    groupNameEditText.setEnabled(false);

                    groupNameEditText.clearFocus();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        groupNameEditText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlackText));
                    } else {
                        groupNameEditText.setTextColor(getActivity().getResources().getColor(R.color.colorBlackText));
                    }

                    String name = groupNameEditText.getText().toString();

                    if (!name.equals("")) {
                        updateGroupName(name);
                    } else {


                    }

                }

            }
        });


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("back", "Clicked");


//                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }
        });


        deleteGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteGroup();

            }
        });

        addContactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                MEngine.switchFragment((BaseActivity) getActivity(), AddGroupFragment.newInstance(group, groupArrayList, position, myFriendsContactsList), ((BaseActivity) getActivity()).getFrameLayout());
            }
        });


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().finish();

            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        contactsRecyclerView.setLayoutManager(linearLayoutManager);

        getGroupCompleteDetails();


        return view;
    }


    private void updateGroupName(final String groupName)
    {

        connectionCheck = new ConnectionCheck(getActivity());
        if (!connectionCheck.isConnectingToInternet())
        {
            alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
        } else
        {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            String url = ResourceManager.updateGroupName();
            final User user = Utils.getUserFromPreference(getActivity());

            StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            Log.i("Update Group Name URL", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String errorCode;
                                String message;
                                errorCode = jsonObject.getString("errorCode");

                                if (errorCode.equals("200"))
                                {

                                    group.groupName = groupName;
                                    message = jsonObject.getString("message");

                                    //Create and set new adapter
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                    pDialog.cancel();

                                } else
                                {

                                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams()
                {
                    Map<String,String> params = new HashMap<>();
                    params.put("userId",user.id);
                    params.put("groupName",groupName);
                    params.put("groupId",groupId);

                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(stringRequest,tag_json_obj);

        }
    }




    //Delete Group
    private void deleteGroup() {

        connectionCheck = new ConnectionCheck(getActivity());
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.deleteGroup();
            User user = Utils.getUserFromPreference(getActivity());

            String newURL = url + "userId=" + user.id + "&groupId=" + groupId;

            Log.i("Del Group URL", newURL);

            new DeleteGroupService().execute(newURL);

        }
    }


    private class DeleteGroupService extends AsyncTask<String, Void, String> {
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

            Log.i("Del Group Response", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {

                    // group=null;
                    groupArrayList.remove(position);


                    message = jsonObject.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    //   mCallback.sendText("");
                    getActivity().getSupportFragmentManager().popBackStack();


                } else {

                    Toast.makeText(getActivity(),
                            "Something Wrong", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();

        }

    }



    //Delete Group
    private void getGroupCompleteDetails()
    {

        connectionCheck = new ConnectionCheck(getActivity());
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.groupcompletedata();
            User user = Utils.getUserFromPreference(getActivity());

            String newURL = url + "userId=" + user.id + "&groupId=" + groupId;

            Log.i("Get Group Comp Det URL", newURL);

            new GetGroupCompleteDetailsService().execute(newURL);

        }
    }


    private class GetGroupCompleteDetailsService extends AsyncTask<String, Void, String> {
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

            Log.i("Get Group Comp Det Resp", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {

                    //         message=jsonObject.getString("message");
                    //         Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    //show List


                    JSONArray groupDetailsJSONArray = jsonObject.getJSONArray("groupDetails");

                    JSONObject groupJSONObj = groupDetailsJSONArray.getJSONObject(0);

                    String adminID=groupJSONObj.getString("adminId");


                    JSONArray membersJSONArray = groupJSONObj.getJSONArray("memberDetails");

                    ArrayList<GroupMember> groupMembers = new ArrayList<>();

                    for (int i = 0; i < membersJSONArray.length(); i++) {

                        JSONObject memberJSONObj = membersJSONArray.getJSONObject(i);


                        GroupMember groupMember=new GroupMember();

                        groupMember.memberId=memberJSONObj.getString("memberId");
                        groupMember.userName=memberJSONObj.getString("userName");
                        groupMember.loginType=memberJSONObj.getString("loginType");
                        groupMember.deviceType=memberJSONObj.getString("deviceType");
                        groupMember.countryCode=memberJSONObj.getString("countryCode");
                        groupMember.phoneNumber=memberJSONObj.getString("phoneNumber");

                        groupMember.dob=memberJSONObj.getString("dob");
                        groupMember.name=memberJSONObj.getString("name");

                        groupMembers.add(groupMember);

                    }


                    ContactGroupMembersAdapter contactGroupMembersAdapter=new ContactGroupMembersAdapter(getActivity(),groupMembers,groupId,adminID);
                    contactsRecyclerView.setAdapter(contactGroupMembersAdapter);


                } else {

                    Toast.makeText(getActivity(),
                            "Something Wrong", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();

        }

    }


    public void someMethod() {
        mCallback.sendText("YOUR TEXT");
    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mCallback.sendText("");

    }









}
