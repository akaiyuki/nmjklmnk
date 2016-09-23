package com.fasionparade.fasionparadeApp.Models.Adapters;

/**
 * Created by root on 30/3/16.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by root on 30/3/16.
 */
public class MyProfileFollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Activity activity;

    ImageLoader imageLoader;

    ArrayList<User> contactList;

    static OnItemClickListener mItemClickListener;


    public MyProfileFollowingAdapter(Activity activity
            ,ArrayList<User> contactList) {
        this.contactList = contactList;
        this.activity=activity;

        //   imageLoader= VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_myprofile_following, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;

            // Loading Image using Volley Image Loader (Need Network Image View)
            //  vhItemHolder.imageView.setImageUrl(product.getPrimaryImageUrl(), imageLoader);
            vhItemHolder.contactName.setText(contactList.get(position).contactName);
            //vhItemHolder.imageView.setImageResource(contactList.get(position));


            if(contactList.get(position).profilePic!= null && !contactList.get(position).profilePic.isEmpty())

            {
                Picasso.with(activity)
                        .load(contactList.get(position).profilePic)
                        .placeholder(R.drawable.no_image)
                        .into(vhItemHolder.imageView);
            }
            else
            {
                vhItemHolder.imageView.setImageResource(R.drawable.contact_icon);

            }
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,closeImageView;
          TextView contactName;

        public VHItem(View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.circleImage);
            contactName=(TextView)itemView.findViewById(R.id.contactName);
            Log.i("", "Adding Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null)
            {
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
