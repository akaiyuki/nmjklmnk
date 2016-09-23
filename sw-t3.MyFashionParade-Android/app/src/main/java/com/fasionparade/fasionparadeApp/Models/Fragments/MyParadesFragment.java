package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.MyParadePageActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.NewParadeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ParadeResultActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.SettingsActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.ActiveParadeListAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.WinnerListAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyParadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyParadesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    private OnFragmentInteractionListener mListener;

    Context context;

    ImageView activeParadeImageView, profileImageView, winnerImageView,settingImage;
    RelativeLayout activeParadeLayout,winnerLayout;

    RelativeLayout activeParadeContentLayout;

    RecyclerView activeParadesRecyclerView,listTypeRecyclerView;
    TextView topWinnerBadgeTextView,topParadeBadgeTextView;

    LinearLayoutManager linearLayoutManager, listLinearLayoutManager;

    LinearLayout startParadeLinLayout;

    TextView groupTextView, activeParadeTextView,noListTextView;

    ArrayList<ActiveParade> myParadeList;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public MyParadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyParadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyParadesFragment newInstance(String param1, String param2) {
        MyParadesFragment fragment = new MyParadesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_parades, container, false);
        context = container.getContext();
        pref = getActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE);


        activeParadeLayout = (RelativeLayout) view.findViewById(R.id.activeParadeLayout);
        winnerLayout = (RelativeLayout) view.findViewById(R.id.winnerLayout);

        activeParadeContentLayout = (RelativeLayout) view.findViewById(R.id.activeParadeContentLayout);


        activeParadeImageView = (ImageView) view.findViewById(R.id.activeParadeImageView);
        winnerImageView = (ImageView) view.findViewById(R.id.winnerImageView);

        startParadeLinLayout = (LinearLayout) view.findViewById(R.id.startParadeLinLayout);

        groupTextView = (TextView) view.findViewById(R.id.groupTextView);
        activeParadeTextView = (TextView) view.findViewById(R.id.activeParadeTextView);
        noListTextView = (TextView) view.findViewById(R.id.NoListTextView);

        topWinnerBadgeTextView  = (TextView) view.findViewById(R.id.topwinnerBadgeTextView);
        topParadeBadgeTextView  = (TextView) view.findViewById(R.id.topparadeBadgeTextView);
        //RecyclerViews
        activeParadesRecyclerView = (RecyclerView) view.findViewById(R.id.activeParadesRecyclerView);
        listTypeRecyclerView=(RecyclerView)view.findViewById(R.id.listTypeRecyclerView);

        activeParadeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activeParadeImageView.setImageResource(R.drawable.parade_icon_white_color);
                winnerImageView.setImageResource(R.drawable.winner_icon);

                activeParadesRecyclerView.setVisibility(View.VISIBLE);
                listTypeRecyclerView.setVisibility(View.GONE);
                topParadeBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);
                topWinnerBadgeTextView.setBackgroundResource(R.drawable.bg_circle);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    winnerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    topParadeBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlackText));
                    topWinnerBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));

                } else {
                    activeParadeLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    activeParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    winnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    topParadeBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorBlackText));
                    topWinnerBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));

                }

                getMyParadeList();


            }
        });

        winnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activeParadesRecyclerView.setVisibility(View.GONE);
                listTypeRecyclerView.setVisibility(View.VISIBLE);

                activeParadeImageView.setImageResource(R.drawable.parade_icon);
                winnerImageView.setImageResource(R.drawable.winner_icon_whitecolor);


                topParadeBadgeTextView.setBackgroundResource(R.drawable.bg_circle);
                topWinnerBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    activeParadeLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteBackground));
                    activeParadeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGroupText));
                    winnerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHighlighted));
                    groupTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    topParadeBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteText));
                    topWinnerBadgeTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlackText));

                } else {
                    activeParadeLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhiteBackground));
                    activeParadeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorGroupText));
                    winnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorHighlighted));
                    groupTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    topParadeBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorWhiteText));
                    topWinnerBadgeTextView.setTextColor(getActivity().getResources().getColor(R.color.colorBlackText));
                }

                getWinnerList();


            }
        });

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



        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        activeParadesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, spacingInPixels, true));
        listTypeRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, spacingInPixels, true));

        //Active parade
        linearLayoutManager=new LinearLayoutManager(getActivity());
        activeParadesRecyclerView.setLayoutManager(linearLayoutManager);

        //winner parade list view
        listLinearLayoutManager=new LinearLayoutManager(getActivity());
        listTypeRecyclerView.setLayoutManager(listLinearLayoutManager);


        startParadeLinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FashionHomeActivity.repost_clicked = false;
                Intent intent = new Intent(getActivity(), NewParadeActivity.class);
                startActivity(intent);

            }
        });


        return view;
    }

    private void getWinnerList() {
        getMyWinnerParadeList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getMyWinnerParadeList() {
        cd = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.myWinnerParade();
            url = url + "userId=" + user.id + "&visitorId=" + user.id;
            new getMyWinnerParade().execute(url);
        }
    }

    private class getMyWinnerParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        String paradeName;
        String startTime;
        String endTime;
        String image;

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
                        noListTextView.setVisibility(View.GONE);
                        myParadeList = new ArrayList<ActiveParade>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++) {
                            ActiveParade activeParade = new ActiveParade();
                            JSONArray imageArray = pParade.get(j).imagePathJson;
                            imageObject = imageArray.getJSONObject(0);

                            paradeName = pParade.get(j).paradeName;
                            startTime = pParade.get(j).startTime;
                            endTime = pParade.get(j).endTime;
                            image = imageObject.getString("fileName");
                            activeParade.imagePathJson = imageArray;
                            activeParade.paradeName = paradeName;
                            activeParade.startTime = getFormattedDate(startTime);
                            activeParade.endTime = endTime;
                            activeParade.imagePath = image;
                            activeParade.userId = pParade.get(j).userId;
                            activeParade.paradeId = pParade.get(j).paradeId;
                            activeParade.tag = pParade.get(j).tag;
                            activeParade.aboutParade = pParade.get(j).aboutParade;
                            activeParade.sharedWith = pParade.get(j).sharedWith;
                            activeParade.groupId = pParade.get(j).groupId;
                            activeParade.duration = pParade.get(j).duration;
                            myParadeList.add(activeParade);
                        }
                        //Listview
                        WinnerListAdapter winnerListAdapter = new WinnerListAdapter(getActivity(), myParadeList);
                        listTypeRecyclerView.setAdapter(winnerListAdapter);

                        winnerListAdapter.SetOnItemClickListener(new WinnerListAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View v, int position) {
                                if(pref.getInt("WinnerCount",0)!=0) {
                                    editor = pref.edit();
                                    if (!pref.getBoolean("viewedWinner" + myParadeList.get(position).paradeId, false)) {
                                        editor.putInt("WinnerCount", (pref.getInt("WinnerCount", 0) - 1));
                                    }
                                    editor.putBoolean("viewedWinner" + myParadeList.get(position).paradeId, true);
                                    editor.commit();
                                }
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(myParadeList);

                                Intent intent = new Intent(context, ParadeResultActivity.class);
                                intent.putExtra("data", jsonData);
                                intent.putExtra("jsonArray", String.valueOf(myParadeList.get(position).imagePathJson));
                                intent.putExtra("index", String.valueOf(position));
                                startActivity(intent);


                            }
                        });


                    }


                } else {
                    listTypeRecyclerView.setVisibility(View.GONE);
                    noListTextView.setVisibility(View.VISIBLE);
                    noListTextView.setText("You have no winner");
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

    private void getMyParadeList() {
        cd = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.myActiveParade();
            url = url + "userId=" + user.id + "&visitorId=" + user.id;
            new getMyParade().execute(url);
        }
    }

    private class getMyParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        String paradeName;
        String startTime;
        String endTime;
        String image;
        String userId;
        String paradeId;
        String sharedWith;
        String completedStatus;

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
                        noListTextView.setVisibility(View.GONE);
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
                            sharedWith = pParade.get(j).sharedWith;
                            completedStatus = pParade.get(j).completedStatus;
                            image = imageObject.getString("fileName");
                            activeParade.imagePathJson = imageArray;
                            activeParade.paradeName = paradeName;
                            activeParade.startTime = getDate(startTime);
                            activeParade.formatstartTime = getFormattedDate(startTime);
                            activeParade.endTime = getDate(endTime);
                            activeParade.imagePath = image;
                            activeParade.fileType=imageObject.getString("fileType");
                            activeParade.userId = userId;
                            activeParade.paradeId = paradeId;
                            activeParade.tag = pParade.get(j).tag;
                            activeParade.aboutParade = pParade.get(j).aboutParade;
                            activeParade.sharedWith = pParade.get(j).sharedWith;
                            activeParade.groupId = pParade.get(j).groupId;
                            activeParade.completedStatus = pParade.get(j).completedStatus;
                            activeParade.duration = pParade.get(j).duration;
                            myParadeList.add(activeParade);
                        }


                        ActiveParadeListAdapter activeParadeListAdapter = new ActiveParadeListAdapter(context, myParadeList);

                        activeParadesRecyclerView.setAdapter(activeParadeListAdapter);

                        activeParadeListAdapter.SetOnItemClickListener(new ActiveParadeListAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View v, int position) {
                                if(pref.getInt("VoteCount",0)!=0) {
                                    editor = pref.edit();
                                    if (!pref.getBoolean("viewedParade" + myParadeList.get(position).paradeId, false)) {
                                        editor.putInt("VoteCount", (pref.getInt("VoteCount", 0) - 1));
                                    }
                                    editor.putBoolean("viewedParade" + myParadeList.get(position).paradeId, true);
                                    editor.commit();
                                }
                                if(myParadeList.get(position).completedStatus.equals("2")) {
                                    String image= myParadeList.get(position).imagePath;
                                    String startTime = myParadeList.get(position).formatstartTime;
                                    int index=position;
                                    String fileType = myParadeList.get(position).fileType;
                                    dialogShowParadeRepost(image,startTime,index,fileType);
                                }
                                else
                                {
                                    Gson gson = new Gson();
                                    String jsonData = gson.toJson(myParadeList);
                                    Intent intent = new Intent(context, MyParadePageActivity.class);
                                    intent.putExtra("data", jsonData);
                                    intent.putExtra("jsonArray", String.valueOf(myParadeList.get(position).imagePathJson));
                                    intent.putExtra("index", String.valueOf(position));
                                    startActivity(intent);
                                }

                            }
                        });

                    } else {


                    }


                } else {
                    activeParadesRecyclerView.setVisibility(View.GONE);
                    noListTextView.setVisibility(View.VISIBLE);
                    noListTextView.setText("You have no active parade");                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
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
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
    public void update() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pref.getInt("VoteCount", 0) != 0) {
                    topParadeBadgeTextView.setVisibility(View.VISIBLE);
                    topParadeBadgeTextView.setText(String.valueOf(pref.getInt("VoteCount", 0)));
                } else
                    topParadeBadgeTextView.setVisibility(View.GONE);
            }
        });




            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pref.getInt("WinnerCount", 0) != 0) {
                        topWinnerBadgeTextView.setVisibility(View.VISIBLE);
                        topWinnerBadgeTextView.setText(String.valueOf(pref.getInt("WinnerCount", 0)));
                    } else
                        topWinnerBadgeTextView.setVisibility(View.GONE);
                }

            });


        }

    public void dialogShowParadeRepost(String image,String starttime,final int index,String fileType)

    {
        final Dialog dialogParadeDetail = new Dialog(getActivity(), R.style.custom_dialog_theme);
        dialogParadeDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogParadeDetail.setContentView(R.layout.layout_popup_repost);

        TextView aboutParadeTextView = (TextView) dialogParadeDetail.findViewById(R.id.startTime);
        ImageView thumbImageView = (ImageView) dialogParadeDetail.findViewById(R.id.thumbImageView);
        VideoView videoView = (VideoView) dialogParadeDetail.findViewById(R.id.videoView);
        TextView yesTextView = (TextView) dialogParadeDetail.findViewById(R.id.yesTextView);
        TextView noTextView = (TextView) dialogParadeDetail.findViewById(R.id.noTextView);
        ImageView closeImageView = (ImageView) dialogParadeDetail.findViewById(R.id.closeImageView);

        aboutParadeTextView.setText(starttime);
        if (fileType.equals("1")) {
            videoView.setVisibility(View.VISIBLE);
            thumbImageView.setVisibility(View.GONE);
            Uri uri = Uri.parse(image);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.setZOrderOnTop(true);
            videoView.start();


        } else {
            videoView.setVisibility(View.GONE);
            thumbImageView.setVisibility(View.VISIBLE);
            System.out.println("image" + image);
            if(!image.isEmpty()) {
                Picasso.with(getActivity())
                        .load(image)
                        .placeholder(R.drawable.no_image)
                        .into(thumbImageView);
            }
        }

        yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FashionHomeActivity.repost_clicked=true;
                Gson gson = new Gson();
                String jsonData = gson.toJson(myParadeList);
                Intent intent = new Intent(context, NewParadeActivity.class);
                intent.putExtra("data", jsonData);
                intent.putExtra("jsonArray", String.valueOf(myParadeList.get(index).imagePathJson));
                intent.putExtra("index", String.valueOf(index));
                startActivity(intent);
                dialogParadeDetail.dismiss();
            }
        });
        noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogParadeDetail.dismiss();
            }
        });
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogParadeDetail.dismiss();
            }
        });

        dialogParadeDetail.show();
    }



    @Override
    public void onResume() {
        super.onResume();

        getMyParadeList();

        if(pref.getInt("VoteCount",0)!=0) {
            topParadeBadgeTextView.setVisibility(View.VISIBLE);
            topParadeBadgeTextView.setText(String.valueOf(pref.getInt("VoteCount", 0)));
        }
        else
            topParadeBadgeTextView.setVisibility(View.GONE);

        if(pref.getInt("WinnerCount",0)!=0) {
            topWinnerBadgeTextView.setVisibility(View.VISIBLE);
            topWinnerBadgeTextView.setText(String.valueOf(pref.getInt("WinnerCount", 0)));
        }
        else
            topWinnerBadgeTextView.setVisibility(View.GONE);

        //setting fragment in application class
        ((AppController) getActivity().getApplicationContext()).setMyParadesFragment(this);

    }
    }



