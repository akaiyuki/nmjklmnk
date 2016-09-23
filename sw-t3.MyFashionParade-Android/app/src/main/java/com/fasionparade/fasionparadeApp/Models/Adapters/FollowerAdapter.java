package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishnu on 7/27/2016.
 */
public class FollowerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    Context context;
    static Activity activity;
    ImageLoader imageLoader;
    static ArrayList<Contacts> contactList;
    static String userId="";
    static OnItemClickListener mItemClickListener;
    static String followingStatus="",followerId="";
    static  String checkNow ="1";
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    boolean followClicked;
    User user;


    public FollowerAdapter(Activity activity, ArrayList<Contacts> contactList) {
        this.contactList = contactList;
        this.activity=activity;
        user = Utils.getUserFromPreference(activity);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follwerlist_items, parent, false);
        VHItem dataObjectHolder = new VHItem(view);
        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        if (holder instanceof VHItem) {

            final VHItem vhItemHolder=(VHItem)holder;

            userId = contactList.get(position).getId();

            followingStatus=contactList.get(position).getFollowingstatus();

            vhItemHolder.contactName.setText(contactList.get(position).getName());

            if(user.id.equals(userId))
                vhItemHolder.followTxtView.setVisibility(View.GONE);
            else
                vhItemHolder.followTxtView.setVisibility(View.VISIBLE);


            if(contactList.get(position).getPhoto()!= null && !contactList.get(position).getPhoto().isEmpty())

            {
                Picasso.with(activity)
                        .load(contactList.get(position).getPhoto())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.actionbar_profileicon).into(vhItemHolder.circleImageView);

            }
            else
            {
                vhItemHolder.circleImageView.setImageResource(R.drawable.actionbar_profileicon);


            }


            //Follows & UnFollows
            if(checkNow.equals(followingStatus))
            {
                vhItemHolder.followTxtView.setText("Following");

                Log.i("Follower id check1:", followingStatus);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.followTxtView.setTextColor(ContextCompat.getColor(context, R.color.colorWhiteText));
                    vhItemHolder.followTxtView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemColorBackground));
                } else {
                    vhItemHolder.followTxtView.setTextColor(context.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.followTxtView.setBackgroundColor(context.getResources().getColor(R.color.colorItemColorBackground));
                }

                followClicked = true;
            }
            else
            {
                vhItemHolder.followTxtView.setText("+Follow");

                Log.i("Follower id check2:", followingStatus);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.followTxtView.setTextColor(ContextCompat.getColor(context, R.color.colorWhiteText));
                    vhItemHolder.followTxtView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlighted));
                } else {
                    vhItemHolder.followTxtView.setTextColor(context.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.followTxtView.setBackgroundColor(context.getResources().getColor(R.color.colorHighlighted));
                }

                followClicked = false;

            }

            vhItemHolder.followTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followUnfollowParadeRequest(vhItemHolder.followTxtView,contactList.get(position).getId());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView contactName,followTxtView;

        CircleImageView circleImageView;

        public VHItem(View itemView)
        {
            super(itemView);


            contactName=(TextView)itemView.findViewById(R.id.textView_username);
            followTxtView=(TextView)itemView.findViewById(R.id.followTextView_status);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.imageView_followers);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }

        }

    }
    public interface OnItemClickListener
    {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }

    private void followUnfollowParadeRequest(TextView v,String followingId) {
        mConnectionCheck = new ConnectionCheck(activity);
        User user = Utils.getUserFromPreference(activity);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = "";
            if (!followClicked)
                url = ResourceManager.Followmtpuser();
            else
                url = ResourceManager.Unfollowmtpuser();

            url = url + "userId=" + user.id + "&followingId=" + followingId;
            new followUnfollowParade(v).execute(url);
        }
    }

    private class followUnfollowParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        TextView followView;

        public followUnfollowParade(TextView v)
        {
            followView = v;
        }
        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
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
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    if (!followClicked) {
                        followView.setText("Following");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFollowBackground));
                        } else {
                            followView.setBackgroundColor(context.getResources().getColor(R.color.colorFollowBackground));
                        }

                        followClicked = true;
                    } else {
                        followView.setText("+Follow");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFollowBackground));
                        } else {
                            followView.setBackgroundColor(context.getResources().getColor(R.color.colorFollowBackground));
                        }

                        followClicked = false;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }



}
