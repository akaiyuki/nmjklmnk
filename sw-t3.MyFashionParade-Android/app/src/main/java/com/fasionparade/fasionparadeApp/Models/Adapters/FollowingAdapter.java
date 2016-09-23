package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishnu on 8/1/2016.
 */
public class FollowingAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    Context context;
    static Activity activity;
    ImageLoader imageLoader;
    static ArrayList<Contacts> contactList;
    static String userId="";
    static OnItemClickListener mItemClickListener;
    static String followingStatus="",followerId="";
    static  String checkNow ="1";
    User user;


    public FollowingAdapter (Activity activity,ArrayList<Contacts> contactList) {
        this.contactList = contactList;
        this.activity=activity;
        user = Utils.getUserFromPreference(activity);


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);
        VHItem dataObjectHolder = new VHItem(view);
        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;

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
                        .error(R.drawable.actionbar_profileicon)
                        .into(vhItemHolder.circleImageView);

            }
            else
            {
                vhItemHolder.circleImageView.setImageResource(R.drawable.actionbar_profileicon);


            }



                vhItemHolder.followTxtView.setText("Following");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    vhItemHolder.followTxtView.setTextColor(ContextCompat.getColor(context, R.color.colorWhiteText));
                    vhItemHolder.followTxtView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemColorBackground));
                } else {
                    vhItemHolder.followTxtView.setTextColor(context.getResources().getColor(R.color.colorWhiteText));
                    vhItemHolder.followTxtView.setBackgroundColor(context.getResources().getColor(R.color.colorItemColorBackground));
                }

                Log.i("Follower id check1:", followingStatus);

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


            contactName=(TextView)itemView.findViewById(R.id.textView_username_following);
            followTxtView=(TextView)itemView.findViewById(R.id.followTextView_statusfollowing);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.imageView_following);


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



}
