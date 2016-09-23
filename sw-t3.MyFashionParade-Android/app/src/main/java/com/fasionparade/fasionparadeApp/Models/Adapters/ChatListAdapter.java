package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Database.DatabaseHandler;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveChatUser;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import java.util.ArrayList;

/**
 * Created by ksuresh on 7/8/2016.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    static Activity activity;

    static OnItemClickListener mItemClickListener;

    ArrayList<ActiveChatUser> activeChatUserArrayList;

    String myId;

    public ChatListAdapter(Activity activity
            ,ArrayList<ActiveChatUser> activeChatUserArrayList

    ) {
        this.activeChatUserArrayList = activeChatUserArrayList;
        this.activity=activity;

        myId= Utils.getUserFromPreference(activity).id;

        //   imageLoader= VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_chat_list_item, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;


            vhItemHolder.userNameTextView.setText(activeChatUserArrayList.get(position).getUserName());
            vhItemHolder.nameTextView.setText(activeChatUserArrayList.get(position).getName());


            final DatabaseHandler databaseHandler=new DatabaseHandler(activity);
            String count=databaseHandler.getReadStatusForUser(myId, activeChatUserArrayList.get(position).getId());




            if(!count.equals("")){
                vhItemHolder.badgeTextView.setVisibility(View.VISIBLE);

                vhItemHolder.badgeTextView.setText(count);


            }else {
                vhItemHolder.badgeTextView.setVisibility(View.GONE);



            }



            vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //delete query based on friend id
                   // DatabaseHandler databaseHandler1=new DatabaseHandler(activity);

                    databaseHandler.deleteChatHistory(myId,activeChatUserArrayList.get(position).id);

                    activeChatUserArrayList.remove(position);
                    notifyDataSetChanged();

                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return activeChatUserArrayList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,closeImageView;
          TextView userNameTextView,nameTextView,badgeTextView;

        public VHItem(View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.thumbImageView);
            closeImageView=(ImageView) itemView.findViewById(R.id.closeImageView);
            //    contactName=(TextView)itemView.findViewById(R.id.contactName);

            userNameTextView=(TextView)itemView.findViewById(R.id.userNameTextView);
            nameTextView=(TextView)itemView.findViewById(R.id.nameTextView);
            badgeTextView=(TextView)itemView.findViewById(R.id.badgeTextView);


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