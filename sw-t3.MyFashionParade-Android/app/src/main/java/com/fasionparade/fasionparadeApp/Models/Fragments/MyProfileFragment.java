package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.MFPUserDetailsActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.SettingsActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.BrowseLatestParadeAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.MyProfileFollowersAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.MyProfileFollowingAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView latestParadeRecyclerView;
    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    //
    RelativeLayout paradeContentLayout,followersContentLayout,followingContentLayout;

    LinearLayout paradeLinLayout,followersLinLayout,followingLinLayout;

    RecyclerView followersRecyclerView,followingRecyclerView;

    TextView editProfileTextView,followingTxt,followerTxt,webTxt,mailTxt,bioTxt;
    BrowseLatestParadeAdapter browseLatestParadeAdapter;
    ImageView backImageView,circleImageView,profileImageView,settingImage;

    Context context;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        View view= inflater.inflate(R.layout.fragment_my_profile, container, false);
        context=container.getContext();

        paradeContentLayout=(RelativeLayout)view.findViewById(R.id.paradeContentLayout);
        followersContentLayout=(RelativeLayout)view.findViewById(R.id.followersContentLayout);
        followingContentLayout=(RelativeLayout)view.findViewById(R.id.followingContentLayout);
        latestParadeRecyclerView = (RecyclerView) view.findViewById(R.id.latestParadeRecyclerView);
        followingTxt=(TextView)view.findViewById(R.id.followingTxt);
        followerTxt=(TextView)view.findViewById(R.id.followerTxt);
        webTxt=(TextView)view.findViewById(R.id.webTxt);
        mailTxt = (TextView)view.findViewById(R.id.mailTxt);
        bioTxt = (TextView)view.findViewById(R.id.bioTxt);
        circleImageView=(ImageView)view.findViewById(R.id.circleImagView);
        //  paradeLinLayout=(LinearLayout)view.findViewById(R.id.paradeLinLayout);
        followersLinLayout=(LinearLayout)view.findViewById(R.id.followersLinLayout);
        followingLinLayout=(LinearLayout)view.findViewById(R.id.followingLinLayout);

        followersRecyclerView=(RecyclerView)view.findViewById(R.id.followersRecyclerView);
        followingRecyclerView=(RecyclerView)view.findViewById(R.id.followingRecyclerView);

        editProfileTextView=(TextView)view.findViewById(R.id.editProfileTextView);



        profileImageView = (ImageView) view.findViewById(R.id.profileImageView);
        settingImage = (ImageView) view.findViewById(R.id.settingImage);


        followersLinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paradeContentLayout.setVisibility(View.GONE);
                followersContentLayout.setVisibility(View.VISIBLE);
                followingContentLayout.setVisibility(View.GONE);

            }
        });

        followingLinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paradeContentLayout.setVisibility(View.GONE);
                followersContentLayout.setVisibility(View.GONE);
                followingContentLayout.setVisibility(View.VISIBLE);

            }
        });

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


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        followersRecyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager followingLayoutManager=new LinearLayoutManager(getActivity());
        followingRecyclerView.setLayoutManager(followingLayoutManager);


        editProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MEngine.switchFragment((BaseActivity) getActivity(), new EditProfileFragment(), ((BaseActivity) getActivity()).getFrameLayout());

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
        User user= Utils.getUserFromPreference(context);
        followingTxt.setText(user.followings);
        followerTxt.setText(user.followers);
        webTxt.setText(user.website);
        mailTxt.setText(user.mail);
        bioTxt.setText(user.bio);
        getMyFollowerList();
    }

    private void getMyFollowerList() {
        cd = new ConnectionCheck(context);
        User user= Utils.getUserFromPreference(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.myFollowerList();
            url = url + "userId=" + user.id;
            new GetMyFollower().execute(url);
        }
        if(user.profilePic!=null&&!user.profilePic.isEmpty())
            loadProfilePic(user.profilePic);
    }

    private void loadProfilePic(String profilePic) {

        Picasso.with(context)
                .load(profilePic)
                .placeholder(R.drawable.no_image)
                .into(circleImageView);
    }

    private class GetMyFollower extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<User> fTotalUser;
        ArrayList<User> fFollowUser;
        ArrayList<User> fFollowingUser;
        //  myParadeList=new ArrayList<User>();

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
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {


                    fTotalUser = JsonParser.getFollowResult(jsonObject);
                    int length=fTotalUser.size();
                    if(length>0){
                        fFollowUser=new ArrayList<User>();
                        fFollowingUser=new ArrayList<User>();
                        JSONObject imageObject;
                        for (int j = 0; j < length; j++)
                        {
                            User userFollow = new User();
                            String followers = fTotalUser.get(j).followers;

                            if(followers.equals("1"))
                            {

                                //Follower
                                userFollow.contactName=fTotalUser.get(j).contactName;
                                userFollow.mail=fTotalUser.get(j).mail;
                                userFollow.id=fTotalUser.get(j).id;
                                userFollow.profilePic=fTotalUser.get(j).profilePic;
                                userFollow.followingsStatus =fTotalUser.get(j).followingsStatus;
                                userFollow.chatBlockedStatus =fTotalUser.get(j).chatBlockedStatus;
                                fFollowUser.add(userFollow);
                            }
                            else if(followers.equals("2"))
                            {
                                //Followings
                                userFollow.contactName=fTotalUser.get(j).contactName;
                                userFollow.mail=fTotalUser.get(j).mail;
                                userFollow.id=fTotalUser.get(j).id;
                                userFollow.profilePic=fTotalUser.get(j).profilePic;
                                userFollow.followingsStatus =fTotalUser.get(j).followingsStatus;
                                userFollow.chatBlockedStatus =fTotalUser.get(j).chatBlockedStatus;
                                fFollowingUser.add(userFollow);
                            }
                        }

                        MyProfileFollowersAdapter myProfileFollowersAdapter=new MyProfileFollowersAdapter(getActivity(),fFollowUser);
                        followersRecyclerView.setAdapter(myProfileFollowersAdapter);

                        MyProfileFollowingAdapter myProfileFollowingAdapter=new MyProfileFollowingAdapter(getActivity(),fFollowingUser);
                        followingRecyclerView.setAdapter(myProfileFollowingAdapter);


                      myProfileFollowingAdapter.SetOnItemClickListener(new MyProfileFollowingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position)
                        {


                            Intent i =new Intent(getActivity(), MFPUserDetailsActivity.class);
                            i.putExtra("followerid",String.valueOf(fFollowingUser.get(position).id));
                            Log.i("followerid", String.valueOf(fFollowingUser.get(position).id));
                            startActivity(i);


                        }
                    });


                    }

                }else{
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
            pDialog.cancel();
        }
    }
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
}
