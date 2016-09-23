package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.MFPUserDetailsActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.SettingsActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.AlphabetListAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.ContactHelperAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.ContactsGroupAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.MFPContactAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.Group;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.fasionparade.fasionparadeApp.Views.Listview.ExampleContactListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";
    Context context;
    RelativeLayout contactLayout, groupLayout;
    RelativeLayout contactContentLayout, groupContentLayout;
    FrameLayout progress;
    TextView contactTextView, groupTextView;
    ImageView contactImageView, groupImageView,backImageView;

    Activity mActivity;

    String errorCode = "";
    String message = "";

    RecyclerView mfpContactsRecyclerView; //contactRecyclerView,
    private List<Contacts> contactsList;
    SimpleCursorAdapter mAdapter;
    MatrixCursor mMatrixCursor;

    ArrayList<String> nameList;
    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    RelativeLayout allLayout, mfpLayout, fbLayout;
    TextView mfpTextView, allTextView, fbTextView;
    ExampleContactListView list;


    //private AlphabetListAdapter adapter = new AlphabetListAdapter();
    private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private int indexListSize;
    MFPContactAdapter mfpContactAdapter;
    public View view;

    private AlphabetListAdapter adapter = new AlphabetListAdapter();


    //Gropup
    public RecyclerView groupRecyclerView;
    public RelativeLayout createGroupLayout;


    AlertDialog alertDialog;

    public ArrayList<Group> groupList;
    public ContactsGroupAdapter mfpContactsGroupAdapter;

    ArrayList<Contacts> myFriendsContactsList;


