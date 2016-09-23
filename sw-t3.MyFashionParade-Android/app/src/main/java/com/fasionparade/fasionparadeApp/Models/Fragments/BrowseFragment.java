package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.InboxPageActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.LatestWinnerUserActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.MyParadePageActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.MyFaveSingleViewActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ParadeResultActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.SettingsActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.FollowMtpAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.LatestparadesAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.MyFavouritesAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.FindFriend;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.MyFavourite;
import com.fasionparade.fasionparadeApp.Functions.Object.PublicParade;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;

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
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ImageView profileImageView,settingImage;

    ProgressDialog pDialog;
    List<PublicParade> pParade;
    String paradeName;
    String startTime;
    String endTime;
    String image;
    Context context;
    String getUserId="";


    ArrayList<ActiveParade> latestParades;
    ArrayList<ActiveParade> latestWinners;
    ArrayList<MyFavourite> myFavourites;
    ArrayList<FindFriend> findFriends;

   public static MyFavouritesAdapter myFavouritesAdapter;

    ConnectionCheck connectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    User user;
    public static TextView latestParadeTextView, latestWinnerTextView, myFavouritesTextView,EmptyView ,myFollowTextView;


    RecyclerView latestParadeRecyclerView;
    RelativeLayout latestParadeContentLayout;
    String errorCode;
    String message_test;

    EditText Search_text;

    String userName="",profilePic="",followingStatus="";
    Intent intent;


    public BrowseFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
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
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        latestParades = new ArrayList<>();
        latestWinners = new ArrayList<>();

        findFriends = new ArrayList<>();

        myFavourites =new ArrayList<>();


        context = getActivity();
        latestParadeTextView = (TextView) view.findViewById(R.id.latestParadeTextView);
        latestWinnerTextView = (TextView) view.findViewById(R.id.latestWinnerTextView);
        myFavouritesTextView = (TextView) view.findViewById(R.id.myFavouritestextview);
        myFollowTextView = (TextView)view.findViewById(R.id.myFollowTextView);
     //   back_imageview_seeting =(ImageView)view.findViewById(R.id.backImageView_browerFargment);

        Search_text =(EditText)view.findViewById(R.id.searchEditText);
        Search_text.setEnabled(false);
        Search_text.setHint("Search the #tag");
        Search_text.setText("");

        EmptyView=(TextView)view.findViewById(R.id.textView_latest_parades);
        latestParadeRecyclerView = (RecyclerView) view.findViewById(R.id.latestParadeRecyclerView);
        latestParadeContentLayout = (RelativeLayout) view.findViewById(R.id.latestParadeContentLayout);
        profileImageView=(ImageView)view.findViewById(R.id.profileImageView);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().finish();

            }
        });
        settingImage= (ImageView) view.findViewById(R.id.settingImage);
        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SettingsActivity.class));
                getActivity().finish();
            }
        });


        getLatestParadeResponse();


        /**
         * Enabling Search Filter
         * */
        Search_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable!=null&&!editable.toString().isEmpty()) {
                    connectionCheck = new ConnectionCheck(context);
                    if (!connectionCheck.isConnectingToInternet()) {
                        alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
                    } else {


                        String url = ResourceManager.searchfriend();
                        user = Utils.getUserFromPreference(context);
                        String newURL = url + "userId=" + user.id + "&searchText=" + editable;
                        Log.i("Search friend", newURL);
                        new SearchFriend().execute(newURL);

                    }

                }
            }
        });


        latestParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                latestWinners.clear();
                findFriends.clear();
                myFavourites.clear();
                Search_text.setVisibility(View.VISIBLE);

                Search_text.setEnabled(false);
                Search_text.setHint("Search the #tag");
                Search_text.setText("");
                FashionHomeActivity.winner_clicked=false;
                getLatestParadeResponse();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    latestParadeTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestParadeBackground));
                    latestParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    latestWinnerTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    latestWinnerTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    myFollowTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    myFollowTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    myFavouritesTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    myFavouritesTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                } else {
                    latestParadeTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestParadeBackground));
                    latestParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    latestWinnerTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    latestWinnerTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    myFollowTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    myFollowTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    myFavouritesTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    myFavouritesTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                }

            }
        });

        latestWinnerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                latestParades.clear();
                findFriends.clear();
                myFavourites.clear();
                Search_text.setVisibility(View.VISIBLE);

                Search_text.setEnabled(false);
                Search_text.setHint("Search the #tag");
                Search_text.setText("");
                FashionHomeActivity.parade_clicked=false;
                getLatestWinners();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    latestParadeTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    latestParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    latestWinnerTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestParadeBackground));
                    latestWinnerTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    myFollowTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    myFollowTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    myFavouritesTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    myFavouritesTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                } else {
                    latestParadeTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    latestParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    latestWinnerTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestParadeBackground));
                    latestWinnerTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    myFollowTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    myFollowTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    myFavouritesTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    myFavouritesTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                }

            }
        });


        myFollowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                latestParades.clear();
                latestWinners.clear();
                myFavourites.clear();
                Search_text.setVisibility(View.VISIBLE);

                Search_text.setEnabled(true);
                Search_text.setHint("Search the #tag");
                EmptyView.setVisibility(View.VISIBLE);
                EmptyView.setText("Enter the username of the person you want to follow in the above search field");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    latestParadeTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    latestParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    latestWinnerTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    latestWinnerTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    myFollowTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestParadeBackground));
                    myFollowTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    myFavouritesTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    myFavouritesTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                } else {
                    latestParadeTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    latestParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    latestWinnerTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    latestWinnerTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    myFollowTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestParadeBackground));
                    myFollowTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    myFavouritesTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    myFavouritesTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                }


            }
        });


        myFavouritesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                latestParades.clear();
                findFriends.clear();
                latestWinners.clear();
