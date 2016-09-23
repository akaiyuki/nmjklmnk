package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;

import com.fasionparade.fasionparadeApp.R;

import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by root on 23/3/16.
 */
public class ActiveParadeListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //Context context;
    static Context activity;
    MediaController mMediaController;
    ImageLoader imageLoader;
    static OnItemClickListener mItemClickListener;
    ArrayList<ActiveParade> contactList;

    ConnectionCheck connectionCheck;

    AlertDialogManager alert = new AlertDialogManager();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int pos;

    public ActiveParadeListAdapter(Context activity
            ,ArrayList<ActiveParade> contactList

    ) {
        this.contactList = contactList;
        this.activity=activity;
        sharedPreferences = activity.getSharedPreferences("Preference",
                Context.MODE_PRIVATE);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_active_parade_item, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            final VHItem vhItemHolder=(VHItem)holder;
            if(contactList.get(position).completedStatus.equals("0")) {
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                try {

                    vhItemHolder.timeLayout.setVisibility(View.VISIBLE);
                    vhItemHolder.paradeName.setVisibility(View.GONE);
                    Date starttime = format.parse(contactList.get(position).startTime);
                    Date endtime = format.parse(contactList.get(position).endTime);

                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(starttime);

                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(endtime);

                    if (endTime.getTimeInMillis() > System.currentTimeMillis()) {
                        long milliseconds = endTime.getTimeInMillis() - System.currentTimeMillis();
                        new CountDownTimer(milliseconds, 1000) {
                            // here comes your code
                            @Override
                            public void onTick(long millis) {
                                int seconds = (int) (millis / 1000) % 60;
                                int minutes = (int) ((millis / (1000 * 60)) % 60);
                                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                                String text = String.format("Remaining %02d:%02d:%02d", hours, minutes, seconds);
                                vhItemHolder.time.setText(text);
                            }

                            @Override
                            public void onFinish() {
                                System.out.println("else");
                                vhItemHolder.time.setText("Voting Duration End");
                            }
                        }.start();
                    } else {
                        vhItemHolder.time.setText("Voting Duration End");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(contactList.get(position).completedStatus.equals("2")) {
                vhItemHolder.timeLayout.setVisibility(View.GONE);
                vhItemHolder.paradeName.setVisibility(View.VISIBLE);
                vhItemHolder.paradeName.setText("No Votes \nClick for options");
            }
            else if(contactList.get(position).completedStatus.equals("3")) {
                vhItemHolder.timeLayout.setVisibility(View.GONE);
                vhItemHolder.paradeName.setVisibility(View.VISIBLE);
                vhItemHolder.paradeName.setText("It's a Draw \n Click for options");
            }

            if(contactList.get(position).fileType.equals("1")){

                vhItemHolder.mVideoView.setVisibility(View.VISIBLE);
                vhItemHolder.imageView.setVisibility(View.GONE);
                Uri uri  = Uri.parse(contactList.get(position).imagePath);
                 vhItemHolder.mVideoView.setVideoURI(uri);
                 vhItemHolder. mVideoView.requestFocus();
                 vhItemHolder. mVideoView.setZOrderOnTop(true);
                 vhItemHolder.mVideoView.start();

//                Bitmap bMap = null;
//                try {
//                    bMap = retriveVideoFrameFromVideo(contactList.get(position).imagePath);
//
//                    vhItemHolder.imageView.setImageBitmap(bMap);
//                } catch (Throwable throwable) {
//
//                    throwable.printStackTrace();
//                }
//


            }else{
                vhItemHolder.mVideoView.setVisibility(View.GONE);
                vhItemHolder.imageView.setVisibility(View.VISIBLE);
                Picasso.with(activity)
                        .load(contactList.get(position).imagePath)
                        .placeholder(R.drawable.no_image)
                        .into(vhItemHolder.imageView);
            }


            vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setMessage("Do you want to delete this contact?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            pos = position;
                            makeDeleteParadeRequest(contactList.get(position).userId, contactList.get(position).paradeId);
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    Dialog dialog = alertDialog.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    dialog.show();

                    pos= position;


                }
            });

            vhItemHolder.startTime.setText(contactList.get(position).formatstartTime);

        }


    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1000,MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {

            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,closeImageView;
        TextView paradeName,startTime,time;
        LinearLayout timeLayout;

         VideoView mVideoView;
        public VHItem(View itemView) {
            super(itemView);

            timeLayout=(LinearLayout) itemView.findViewById(R.id.timeLayout);
            imageView=(ImageView) itemView.findViewById(R.id.thumbImageView);
            closeImageView=(ImageView) itemView.findViewById(R.id.closeImageView);
            paradeName=(TextView)itemView.findViewById(R.id.paradeName);
            startTime=(TextView)itemView.findViewById(R.id.startTime);
            time=(TextView)itemView.findViewById(R.id.time);
            mVideoView = (VideoView)itemView.findViewById(R.id.player);
            itemView.setOnClickListener(this);
           // emVideoView.setOnPreparedListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
        public void onPrepared() {
            //Starts the video playback as soon as it is ready

        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private void makeDeleteParadeRequest(String userId,String paradeId) {
        connectionCheck = new ConnectionCheck(activity);
        User user = Utils.getUserFromPreference(activity);
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.deleteParade();
            url = url + "userId=" + user.id + "&paradeId=" +paradeId;
            if(sharedPreferences.getInt("VoteCount",0)!=0) {
                editor = sharedPreferences.edit();
                if (!sharedPreferences.getBoolean("viewedParade" + paradeId, false)) {
                    editor.putInt("VoteCount", (sharedPreferences.getInt("VoteCount", 0) - 1));
                }
                editor.commit();
            }
            new deleteParade().execute(url);
        }
    }

    private class deleteParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String message;
                message = jsonObject.getString("message");
                contactList.remove(pos);
                notifyDataSetChanged();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

}