//    Facebook variables
    private CallbackManager callbackManager;
    private LoginButton loginButton;


    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // The contacts from the contacts content provider is stored in this cursor
        mMatrixCursor = new MatrixCursor(new String[]{"_id", "name", "photo", "details"});


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        context = container.getContext();
        contactLayout = (RelativeLayout) view.findViewById(R.id.contactLayout);
        groupLayout = (RelativeLayout) view.findViewById(R.id.groupLayout);
        progress = (FrameLayout) view.findViewById(R.id.avloadingIndicatorView);
        contactContentLayout = (RelativeLayout) view.findViewById(R.id.contactContentLayout);
        groupContentLayout = (RelativeLayout) view.findViewById(R.id.groupContentLayout);

        contactTextView = (TextView) view.findViewById(R.id.contactTextView);
        groupTextView = (TextView) view.findViewById(R.id.groupTextView);

        contactImageView = (ImageView) view.findViewById(R.id.contactImageView);
        groupImageView = (ImageView) view.findViewById(R.id.groupImageView);

        //     contactRecyclerView = (RecyclerView) view.findViewById(R.id.contactRecyclerView);
        mfpContactsRecyclerView = (RecyclerView) view.findViewById(R.id.mfpContactsRecyclerView);

        backImageView=(ImageView)view.findViewById(R.id.backImageView_addgroup);

        allLayout = (RelativeLayout) view.findViewById(R.id.allLayout);
        mfpLayout = (RelativeLayout) view.findViewById(R.id.mfpLayout);
        fbLayout = (RelativeLayout) view.findViewById(R.id.fbLayout);

        allTextView = (TextView) view.findViewById(R.id.allTextView);
        mfpTextView = (TextView) view.findViewById(R.id.mfpTextView);
        fbTextView = (TextView) view.findViewById(R.id.fbTextView);

        list = (ExampleContactListView) view.findViewById(R.id.list);


        mGestureDetector = new GestureDetector(getActivity(), new SideIndexGestureListener());

        this.view = view;

        //Group
        groupRecyclerView = (RecyclerView) view.findViewById(R.id.groupRecyclerView);
        groupRecyclerView.setTag("GROUP_RECYCLER_VIEW");

        createGroupLayout = (RelativeLayout) view.findViewById(R.id.createGroupLayout);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                List<Contacts> searchList = new ArrayList<Contacts>();

                float lastTouchX = list.getScroller().getLastTouchDownEventX();
                if (lastTouchX < 45 && lastTouchX > -1) {
                } else {

                }

                //Send the Invite Sms Message

                TextView inviteTextView = (TextView) v.findViewById(R.id.inviteTextView_new);
                TextView nicknameView = (TextView) v.findViewById(R.id.nickNameView);

                String username = nicknameView.getText().toString();

                if (inviteTextView.getText().toString().equals("Invite")) {
                    String smsBody = "Hey, Join me  on MFP , the latest fashion-based social voting App (download link),\n My MFP \n" + username;
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", smsBody);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(sendIntent);

                }
            }
        });





        allTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    allTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    allTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    mfpTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                    mfpTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    fbTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    fbTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                } else {
                    allTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    allTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    mfpTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                    mfpTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    fbTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    fbTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                }

                allLayout.setVisibility(View.VISIBLE);
                mfpLayout.setVisibility(View.GONE);
                fbLayout.setVisibility(View.GONE);

            }
        });

        mfpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    allTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                    allTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    mfpTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    mfpTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    fbTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    fbTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                } else {
                    allTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                    allTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    mfpTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    mfpTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    fbTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    fbTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                }

                allLayout.setVisibility(View.GONE);
                mfpLayout.setVisibility(View.VISIBLE);
                fbLayout.setVisibility(View.GONE);

                myFriends();


            }
        });

        fbTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    allTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                    allTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    mfpTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                    mfpTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    fbTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    fbTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                } else {
                    allTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                    allTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    mfpTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                    mfpTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    fbTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    fbTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                }

                allLayout.setVisibility(View.GONE);
                mfpLayout.setVisibility(View.GONE);
                fbLayout.setVisibility(View.VISIBLE);
            }
        });

        ImageView mImageContact = (ImageView) view.findViewById(R.id.profileImageView_addgroup);
        mImageContact.setVisibility(View.GONE);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FashionHomeActivity.class));
                getActivity().finish();
            }
        });


        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactContentLayout.setVisibility(View.VISIBLE);
                groupContentLayout.setVisibility(View.GONE);

                contactImageView.setImageResource(R.drawable.contact_icon);
                groupImageView.setImageResource(R.drawable.groupicon);

                ListViewContactsLoader listViewContactsLoader = new ListViewContactsLoader();
                listViewContactsLoader.execute();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    contactTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    contactLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                    groupLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                } else {
                    contactTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    contactLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                    groupLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                }


            }
        });

        groupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactContentLayout.setVisibility(View.GONE);
                groupContentLayout.setVisibility(View.VISIBLE);

                contactImageView.setImageResource(R.drawable.contacticon);
                groupImageView.setImageResource(R.drawable.group_icon);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    contactTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                    contactLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                    groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    groupLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                } else {
                    contactTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                    contactLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                    groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    groupLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                }

                getGroupList();


            }
        });


        createGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater layInflater = getActivity().getLayoutInflater();
                View dialogView = layInflater.inflate(R.layout.layout_dialog_create_group, null);
                dialogBuilder.setView(dialogView);

                TextView submitTextView = (TextView) dialogView.findViewById(R.id.submitTextView);
                TextView cancelTextView = (TextView) dialogView.findViewById(R.id.cancelTextView);
                final EditText groupNameEditText = (EditText) dialogView.findViewById(R.id.nameEditText);

                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(true);


                submitTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = groupNameEditText.getText().toString();

                        if (!name.equals("")) {

                            createGroup(name);

                        } else {

                            Toast.makeText(getActivity(), "Please enter Group Name", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        //   hideKeyboard();

                    }
                });


                alertDialog.show();

            }
        });



        /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        contactRecyclerView.setLayoutManager(mLayoutManager);*/

        LinearLayoutManager mfpLayoutManager = new LinearLayoutManager(getActivity());
        mfpContactsRecyclerView.setLayoutManager(mfpLayoutManager);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        groupRecyclerView.setLayoutManager(mLayoutManager1);

        stopAnim();





