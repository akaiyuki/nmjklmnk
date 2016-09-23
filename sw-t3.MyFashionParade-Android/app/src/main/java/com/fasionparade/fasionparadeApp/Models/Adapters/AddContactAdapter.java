package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.FindFriend;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishnu on 7/25/2016.
 */

public class AddContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    Context context;
    ArrayList<FindFriend> contactList;
    static Activity activity;
    static String followingStatus="",followerId="";
    static User user;
    AlertDialogManager alert = new AlertDialogManager();
    static   String checkNow = "1";


    public AddContactAdapter(Activity activity, ArrayList<FindFriend> contactList)
    {
        this.contactList = contactList;

        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addcontact_items, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder = (VHItem) holder;


            followingStatus=contactList.get(position).getFriendfollowingStatus();

            followerId =contactList.get(position).getFriendid();


            Log.i("foll s",followingStatus);

            //Follows & UnFollows
            if (checkNow.equals(followingStatus))
            {
                vhItemHolder.Follows.setText("Friend");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.Follows.setTextColor(ContextCompat.getColor(context, R.color.colorWhiteText));
                    vhItemHolder.Follows.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemColorBackground));
                } else {
                    vhItemHolder.Follows.setTextColor(context.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.Follows.setBackgroundColor(context.getResources().getColor(R.color.colorItemColorBackground));
                }
            }
            else
            {
                vhItemHolder.Follows.setText("+ADD");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.Follows.setTextColor(ContextCompat.getColor(context, R.color.colorWhiteText));
                    vhItemHolder.Follows.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlighted));
                } else {
                    vhItemHolder.Follows.setTextColor(context.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.Follows.setBackgroundColor(context.getResources().getColor(R.color.colorHighlighted));
                }

            }

            if(contactList.get(position).getFriendProfilePic() != null && !contactList.get(position).getFriendProfilePic().isEmpty())
            {


                Picasso.with(activity)
                        .load(contactList.get(position).getFriendProfilePic())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.actionbar_profileicon)
                        .into(vhItemHolder.circleImage);

            }
            else
            {

                vhItemHolder.circleImage.setImageResource(R.drawable.actionbar_profileicon);

            }








            vhItemHolder.UserName.setText(contactList.get(position).getFriendUserName());
            vhItemHolder.Name.setText(contactList.get(position).getFriendName());




        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Name, UserName, Follows;
        ConnectionCheck cd;
        CircleImageView circleImage;
        User user;
        String strI = "";
        AlertDialogManager alert = new AlertDialogManager();

        public VHItem(View itemView) {
            super(itemView);



            circleImage=(CircleImageView)itemView.findViewById(R.id.thumbImageView_addcontact);
            Name = (TextView) itemView.findViewById(R.id.contactName_addcontact);
            UserName = (TextView) itemView.findViewById(R.id.userName_addcontact);
            Follows = (TextView) itemView.findViewById(R.id.followTextView_addcontac);

            Follows.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            int itemPosition = getAdapterPosition();//mRecyclerView.getChildPosition(view);
            strI = Integer.toString(itemPosition + 1);


            Log.i("item Position:", strI);
            Log.i("Follower id:", followerId);


            if (Follows.getText().toString().equals("Friend"))
            {

            }
            else
            {
                getAddContactMethod();
            }
        }


        public void getAddContactMethod() {


            cd = new ConnectionCheck(activity);
            if (!cd.isConnectingToInternet()) {
                alert.showAlertDialog(activity, "Internet Connection Error", "Please connect to working Internet connection", false);
            } else {
                String url = ResourceManager.Addcontact();
                user = Utils.getUserFromPreference(activity);
                String newURL = url + "userId=" + user.id + "&friendId=" + followerId;
                Log.i("Add Contact :", newURL);
                new AddContact().execute(newURL);
            }
        }


        private class AddContact extends AsyncTask<String, Void, String> {
            ProgressDialog pDialog;


            @SuppressWarnings("static-access")
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(activity);
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

                        Follows.setText("Friend");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            Follows.setTextColor(ContextCompat.getColor(activity, R.color.colorWhiteText));
                            Follows.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorItemColorBackground));
                        } else {
                            Follows.setTextColor(activity.getResources().getColor(R.color.colorWhiteText));
                            Follows.setBackgroundColor(activity.getResources().getColor(R.color.colorItemColorBackground));
                        }

                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                pDialog.cancel();

            }

        }


    }
}
