package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.Models.Activity.NewParadeActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;

import java.util.ArrayList;

/**
 * Created by root on 23/3/16.
 */
public class NewParadeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    static Context mContext;

    ImageLoader imageLoader;

    ArrayList<Uri> contactList;
    String userType;
    RelativeLayout.LayoutParams layoutParams;

    public NewParadeAdapter(Context activity ,ArrayList<Uri> contactList,String userType) {
        this.contactList = contactList;
        this.mContext=activity;
        this.userType=userType;
    //    imageLoader= VolleySingleton.getInstance(context).getImageLoader();



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_new_parade_grid, parent, false);

        VHItem dataObjectHolder = new VHItem(view);


        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;

            // Loading Image using Volley Image Loader (Need Network Image View)
            //  vhItemHolder.imageView.setImageUrl(product.getPrimaryImageUrl(), imageLoader);




            if(contactList.size()==0  || (contactList.size()<position+1)){

                if(userType.equals("0"))
                vhItemHolder.imageView.setImageResource(R.drawable.photolayer_f);
                else if(userType.equals("1"))
                    vhItemHolder.imageView.setImageResource(R.drawable.photolayer_p);

                vhItemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((NewParadeActivity)mContext).cameraViewOpen(userType,mContext);
                        //NewParadeActivity.cameraViewOpen(userType);
                    }
                });

                vhItemHolder.closeImageView.setVisibility(View.GONE);

                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                float width = (displayMetrics.widthPixels / 3);  //-left -right;
                Log.i("Width", width + "");
                int height = ((int) (width * 1.2));    //0.692424242)
                Log.i("Height", height + "");

                layoutParams = new RelativeLayout.LayoutParams((int) width, height);
                //         layoutParams.setMargins(left,top,right,bottom);
                vhItemHolder.imageView.setLayoutParams(layoutParams);

                vhItemHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);





            }else {


                int left = dpToPx(10);
                int right = dpToPx(10);
                int top = dpToPx(10);
                int bottom = dpToPx(10);

                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                float width = (displayMetrics.widthPixels / 3);  //-left -right;
                Log.i("Width", width + "");
                int height = ((int) (width * 1.2));    //0.692424242)
                Log.i("Height", height + "");

                layoutParams = new RelativeLayout.LayoutParams((int) width, height);
                //         layoutParams.setMargins(left,top,right,bottom);
                vhItemHolder.imageView.setLayoutParams(layoutParams);
                vhItemHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                vhItemHolder.imageView.setImageURI(Uri.parse(contactList.get(position).toString()));

                vhItemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                vhItemHolder.closeImageView.setVisibility(View.VISIBLE);

                vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                      //  contactList.remove(position);
                        Flag.totalImageUris.remove(position);
                        Flag.totalImage.remove(position);
                        notifyDataSetChanged();

                    }
                });





            }

        }


    }

    @Override
    public int getItemCount() {


            return 6;


    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,closeImageView;
        //    TextView contactName;

        public VHItem(View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.paradeImageView);
            closeImageView=(ImageView)itemView.findViewById(R.id.closeImageView);
            //    contactName=(TextView)itemView.findViewById(R.id.contactName);

            Log.i("", "Adding Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {




        }

    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }






}