//        sample callback for fb login
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.button_fb);
        loginButton.setReadPermissions("email", "user_friends", "public_profile");
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                Log.i("name user", String.valueOf(loginResult.getAccessToken().getUserId()));
                final String fbAccesstoken = loginResult.getAccessToken().getToken();
                final String fbId = loginResult.getAccessToken().getUserId();


                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("response: ", response + "");
                                try {

                                    Profile profile = Profile.getCurrentProfile();
//                                    String firstName = profile.getFirstName();
//                                    String lastName = profile.getLastName();

                                    String email = object.optString("email");

                                    AccessToken accessToken = loginResult.getAccessToken();
                                    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
                                        @Override
                                        protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

                                        }
                                    };
                                    accessTokenTracker.startTracking();

                                    ProfileTracker profileTracker = new ProfileTracker() {
                                        @Override
                                        protected void onCurrentProfileChanged(Profile profile, Profile profile1) {

                                        }
                                    };
                                    profileTracker.startTracking();

                                    if (profile != null) {
                                        //get data here
                                        Log.e("firstnamefb", profile.getFirstName()+" "+email);

                                        /* log out facebook if access token is not null */
                                        if(accessToken != null){
                                            LoginManager.getInstance().logOut();
                                        }

//                                        fb login api

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    Log.e("requesterror", e.getMessage());
                                }
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.i("login user", "canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("login user", exception.toString());
            }
        });