//                Search_text.setEnabled(false);
//                Search_text.setHint("Search the #tag");
//                Search_text.setText("");
                Search_text.setVisibility(View.GONE);
                getMyFavourites();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    latestParadeTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    latestParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    latestWinnerTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    latestWinnerTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    myFavouritesTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestParadeBackground));
                    myFavouritesTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    myFollowTextView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLatestWinner));
                    myFollowTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                } else {
                    latestParadeTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    latestParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    latestWinnerTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    latestWinnerTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    myFavouritesTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestParadeBackground));
                    myFavouritesTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    myFollowTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLatestWinner));
                    myFollowTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                }


            }
        });


        return view;
    }


    // My Favourites
    public void getMyFavourites()
    {

        connectionCheck = new ConnectionCheck(context);
        if (!connectionCheck.isConnectingToInternet())
        {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else
        {

            String url = ResourceManager.myfavourites();
            user= Utils.getUserFromPreference(context);
            String newURL = url + "userId=" + user.id;

            Log.i("My favourites Response", newURL);
            new MyFavourites().execute(newURL);
        }
    }



    //Latestparade
    public void getLatestParadeResponse()
    {
        connectionCheck = new ConnectionCheck(context);
        if (!connectionCheck.isConnectingToInternet())
        {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {


            String url = ResourceManager.latestparades();
            user= Utils.getUserFromPreference(context);
            String newURL = url + "userId=" + user.id;
            Log.i("Latestparade Response", newURL);
            new LatestParadesPass().execute(newURL);
        }
    }



    //latestWinners_pass
    public  void getLatestWinners() {

        connectionCheck = new ConnectionCheck(context);
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.latestwinners();
            user = Utils.getUserFromPreference(context);
            String newURL = url + "userId=" + user.id;
            Log.i("latestWinners Response", newURL);
            new LatestWinnersPass().execute(newURL);
        }

    }



    //latestWinners_pass
    private class LatestWinnersPass extends AsyncTask<String, Void, String>
    {
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
        protected String doInBackground(String... params)
        {
            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {
                    EmptyView.setVisibility(View.GONE);
                    pParade = JsonParser.getWinnerResult(jsonObject);
                    int length=pParade.size();
                    if(length>0)
                    {
                        latestWinners = new ArrayList<>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++)
                        {
                            ActiveParade activeParade = new ActiveParade();
                            JSONArray imageArray=pParade.get(j).imagePathJson;
                            imageObject = imageArray.getJSONObject(0);

                            paradeName=pParade.get(j).paradeName;
                            startTime=pParade.get(j).startTime;
                            endTime=pParade.get(j).endTime;
                            image=imageObject.getString("fileName");
                            activeParade.imagePathJson=imageArray;
                            activeParade.paradeName= paradeName;
                            activeParade.startTime=getFormattedDate(startTime);
                            activeParade.endTime=endTime;
                            activeParade.imagePath=image;
                            activeParade.profilePic=pParade.get(j).profilePic;

                            activeParade.userId= pParade.get(j).userId;
                            activeParade.paradeId=pParade.get(j).paradeId;
                            activeParade.tag=pParade.get(j).tag;
                            activeParade.aboutParade=pParade.get(j).aboutParade;
                            activeParade.sharedWith=pParade.get(j).sharedWith;
                            activeParade.groupId=pParade.get(j).groupId;
                            latestWinners.add(activeParade);
                        }


                        LatestparadesAdapter latestparadesAdapter=new LatestparadesAdapter(getActivity(),latestWinners,true);
                        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
                        latestParadeRecyclerView.setLayoutManager(gridLayoutManager);

                        //adding item decoration
                        int spacing= getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
                        GridSpacingItemDecoration gridSpacingItemDecoration=new GridSpacingItemDecoration(3,spacing,true);
                        latestParadeRecyclerView.addItemDecoration(gridSpacingItemDecoration);
                        latestParadeRecyclerView.setAdapter(latestparadesAdapter);



                        latestparadesAdapter.SetOnItemClickListener(new LatestparadesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position)
                            {
                                user= Utils.getUserFromPreference(context);
                                String userid = latestWinners.get(position).userId;
                                if(userid.equalsIgnoreCase(user.id))
                                {
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(latestWinners);
                                    Intent intent = new Intent(context,ParadeResultActivity.class);
                                    intent.putExtra("data",jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(latestWinners.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    startActivity(intent);


                                }

                                else
                                {
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(latestWinners);
                                    intent = new Intent(context,LatestWinnerUserActivity.class);
                                    intent.putExtra("data",jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(latestWinners.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    FashionHomeActivity.winner_clicked=true;
                                    getMfpUserDetails(userid);

                                }
                            }
                        });




                    }else
                    {
                       // Toast.makeText(getActivity(), "No Records Found", Toast.LENGTH_SHORT).show();
                        EmptyView.setVisibility(View.VISIBLE);
                        EmptyView.setGravity(Gravity.CENTER);
                        EmptyView.setText("There are currently no Latest winners");
                    }


                }else{


                  //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    EmptyView.setVisibility(View.VISIBLE);
                    EmptyView.setGravity(Gravity.CENTER);
                    EmptyView.setText("There are currently no Latest winners");



                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private String getFormattedDate(String OurDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE dd,MMM yyyy @HH:mm:a"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

            Log.d("Our-Date", OurDate);
        } catch (Exception e) {
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }

    private class LatestParadesPass extends AsyncTask<String, Void, String>
    {
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

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {
                    EmptyView.setVisibility(View.GONE);
                    pParade = JsonParser.getlastparades(jsonObject);
                    int length=pParade.size();
                    if(length>0)
                    {
                        latestParades=new ArrayList<ActiveParade>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++)
                        {
                            ActiveParade activeParade = new ActiveParade();
                            JSONArray imageArray=pParade.get(j).imagePathJson;
                            imageObject = imageArray.getJSONObject(0);

                            paradeName=pParade.get(j).paradeName;
                            startTime=pParade.get(j).startTime;
                            endTime=pParade.get(j).endTime;
                            image=imageObject.getString("fileName");
                            activeParade.imagePathJson=imageArray;
                            activeParade.paradeName= paradeName;
                            activeParade.startTime=getDate(startTime);
                            activeParade.formatstartTime=getFormattedDate(startTime);
                            activeParade.endTime=getDate(endTime);
                            activeParade.imagePath=image;
                            activeParade.userId= pParade.get(j).userId;
                            activeParade.profilePic=pParade.get(j).profilePic;
                            getUserId = pParade.get(j).userId;

                            activeParade.paradeId=pParade.get(j).paradeId;
                            activeParade.tag=pParade.get(j).tag;
                            activeParade.aboutParade=pParade.get(j).aboutParade;
                            activeParade.sharedWith=pParade.get(j).sharedWith;
                            activeParade.groupId=pParade.get(j).groupId;
                            latestParades.add(activeParade);
                        }




                        LatestparadesAdapter latestparadesAdapter=new LatestparadesAdapter(getActivity(),latestParades,false);
                        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
                        latestParadeRecyclerView.setLayoutManager(gridLayoutManager);

                        //adding item decoration
                        int spacing= getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
                        GridSpacingItemDecoration gridSpacingItemDecoration=new GridSpacingItemDecoration(3,spacing,true);
                        latestParadeRecyclerView.addItemDecoration(gridSpacingItemDecoration);
                        latestParadeRecyclerView.setAdapter(latestparadesAdapter);



                        latestparadesAdapter.SetOnItemClickListener(new LatestparadesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position)
                            {


                                user= Utils.getUserFromPreference(context);
                                String userid = latestParades.get(position).userId;
                                if(userid.equalsIgnoreCase(user.id))
                                {

                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(latestParades);
                                    Intent intent = new Intent(context, MyParadePageActivity.class);
                                    intent.putExtra("data",jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(latestParades.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    startActivity(intent);

                                }
                                else
                                {
                                    FashionHomeActivity.parade_clicked=true;
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(latestParades);

                                    intent = new Intent(context, InboxPageActivity.class);
                                    intent.putExtra("data",jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(latestParades.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    getMfpUserDetails(userid);
                                 }
                            }
                        });

                    }else
                    {
                        EmptyView.setVisibility(View.VISIBLE);
                        EmptyView.setText("There are currently no public parades");
                       // Toast.makeText(getActivity(), "No Records Found", Toast.LENGTH_SHORT).show();
                    }


                }else
                {
                   // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    EmptyView.setVisibility(View.VISIBLE);
                    EmptyView.setText("There are currently no public parades");

                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

                pDialog.cancel();

            }

    }



    //Searchfriend
    private class SearchFriend extends AsyncTask<String, Void, String>
    {
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

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {
                    EmptyView.setVisibility(View.GONE);
                    findFriends = new ArrayList<>();

                    //JSONOBJECT Commonjsonobject
                    JSONArray userdetails_itemsJsonarry = jsonObject.getJSONArray("userDetails");
                    for (int i = 0; i < userdetails_itemsJsonarry.length(); i++)
                    {

                        JSONObject userdetails_JSONObject = userdetails_itemsJsonarry.getJSONObject(i);
                        FindFriend findFriend =new FindFriend();
                        findFriend.setFriendid(userdetails_JSONObject.getString("friendId"));
                        findFriend.setFriendName(userdetails_JSONObject.getString("name"));
                        findFriend.setFriendUserName(userdetails_JSONObject.getString("userName"));
                        findFriend.setFriendProfilePic(userdetails_JSONObject.getString("profilePic"));
                        findFriend.setFriendfollowingStatus(userdetails_JSONObject.getString("followingStatus"));
                        findFriends.add(findFriend);
                    }


                    FollowMtpAdapter followMtpAdapter=new FollowMtpAdapter(getActivity(),findFriends);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                    latestParadeRecyclerView.setLayoutManager(gridLayoutManager);

                    //adding item decoration
                    int spacing= getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
                    GridSpacingItemDecoration gridSpacingItemDecoration=new GridSpacingItemDecoration(1,spacing,true);
                    latestParadeRecyclerView.addItemDecoration(gridSpacingItemDecoration);
                    latestParadeRecyclerView.setAdapter(followMtpAdapter);

                    }

                else
                {
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        EmptyView.setVisibility(View.VISIBLE);
                        EmptyView.setGravity(Gravity.CENTER);
                        EmptyView.setText("Enter the username of the person you want to follow in the above search field");
                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pDialog.cancel();

        }

    }

    private class MyFavourites extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {

                    myFavourites = new ArrayList<>();
                    EmptyView.setVisibility(View.GONE);
                    //JSONOBJECT Commonjsonobject
                    JSONArray myFavouriteDetailsItemsArray = jsonObject.getJSONArray("imageDetails");
                    for (int i = 0; i < myFavouriteDetailsItemsArray.length(); i++)
                    {
                        JSONObject myFavouriteDetails = myFavouriteDetailsItemsArray.getJSONObject(i);
                        MyFavourite myFavourite = new MyFavourite();
                        myFavourite.setImageid(myFavouriteDetails.getString("imageId"));
                        myFavourite.setImageName(myFavouriteDetails.getString("imageName"));
                        myFavourite.setImagetype(myFavouriteDetails.getString("imageType"));
                        myFavourites.add(myFavourite);
                    }

                    myFavouritesAdapter=new MyFavouritesAdapter(getActivity(),myFavourites);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
                    latestParadeRecyclerView.setLayoutManager(gridLayoutManager);

                    //adding item decoration
                    int spacing= getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
                    GridSpacingItemDecoration gridSpacingItemDecoration=new GridSpacingItemDecoration(3,spacing,true);
                    latestParadeRecyclerView.addItemDecoration(gridSpacingItemDecoration);
                    latestParadeRecyclerView.setAdapter(myFavouritesAdapter);


                    myFavouritesAdapter.SetOnItemClickListener(new MyFavouritesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(context, MyFaveSingleViewActivity.class);
                            intent.putExtra("jsonArray", String.valueOf(myFavourites.get(position).getImageName()));
                            startActivity(intent);

                        }
                    });
                }
                else
                {


                   // Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
                    EmptyView.setVisibility(View.VISIBLE);
                    EmptyView.setGravity(Gravity.CENTER);
                    EmptyView.setText("You have no favourites");

                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pDialog.cancel();

        }

    }

    public void getMfpUserDetails(String friendId) {
        connectionCheck = new ConnectionCheck(getActivity());
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {


            String url = ResourceManager.getMyProfile();
            User user = Utils.getUserFromPreference(getActivity());
            String newURL = url + "userId=" + user.id+"&friendId="+friendId;
            Log.i("MFP Response", newURL);
            new MFPUserDetails().execute(newURL);

        }
    }

    private class MFPUserDetails extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                    JSONArray mfpUserDetailsItemsArray = jsonObject.getJSONArray("userDetails");
                    for (int i = 0; i < mfpUserDetailsItemsArray.length(); i++) {


                        JSONObject mfpuserdetails_itemsJSONObject = mfpUserDetailsItemsArray.getJSONObject(i);
                        userName = mfpuserdetails_itemsJSONObject.getString("userName");
                        profilePic = mfpuserdetails_itemsJSONObject.getString("profilePic");
                        followingStatus = mfpuserdetails_itemsJSONObject.getString("followingStatus");

                    }

                        intent.putExtra("userName", userName);
                        intent.putExtra("profilePic", profilePic);
                        intent.putExtra("followingStatus", followingStatus);
                        startActivity(intent);


                } else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();

        }

    }


  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
