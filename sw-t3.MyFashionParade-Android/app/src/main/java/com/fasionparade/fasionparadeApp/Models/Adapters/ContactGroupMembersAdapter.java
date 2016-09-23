package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import com.fasionparade.fasionparadeApp.Functions.Object.GroupMember;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ksuresh on 7/19/2016.
 */
public class ContactGroupMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    Context context;
    static Activity activity;

    ImageLoader imageLoader;

    ContactGroupMembersAdapter adapter;


    ArrayList<GroupMember> groupMemberList; //GroupMember
    String groupId;
    String adminID;


    AlertDialogManager alert = new AlertDialogManager();

    public ContactGroupMembersAdapter(Activity activity
            ,ArrayList<GroupMember> groupMemberList , String groupId , String adminID) {
        this.groupMemberList = groupMemberList;
        this.groupId=groupId;
        this.adminID=adminID;

        this.activity = activity;
        adapter=this;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_contact_group_member_item, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;
            vhItemHolder.contactName.setText(groupMemberList.get(position).name);
            vhItemHolder.phoneNumber.setText(groupMemberList.get(position).phoneNumber);


            if(!groupMemberList.get(position).memberId.equals(adminID)) {  //other members close Image

                vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteMemberFromGroup(groupMemberList.get(position).memberId,position);
                    }
                });
            }else { //admin close Image
                vhItemHolder.closeImageView.setVisibility(View.GONE);
            }



        }

    }

    @Override
    public int getItemCount() {
        return groupMemberList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactName,phoneNumber;

        ImageView closeImageView;

        public VHItem(View itemView) {
            super(itemView);
            contactName=(TextView)itemView.findViewById(R.id.contactName);
            phoneNumber=(TextView)itemView.findViewById(R.id.phoneNumber);

            closeImageView=(ImageView)itemView.findViewById(R.id.closeImageView);

            Log.i("", "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

    }







    //////////////
    public void deleteMemberFromGroup(String memberId,int position){

        ConnectionCheck cd = new ConnectionCheck(activity);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.deletecontact();
            User user = Utils.getUserFromPreference(activity);

            String newURL = url + "userId=" + user.id + "&groupId=" + groupId +"&contactId=" + memberId;

            Log.i("Del Group Mem URL", newURL);

            new DeleteMemberFromGroupService(position).execute(newURL);
        }

    }


    private class DeleteMemberFromGroupService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        int position;

        public DeleteMemberFromGroupService(int position){

            this.position=position;

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

            Log.i("Del Group Mem Resp", result);

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {

                    groupMemberList.remove(position);
                    adapter.notifyDataSetChanged();


                    message=jsonObject.getString("message");
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    //listview datesetchanged

                } else {

                    message=jsonObject.getString("message");
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    Toast.makeText(activity,
                            message , Toast.LENGTH_SHORT)
                            .show();

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
