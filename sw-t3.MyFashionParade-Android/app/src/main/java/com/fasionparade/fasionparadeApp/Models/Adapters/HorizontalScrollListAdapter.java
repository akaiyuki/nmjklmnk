package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 30/3/16.
 */
public class HorizontalScrollListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    static Activity activity;
    static OnItemClickListener mItemClickListener;
    ImageLoader imageLoader;

    ArrayList<String> contactList;

    public HorizontalScrollListAdapter(Activity activity
            , ArrayList<String> contactList

    ) {
        this.contactList = contactList;
        this.activity = activity;

        //    imageLoader = VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_voting_horizontal_list, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder = (VHItem) holder;
            int spacing= activity.getResources().getDimensionPixelSize(R.dimen.spacing);
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            float width = (displayMetrics.widthPixels / 4) - ((spacing*2)/4) -(  ((spacing/2)*3)/4  )  ;  //-left -right;
            Log.i("Width", width + "");
            int height = ((int) (width * 1.2));    //0.692424242)
            Log.i("Height", height + "");
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams((int)width,height);

            vhItemHolder.imageView.setLayoutParams(layoutParams);
            Picasso.with(context)
                    .load(contactList.get(position))
                    .placeholder(R.drawable.no_image)
                    .into(vhItemHolder.imageView);


        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        //  TextView contactName;

        public VHItem(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //    contactName=(TextView)itemView.findViewById(R.id.contactName);

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
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}