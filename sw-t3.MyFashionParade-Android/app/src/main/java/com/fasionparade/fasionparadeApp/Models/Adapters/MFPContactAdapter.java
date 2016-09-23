package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.Models.Activity.ChatDetailActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ksuresh on 7/19/2016.
 */
public class MFPContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    Context context;
    static Activity activity;
    ImageLoader imageLoader;
    static ArrayList<Contacts> contactList;
    static String Userid="";


    static OnItemClickListener mItemClickListener;


    public MFPContactAdapter(Activity activity,ArrayList<Contacts> contactList) {
        this.contactList = contactList;
        this.activity=activity;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_mfp_contact_item, parent, false);
        VHItem dataObjectHolder = new VHItem(view);
        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position)
    {

        if (holder instanceof VHItem)
        {

            VHItem vhItemHolder=(VHItem)holder;

            Userid = contactList.get(position).getId();
            //vhItemHolder.imageView.setImageResource(contactList.get(position).photo);
            vhItemHolder.contactName.setText(contactList.get(position).getName());
            vhItemHolder.phoneNumber.setText("" + contactList.get(position).getPhone());
            if(contactList.get(position).getPhoto()!= null && !contactList.get(position).getPhoto().isEmpty())

            {

                Picasso.with(activity)
                        .load(contactList.get(position).getPhoto())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.actionbar_profileicon)
                        .into(vhItemHolder.circleImage);

            }
            else
            {
                vhItemHolder.circleImage.setImageResource(R.drawable.contact_icon);

            }

            vhItemHolder.chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    Intent intent = new Intent(activity, ChatDetailActivity.class);
                    intent.putExtra("RECEIVER_ID", contactList.get(position).getId());
                    intent.putExtra("NAME", contactList.get(position).getName());
                    activity.startActivity(intent);

                }
            });


            vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {



                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want delete this?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.img_delete_icon);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            // Write your code here to invoke YES event
                            contactList.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
            });




        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView circleImage;
        RelativeLayout chatLayout;
        ImageView closeImageView;
        TextView contactName,phoneNumber;

        public VHItem(View itemView) {
            super(itemView);

            circleImage=(CircleImageView)itemView.findViewById(R.id.circleImage_mfpuser);
            chatLayout=(RelativeLayout)itemView.findViewById(R.id.chatLayout);
            closeImageView=(ImageView) itemView.findViewById(R.id.closeImageView_mfpcontatadapter);

            contactName=(TextView)itemView.findViewById(R.id.contactName);
            phoneNumber=(TextView)itemView.findViewById(R.id.phoneNumber);


            Log.i("", "Adding Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
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
