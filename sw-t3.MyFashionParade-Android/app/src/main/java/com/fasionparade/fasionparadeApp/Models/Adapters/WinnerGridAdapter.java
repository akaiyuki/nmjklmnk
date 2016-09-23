package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fasionparade.fasionparadeApp.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 23/3/16.
 */
public class WinnerGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Activity activity;

    static OnItemClickListener mItemClickListener;

    ArrayList<String> contactList;
    List<String> rankstatus;

    public WinnerGridAdapter(Activity activity
            , ArrayList<String> contactList,List<String> rankstatus

    ) {
        this.contactList = contactList;
        this.rankstatus = rankstatus;
        this.activity = activity;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_winner_grid_item, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder = (VHItem) holder;


            Picasso.with(activity)
                    .load(contactList.get(position))
                    .placeholder(R.drawable.no_image)
                    .into(vhItemHolder.imageView);
            if(rankstatus.get(position).equals("1"))
                vhItemHolder.winnerBadgeImageView.setImageResource(R.drawable.winnerbadge);
            else if(rankstatus.get(position).equals("2"))
                vhItemHolder.winnerBadgeImageView.setImageResource(R.drawable.winnerbadge_second);
            else if(rankstatus.get(position).equals("=2"))
                vhItemHolder.winnerBadgeImageView.setImageResource(R.drawable.winnerbadge_second_equal);
            else if(rankstatus.get(position).equals("3"))
                vhItemHolder.winnerBadgeImageView.setImageResource(R.drawable.winnerbadge_third);
            else if(rankstatus.get(position).equals("=3"))
                vhItemHolder.winnerBadgeImageView.setImageResource(R.drawable.winnerbadge_third);
            else if(rankstatus.get(position).equals(""))
                vhItemHolder.winnerBadgeImageView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
       // Toast.makeText(activity,String.valueOf(contactList.size()),Toast.LENGTH_SHORT).show();
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,winnerBadgeImageView;


        public VHItem(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            winnerBadgeImageView=(ImageView)itemView.findViewById(R.id.winnerBadgeImageView);


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