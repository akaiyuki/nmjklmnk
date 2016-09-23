package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 23/3/16.
 */
public class WinnerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    static Activity activity;



    static OnItemClickListener mItemClickListener;
    ArrayList<ActiveParade> contactList;
    public WinnerListAdapter(Activity activity
            ,ArrayList<ActiveParade> contactList

    ) {
        this.contactList = contactList;
        this.activity=activity;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_winner_list_item, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;



            Picasso.with(activity)
                    .load(contactList.get(position).imagePath)
                    .placeholder(R.drawable.no_image)
                    .into(vhItemHolder.imageView);

            vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    contactList.remove(position);
                    notifyDataSetChanged();

                }
            });

            vhItemHolder.paradeName.setText(contactList.get(position).paradeName);
            vhItemHolder.startTime.setText(contactList.get(position).startTime);

        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,closeImageView;
        TextView paradeName,startTime;

        public VHItem(View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.thumbImageView);
            closeImageView=(ImageView) itemView.findViewById(R.id.closeImageView);
            paradeName=(TextView)itemView.findViewById(R.id.paradeName);
            startTime=(TextView)itemView.findViewById(R.id.startTime);


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
