package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 30/3/16.
 */
public class MyProfileFollowersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Activity activity;

    ImageLoader imageLoader;

    ArrayList<User> contactList;
    static OnItemClickListener mItemClickListener;

    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    boolean follow_clicked,block_clicked;

    public MyProfileFollowersAdapter(Activity activity
            , ArrayList<User> contactList) {
        this.contactList = contactList;
        this.activity = activity;

        //   imageLoader= VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_myprofile_followers, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            final VHItem vhItemHolder = (VHItem) holder;

            // Loading Image using Volley Image Loader (Need Network Image View)
            //  vhItemHolder.imageView.setImageUrl(product.getPrimaryImageUrl(), imageLoader);

            vhItemHolder.contactName.setText(contactList.get(position).contactName);

            if (contactList.get(position).profilePic != null && !contactList.get(position).profilePic.isEmpty()) {
                Picasso.with(activity)
                        .load(contactList.get(position).profilePic)
                        .placeholder(R.drawable.no_image)
                        .into(vhItemHolder.imageView);

            } else {
                vhItemHolder.imageView.setImageResource(R.drawable.contact_icon);

            }
            if(contactList.get(position).followingsStatus.equals("1"))
            {
                vhItemHolder.followTextView.setText("Following");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.followTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorWhiteText));
                    vhItemHolder.followTextView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorItemColorBackground));
                } else {
                    vhItemHolder.followTextView.setTextColor(activity.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.followTextView.setBackgroundColor(activity.getResources().getColor(R.color.colorItemColorBackground));
                }

                follow_clicked = true;
            }
            else  if(contactList.get(position).followingsStatus.equals("0"))
            {
                vhItemHolder.followTextView.setText("+Follow");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.followTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorWhiteText));
                    vhItemHolder.followTextView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorHighlighted));
                } else {
                    vhItemHolder.followTextView.setTextColor(activity.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.followTextView.setBackgroundColor(activity.getResources().getColor(R.color.colorHighlighted));
                }

                follow_clicked = false;

            }


            if(contactList.get(position).chatBlockedStatus.equals("1"))
            {
                vhItemHolder.blockTextView.setText("Unblock");
                block_clicked = true;
            }
            else  if(contactList.get(position).chatBlockedStatus.equals("0"))
            {
                vhItemHolder.blockTextView.setText("Block");
                block_clicked = false;

            }
            vhItemHolder.followTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FollowUnfollowParadeRequest(vhItemHolder.followTextView, contactList.get(position).id);
                }
            });
            vhItemHolder.blockTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockUnblockRequest(vhItemHolder.blockTextView, contactList.get(position).id);
                }
            });

         /*   vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    contactList.remove(position);
                    notifyDataSetChanged();

                }
            });*/


        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, closeImageView;
        TextView contactName, followTextView,blockTextView;

        public VHItem(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.circleImage);
            //    closeImageView=(ImageView) itemView.findViewById(R.id.closeImageView);
            contactName = (TextView) itemView.findViewById(R.id.contactName);
            followTextView = (TextView) itemView.findViewById(R.id.followTextView);
            blockTextView=(TextView) itemView.findViewById(R.id.blockTextView);

            Log.i("", "Adding Listener");
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private void FollowUnfollowParadeRequest(TextView v,String followingId) {
        cd = new ConnectionCheck(activity);
        User user = Utils.getUserFromPreference(activity);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = "";
            if (!follow_clicked)
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
                    if (!follow_clicked) {
                        followView.setText("Following");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorFollowBackground));
                        } else {
                            followView.setBackgroundColor(activity.getResources().getColor(R.color.colorFollowBackground));
                        }

                        follow_clicked = true;
                    } else {
                        followView.setText("+Follow");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorHighlighted));
                        } else {
                            followView.setBackgroundColor(activity.getResources().getColor(R.color.colorHighlighted));
                        }

                        follow_clicked = false;
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


    private void blockUnblockRequest(TextView v,String followingId) {
        cd = new ConnectionCheck(activity);
        User user = Utils.getUserFromPreference(activity);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.blockUser();

            url = url + "userId=" + user.id + "&blockId=" + followingId;
            new blockUnblockFriend(v).execute(url);
        }
    }

    private class blockUnblockFriend extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        TextView blockView;

        public blockUnblockFriend(TextView v)
        {
            blockView = v;
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
                    if (!block_clicked) {
                        blockView.setText("Unblock");
                        block_clicked = true;
                    } else {
                        blockView.setText("Block");
                        block_clicked = false;
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
