package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ksuresh on 4/20/2016.
 */

public class MyParadeAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    static Activity activity;

    ImageLoader imageLoader;

    ArrayList<String> contactList;

    public MyParadeAdapter(Activity activity
            , ArrayList<String> contactList

    ) {
        this.contactList = contactList;
        this.activity = activity;

        //    imageLoader = VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_browse_latest_parade, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder = (VHItem) holder;

            // Loading Image using Volley Image Loader (Need Network Image View)
            //  vhItemHolder.imageView.setImageUrl(product.getPrimaryImageUrl(), imageLoader);
            Picasso.with(activity)
                    .load(contactList.get(position))
                    .placeholder(R.drawable.no_image)
                    .into(vhItemHolder.imageView);
          //  vhItemHolder.imageView.setImageResource(contactList.get(position));

            //     vhItemHolder.productDescription.setText(product.getBrand());
            //      vhItemHolder.productPrice.setText(product.getPrice());


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

        }

    }
}