package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 23/3/16.
 */
public class LatestparadesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Activity activity;

    static OnItemClickListener mItemClickListener;

    ArrayList<ActiveParade> contactList;
    boolean from;
    public LatestparadesAdapter(Activity activity,ArrayList<ActiveParade> contactList,boolean from){
        this.contactList = contactList;
        this.activity = activity;
        this.from=from;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_latest_parades_list, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder = (VHItem) holder;
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            float width = (displayMetrics.widthPixels / 3);  //-left -right;
            Log.i("Width", width + "");
            int height = ((int) (width * 1.2));    //0.692424242)
            Log.i("Height", height + "");

            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams((int)width,height);

            vhItemHolder.imageView.setLayoutParams(layoutParams);


            Picasso.with(activity)
                    .load(contactList.get(position).imagePath)
                    .placeholder(R.drawable.no_image)
                    .into(vhItemHolder.imageView);

            if(from){
                vhItemHolder.badge.setVisibility(View.VISIBLE);
            }else{
                vhItemHolder.badge.setVisibility(View.GONE);
            }

        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,badge;


        public VHItem(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            badge=(ImageView)itemView.findViewById(R.id.badge);
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
}