//        ends fb login setup



        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getContactList();
        // Creating an AsyncTask object to retrieve and load listview with contacts
        ListViewContactsLoader listViewContactsLoader = new ListViewContactsLoader();

        // Starting the AsyncTask process to retrieve and load listview with contacts
        if (FashionHomeActivity.group_clicked) {
            contactContentLayout.setVisibility(View.GONE);
            groupContentLayout.setVisibility(View.VISIBLE);

            contactImageView.setImageResource(R.drawable.contacticon);

            groupImageView.setImageResource(R.drawable.group_icon);

            FashionHomeActivity.group_clicked = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                contactTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContactText));
                contactLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorContactLayout));
                groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                groupLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
            } else {
                contactTextView.setTextColor(getActivity().getResources().getColor(R.color.colorContactText));
                contactLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorContactLayout));
                groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                groupLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
            }

            getGroupList();
        }
        else {
            listViewContactsLoader.execute();
        }
    }


    /**
     * An AsyncTask class to retrieve and load listview with contacts
     */
    private class ListViewContactsLoader extends AsyncTask<Void, Void, Cursor> {

        private  String[] PROJECTION;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();

            PROJECTION = new String[] {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DATA1,
                    ContactsContract.CommonDataKinds.Phone.DATA2,
                    ContactsContract.CommonDataKinds.Phone.DATA15,


            };

        }

        @Override
        protected Cursor doInBackground(Void... params) {
            contactsList = new ArrayList<Contacts>();
            Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
            // Querying the table ContactsContract.Contacts to retrieve all the contacts
            Cursor contactsCursor = context.getContentResolver().query(contactsUri, null, null, null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC ");


            if (contactsCursor.moveToFirst()) {
                do {
                    long contactId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID"));


                    Uri dataUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor dataCursor = context.getContentResolver().query(dataUri,
                            PROJECTION,
                            ContactsContract.Data.CONTACT_ID + "=" + contactId,
                            null, null);

                    //       Cursor dataCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = '" + idValue + "'", null, null);

                    String displayName = "";
                    String nickName = "";
                    String homePhone = "";
                    String mobilePhone = "";
                    String workPhone = "";
                    String photoPath = "" + R.drawable.blank;
                    byte[] photoByte = null;


                    if (dataCursor.moveToFirst()) {
                        Contacts contactsData = new Contacts();
                        // Getting Display Name
                        displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                        do {

                            // Getting NickName
                            if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
                                nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));

                            // Getting Phone numbers
                            if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                                switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                        homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                        workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                        break;
                                }
                            }

                            // Getting Photo
                            if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                                photoByte = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));

                                if (photoByte != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
                                    File cacheDirectory = null;
                                    File tmpFile = null;

                                    // The FileOutputStream to the temporary file
                                    try {
                                        // Getting Caching directory
                                        cacheDirectory = getActivity().getBaseContext().getCacheDir();

                                        // Temporary file to store the contact image
                                        tmpFile = new File(cacheDirectory.getPath() + "/wpta_" + contactId + ".png");

                                        FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                                        // Writing the bitmap to the temporary file as png file
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);

                                        // Flush the FileOutputStream
                                        fOutStream.flush();

                                        //Close the FileOutputStream
                                        fOutStream.close();

                                        photoPath = tmpFile.getPath();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            }

                        } while (dataCursor.moveToNext());

                        String details = "";

                        // Concatenating various information to single string
                        if (homePhone != null && !homePhone.equals("") || mobilePhone != null && !mobilePhone.equals("") || workPhone != null && !workPhone.equals("")) {
                            //   details = "Phone : " +mobilePhone + "\n";
                            details = mobilePhone;
                            contactsData.name = displayName;
                            contactsData.number = details;
                            contactsData.photo = photoPath;
                            contactsList.add(contactsData);
                        }


                    }
                    dataCursor.close();
                } while (contactsCursor.moveToNext());


                nameList = new ArrayList<>();

                for (int i = 0; i < nameList.size(); i++) {

                    nameList.add(contactsList.get(i).name);

                }

            }
            contactsCursor.close();


            return mMatrixCursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {

            stopAnim();
            //   ContactUsListAdapter contactUsListAdapter = new ContactUsListAdapter(getActivity(), contactsList);

            ContactHelperAdapter adapter = new ContactHelperAdapter(getActivity(), R.layout.example_contact_item, contactsList, myFriendsContactsList);
            list.setAdapter(adapter);
            list.setFastScrollEnabled(true);

            addFriends();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void startAnim() {
        progress.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        progress.setVisibility(View.GONE);
    }


    private void addFriends() {
        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.addfriends();


            User user = Utils.getUserFromPreference(context);

            //Getting phone Numer Without Space and Special Charachters
            StringBuilder phoneNumberStringBuilder = new StringBuilder();
            for (int i = 0; i < contactsList.size(); i++) {

                String tempPhoneNumber = contactsList.get(i).number;

                String woSpace = tempPhoneNumber.replace(" ", "");

                String woSpecialChar = woSpace.replaceAll("[\\-\\+\\.\\^:,]", "");

                phoneNumberStringBuilder.append(woSpecialChar + ",");
            }

            Log.i("Ph No StringBuilder", phoneNumberStringBuilder.toString());


            String newURL = url + "userId=" + user.id + "&addingType=2" + "&friendsList=" + phoneNumberStringBuilder.toString();
            ;


            Log.i("Add Friend URL", newURL);

            new AddFriendsService().execute(newURL);


        }
    }

    private class AddFriendsService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

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


            Log.i("Add Friends Response", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {
                    //  Toast.makeText(getActivity(), "Successfully update", Toast.LENGTH_SHORT).show();


                } else {

                    // Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }

                myFriends();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();

        }
    }


    private void myFriends() {
        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.myfriends();
            User user = Utils.getUserFromPreference(context);

            String newURL = url + "userId=" + user.id;

            Log.i("My Friend URL", newURL);

            new MyFriendsService().execute(newURL);

        }
    }


    private class MyFriendsService extends AsyncTask<String, Void, String> {
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


            Log.i("My Friends Response", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                errorCode = jsonObject.getString("errorCode");

                message = jsonObject.getString("message");

                if (errorCode.equals("200")) {
                    // Toast.makeText(getActivity(), "Successfully update", Toast.LENGTH_SHORT).show();

                    //Create and set new adapter
                    myFriendsContactsList = new ArrayList<>();
                    JSONArray friendsHJSONArray = jsonObject.getJSONArray("friendsList");

                    for (int i = 0; i < friendsHJSONArray.length(); i++) {

                        JSONObject contactJSONObj = friendsHJSONArray.getJSONObject(i);
                        Contacts contacts = new Contacts();
                        contacts.setId(contactJSONObj.getString("userId"));
                        contacts.setName(contactJSONObj.getString("userName"));
                        contacts.setPhoto(contactJSONObj.getString("profilePic"));
                        contacts.setCountrycode(contactJSONObj.getString("countryCode"));
                        contacts.setNumber(contactJSONObj.getString("phoneNumber"));
                        myFriendsContactsList.add(contacts);
                    }

                    mfpContactAdapter = new MFPContactAdapter(getActivity(), myFriendsContactsList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                    mfpContactsRecyclerView.setLayoutManager(gridLayoutManager);

                    //adding item decoration
                    int spacing = getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
                    GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, spacing, true);
                    mfpContactsRecyclerView.addItemDecoration(gridSpacingItemDecoration);
                    mfpContactsRecyclerView.setAdapter(mfpContactAdapter);

                    ContactHelperAdapter adapter = new ContactHelperAdapter(getActivity(), R.layout.example_contact_item, contactsList, myFriendsContactsList);
                    list.setAdapter(adapter);
                    list.setFastScrollEnabled(true);

                    mfpContactAdapter.SetOnItemClickListener(new MFPContactAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent i = new Intent(getActivity(), MFPUserDetailsActivity.class);
                            i.putExtra("followerid", String.valueOf(myFriendsContactsList.get(position).getId()));
                            i.putExtra("followername", String.valueOf(myFriendsContactsList.get(position).getName()));
                            Log.i("followerid", String.valueOf(myFriendsContactsList.get(position).getId()));
                            startActivity(i);
                        }
                    });


                } else {

                    // Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
                    ContactHelperAdapter adapter = new ContactHelperAdapter(getActivity(), R.layout.example_contact_item, contactsList, myFriendsContactsList);
                    list.setAdapter(adapter);
                    list.setFastScrollEnabled(true);


                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();


            }
            pDialog.cancel();

        }


    }


    private void getGroupList() {


        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet()) {
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

                    //Create and set new adapter

                    groupList = new ArrayList<>();

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
                        }

                    }


                    if (groupList.size() > 0) {

                        mfpContactsGroupAdapter = new ContactsGroupAdapter((BaseActivity) getActivity(), groupList, myFriendsContactsList);
                        groupRecyclerView.setAdapter(mfpContactsGroupAdapter);
                    } else {
                        //show no group text
                    }

                } else {

                    Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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


    private void createGroup(final String groupName) {

        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {


            String createUrl = ResourceManager.createGroup();
            User user = Utils.getUserFromPreference(context);

            final String userId = "" + user.id;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, createUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Add Group URL", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                errorCode = jsonObject.getString("errorCode");

                                message = jsonObject.getString("message");


                                if (errorCode.equals("200")) {
                                    //Create and set new adapter
                                    if (alertDialog != null)
                                        alertDialog.dismiss();


//                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                    JSONArray groupJSONArray = jsonObject.getJSONArray("groupDetails");

                                    for (int i = 0; i < groupJSONArray.length(); i++)

                                    {

                                        JSONObject groupJSONObj = groupJSONArray.getJSONObject(i);

                                        String groupId = groupJSONObj.getString("groupId");
                                        String groupName = groupJSONObj.getString("groupName");
                                        String userId = groupJSONObj.getString("userId");

                                        Group group = new Group();
                                        group.groupName = groupName;
                                        group.groupId = groupId;
                                        group.userId = userId;

                                        group.isAdmin = "1";
                                        group.totalMembers = "1";

                                        if (groupList == null)
                                            groupList = new ArrayList<>();


                                        groupList.add(0, group);


                                    }

                                    if (mfpContactsGroupAdapter != null) {

                                        mfpContactsGroupAdapter.notifyDataSetChanged();

                                    } else {

                                        mfpContactsGroupAdapter = new ContactsGroupAdapter((BaseActivity) getActivity(), groupList, myFriendsContactsList);
                                        groupRecyclerView.setAdapter(mfpContactsGroupAdapter);
                                    }

                                } else {

                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", userId);
                    params.put("groupName", groupName);
                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
//            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//            requestQueue.add(stringRequest);

        }
    }


    class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            if (sideIndexX >= 0 && sideIndexY >= 0) {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public void displayListItem() {


        //     listViewWordList.setAdapter(adapter);

        LinearLayout sideIndex = (LinearLayout) view.findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        if (itemPosition >= 0 && itemPosition < alphabet.size()) {
            Object[] indexItem = alphabet.get(itemPosition);

            Log.i("Alphabet", alphabet.get(itemPosition) + "   " + itemPosition);
            Log.i("Index ITem", indexItem[0].toString() + "   " + itemPosition);
            Log.i("Section ITem", sections.get(indexItem[0]).toString() + "");

            int subItemPosition = sections.get(indexItem[0]);

            //ListView listView = (ListView) findViewById(android.R.id.list);
            list.setSelection(subItemPosition);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity; //
    }


}
