package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Fragments.BrowseFragment;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.MyFavourite;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vishnu on 7/21/2016.
 */
public class MyFavouritesAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    static Activity activity;
    static ArrayList<MyFavourite> contactList;
    static String imageId="";
    ConnectionCheck cd;
    static   User user;
    AlertDialogManager alert = new AlertDialogManager();


    static OnItemClickListener mItemClickListener;


    public MyFavouritesAdapter(Activity activity, ArrayList<MyFavourite> contactList)
    {
        this.contactList = contactList;
        this.activity = activity;
        //    imageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfavourites_items, parent, false);
        VHItem dataObjectHolder = new VHItem(view);
        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem)
        {


            VHItem vhItemHolder = (VHItem) holder;
            final ImageView imageView_inm = vhItemHolder.imageView;

            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            float width = (displayMetrics.widthPixels / 3);  //-left -right;
            Log.i("Width", width + "");
            int height = ((int) (width * 1.2));    //0.692424242)
            Log.i("Height", height + "");

            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams((int)width,height);

            vhItemHolder.imageView.setLayoutParams(layoutParams);


            Picasso.with(activity).load(contactList.get(position).getImageName()).placeholder(R.drawable.photolayer_f)
                    .into(vhItemHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //    Log.i("Home Cat", "Success");
                        }

                        @Override
                        public void onError() {
                            imageView_inm.setImageResource(R.drawable.no_image);
                            //   Log.i("Home Cat", "Error");
                        }
                    });


            imageId =contactList.get(position).getImageid();

            vhItemHolder.deleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    cd = new ConnectionCheck(activity);
                    if (!cd.isConnectingToInternet()) {
                        alert.showAlertDialog(activity, "Internet Connection Error", "Please connect to working Internet connection", false);
                    } else {

                        String url = ResourceManager.DeleteFav();
                        user = Utils.getUserFromPreference(activity);
                        String newURL = url + "userId=" + user.id + "&imageId=" + imageId;
                        Log.i("Delete Response ", newURL);
                        contactList.remove(position);
                        notifyDataSetChanged();
                        new DeleteFav().execute(newURL);

                    }
                }
            });



        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

         ImageView imageView;

          TextView deleteimage;

        public VHItem(View itemView)
        {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView_myfav);
            deleteimage=(TextView)itemView.findViewById(R.id.textView_delete);

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

    public interface OnItemClickListener
    {
        public void onItemClick(View view, int position);


    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }



    static class DeleteFav extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {

                    BrowseFragment.myFavouritesAdapter.notifyDataSetChanged();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();


                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pDialog.cancel();

        }

    }
}