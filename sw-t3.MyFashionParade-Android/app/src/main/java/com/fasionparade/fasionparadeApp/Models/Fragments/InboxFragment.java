package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.ChatDetailActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.SettingsActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.ChatListAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.InboxListAdapter;
import com.fasionparade.fasionparadeApp.Functions.Database.DatabaseHandler;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveChatUser;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.PublicParade;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ArrayList<ActiveParade> myParadeList;

    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    private OnFragmentInteractionListener mListener;

    Context context;
    LinearLayout activeParadeLayout, winnerLayout;
    RecyclerView inboxRecyclerView, chatsRecyclerView;

    TextView groupTextView, activeParadeTextView,noListTextView;
    ImageView activeParadeImageView, winnerImageView, profileImageView,settingImage;

    TextView topChatBadgeTextView,topParadeBadgeTextView;

    ChatListAdapter chatListAdapter;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public InboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invites, container, false);
        context = container.getContext();
        pref = getActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE);

        activeParadeLayout = (LinearLayout) view.findViewById(R.id.activeParadeLayout);
        winnerLayout = (LinearLayout) view.findViewById(R.id.winnerLayout);
        inboxRecyclerView = (RecyclerView) view.findViewById(R.id.inboxRecyclerView);
        chatsRecyclerView = (RecyclerView) view.findViewById(R.id.chatsRecyclerView);

        activeParadeImageView = (ImageView) view.findViewById(R.id.activeParadeImageView);
        winnerImageView = (ImageView) view.findViewById(R.id.winnerImageView);

        groupTextView = (TextView) view.findViewById(R.id.groupTextView);
        activeParadeTextView = (TextView) view.findViewById(R.id.activeParadeTextView);
        noListTextView = (TextView) view.findViewById(R.id.NoListTextView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        inboxRecyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        chatsRecyclerView.setLayoutManager(linearLayoutManager1);

        topChatBadgeTextView = (TextView) view.findViewById(R.id.topChatBadgeTextView);
        topParadeBadgeTextView = (TextView) view.findViewById(R.id.topparadeBadgeTextView);

        int spacing = getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, spacing, true);

        inboxRecyclerView.addItemDecoration(gridSpacingItemDecoration);
        profileImageView = (ImageView) view.findViewById(R.id.profileImageView);
        settingImage= (ImageView) view.findViewById(R.id.settingImage);
        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SettingsActivity.class));
                getActivity().finish();

            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().finish();

            }
        });

        activeParadeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activeParadeImageView.setImageResource(R.drawable.parade_icon_white_color);
                winnerImageView.setImageResource(R.drawable.winner_icon);

                topParadeBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);
                topChatBadgeTextView.setBackgroundResource(R.drawable.bg_circle);

                inboxRecyclerView.setVisibility(View.VISIBLE);
                chatsRecyclerView.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    winnerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    topParadeBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlackText));
                    topChatBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                } else {
                    activeParadeLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    winnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    topParadeBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorBlackText));
                    topChatBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                }

                getMyInboxParadeList();


            }
        });

        winnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inboxRecyclerView.setVisibility(View.GONE);
                chatsRecyclerView.setVisibility(View.VISIBLE);

                activeParadeImageView.setImageResource(R.drawable.parade_icon);
                winnerImageView.setImageResource(R.drawable.winner_icon_whitecolor);

                topParadeBadgeTextView.setBackgroundResource(R.drawable.bg_circle);
                topChatBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    winnerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    topParadeBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    topChatBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlackText));
                } else {
                    activeParadeLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    activeParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    winnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    topParadeBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    topChatBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorBlackText));
                }


                User user = Utils.getUserFromPreference(getActivity());

                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                final ArrayList<ActiveChatUser> activeChatUserArrayList = databaseHandler.getUserList(user.id);


                if (activeChatUserArrayList != null) {
                    if (activeChatUserArrayList.size() > 0) {
                        System.out.println("chat size --" + activeChatUserArrayList.size() + "");

                        chatsRecyclerView.setVisibility(View.VISIBLE);
                        noListTextView.setVisibility(View.GONE);

                        chatListAdapter = new ChatListAdapter(getActivity(), activeChatUserArrayList);
                        chatsRecyclerView.setAdapter(chatListAdapter);

                      /*  ChatListAdapter chatListAdapter = new ChatListAdapter(getActivity(), myParadeList);
                        chatsRecyclerView.setAdapter(chatListAdapter);*/
                        chatListAdapter.SetOnItemClickListener(new ChatListAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View v, int position) {

                                Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                                intent.putExtra("RECEIVER_ID", activeChatUserArrayList.get(position).getId());
                                intent.putExtra("NAME",activeChatUserArrayList.get(position).getName());
                                startActivity(intent);

                            }
                        });
                    }
                    else
                    {
                        chatsRecyclerView.setVisibility(View.GONE);
                        noListTextView.setVisibility(View.VISIBLE);
                        noListTextView.setText("You have no chat");
                    }
                }


                //getWinnerList();


            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void getMyInboxParadeList() {
        cd = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.myInboxParade();
            url = url + "userId=" + user.id + "&testTime=2016-03-18";
            new getMyInboxParade().execute(url);
        }
    }

    private class getMyInboxParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        String paradeName;
        String paradeId;
        String startTime;
        String endTime;
        String image;
        String userName;
        String aboutParade;
        String userId;
        String profilepic;
        String followingStatus;
        String friendId;
        String type;


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

                    pParade = JsonParser.getPPResult(jsonObject);
                    int length = pParade.size();
                    if (length > 0) {
                        noListTextView.setVisibility(View.GONE);
                        myParadeList = new ArrayList<ActiveParade>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++) {
                            ActiveParade activeParade = new ActiveParade();
                            type = pParade.get(j).type;
                            if(type.equals("parade")) {
                                JSONArray imageArray = pParade.get(j).imagePathJson;
                                imageObject = imageArray.getJSONObject(0);

                                paradeName = pParade.get(j).paradeName;
                                startTime = pParade.get(j).startTime;
                                endTime = pParade.get(j).endTime;
                                userName = pParade.get(j).userName;
                                aboutParade = pParade.get(j).aboutParade;
                                userId = pParade.get(j).userId;
                                followingStatus = pParade.get(j).followingStatus;
                                profilepic = pParade.get(j).profilePic;
                                paradeId = pParade.get(j).paradeId;


                                image = imageObject.getString("fileName");
                                activeParade.paradeName = paradeName;
                                activeParade.startTime = getDate(startTime);
                                activeParade.endTime = getDate(endTime);
                                activeParade.imagePath = image;
                                activeParade.userName = userName;
                                activeParade.aboutParade = aboutParade;
                                activeParade.userId = userId;
                                activeParade.followingStatus = followingStatus;
                                activeParade.paradeId = paradeId;
                                activeParade.profilePic = profilepic;
                                activeParade.imagePathJson = imageArray;
                                activeParade.type = type;
                            }
                            else if(type.equals("contact"))
                            {
                                friendId = pParade.get(j).friendId;
                                userName = pParade.get(j).userName;
                                profilepic = pParade.get(j).profilePic;

                                activeParade.friendId = friendId;
                                activeParade.userName = userName;
                                activeParade.profilePic = profilepic;
                                activeParade.type = type;
                            }
                            myParadeList.add(activeParade);
                        }
                        InboxListAdapter inboxListAdapter = new InboxListAdapter(getActivity(), myParadeList);
                        inboxRecyclerView.setAdapter(inboxListAdapter);
                        inboxListAdapter.SetOnItemClickListener(new InboxListAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View v, int position) {

                            }
                        });


                    } else {
                        Toast.makeText(getActivity(),
                                "No Records Found", Toast.LENGTH_SHORT)
                                .show();
                    }


                } else {

                        inboxRecyclerView.setVisibility(View.GONE);
                        noListTextView.setVisibility(View.VISIBLE);
                        noListTextView.setText("You have no inivites");

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
    public void onResume() {
        super.onResume();
        getMyInboxParadeList();
        User user = Utils.getUserFromPreference(getActivity());
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        String count = databaseHandler.getTotalReadStatus(user.id);

        Log.i("Count", count);


        if (!count.equals("")) {
            Log.i("Count", "not empty");

            topChatBadgeTextView.setVisibility(View.VISIBLE);
            topChatBadgeTextView.setText(count);

        } else {

            Log.i("Count", "empty");
            topChatBadgeTextView.setVisibility(View.GONE);

        }

        if (chatListAdapter != null)
            chatListAdapter.notifyDataSetChanged();


       // Toast.makeText(context, String.valueOf(pref.getInt("InvitesCount", 0)),Toast.LENGTH_LONG).show();
        if(pref.getInt("InvitesCount",0)!=0) {
            topParadeBadgeTextView.setVisibility(View.VISIBLE);
            topParadeBadgeTextView.setText(String.valueOf(pref.getInt("InvitesCount", 0)));
        }
        else
            topParadeBadgeTextView.setVisibility(View.GONE);

//setting fragment in application class
        ((AppController) getActivity().getApplicationContext()).setInboxFragment(this);

    }


    @Override
    public void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    public void onStop() {
        clearReferences();
        super.onStop();
    }


    private void clearReferences() {
        InboxFragment currInboxFrag = ((AppController) getActivity().getApplicationContext()).getInboxFragment();
        if (this.equals(currInboxFrag))
            ((AppController) getActivity().getApplicationContext()).setInboxFragment(null);
    }


    public void update() {

        User user = Utils.getUserFromPreference(getActivity());
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        final String count = databaseHandler.getTotalReadStatus(user.id);

        Log.i("inbox frag Count", count);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pref.getInt("InvitesCount", 0) != 0) {
                    topParadeBadgeTextView.setVisibility(View.VISIBLE);
                    topParadeBadgeTextView.setText(String.valueOf(pref.getInt("InvitesCount", 0)));
                } else
                    topParadeBadgeTextView.setVisibility(View.GONE);
            }
        });


        if (!count.equals("")) {
            Log.i("Count", "not empty");

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (topChatBadgeTextView != null) {
                        topChatBadgeTextView.setVisibility(View.VISIBLE);
                        topChatBadgeTextView.setText(count);
                    }
                }
            });


        } else {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (topChatBadgeTextView != null) {
                        Log.i("Count", "empty");
                        topChatBadgeTextView.setVisibility(View.GONE);
                    }
                }
            });


        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatListAdapter != null) {
                  //  chatListAdapter.notifyDataSetChanged();


                    User user = Utils.getUserFromPreference(getActivity());

                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                    final ArrayList<ActiveChatUser> activeChatUserArrayList = databaseHandler.getUserList(user.id);


                    if (activeChatUserArrayList != null) {
                        if (activeChatUserArrayList.size() > 0) {
                            System.out.println("chat size --" + activeChatUserArrayList.size() + "");

                            chatListAdapter = new ChatListAdapter(getActivity(), activeChatUserArrayList);
                            chatsRecyclerView.setAdapter(chatListAdapter);

                      /*  ChatListAdapter chatListAdapter = new ChatListAdapter(getActivity(), myParadeList);
                        chatsRecyclerView.setAdapter(chatListAdapter);*/
                            chatListAdapter.SetOnItemClickListener(new ChatListAdapter.OnItemClickListener() {

                                @Override
                                public void onItemClick(View v, int position) {

                                    Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                                    intent.putExtra("RECEIVER_ID", activeChatUserArrayList.get(position).getId());
                                    intent.putExtra("NAME", activeChatUserArrayList.get(position).getName());
                                    startActivity(intent);

                                }
                            });
                        }
                    }

                }


            }
        });


    }


}