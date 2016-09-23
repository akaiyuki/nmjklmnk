package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.InboxPageActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Fragments.InboxFragment;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 24/3/16.
 */
public class InboxListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Activity activity;

    static OnItemClickListener mItemClickListener;

    ArrayList<ActiveParade> productList;

    ConnectionCheck cd;

    AlertDialogManager alert = new AlertDialogManager();
    int pos;
    String status;
    User user;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public InboxListAdapter(Activity activity
            , ArrayList<ActiveParade> productList

    ) {
        this.productList = productList;
        this.activity = activity;
        pref = activity.getSharedPreferences("Preference",
                Context.MODE_PRIVATE);
        user = Utils.getUserFromPreference(activity);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_inbox_list_item, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            final VHItem vhItemHolder = (VHItem) holder;
            final ActiveParade list = productList.get(position);

            if(list.type.equals("parade")) {
                status ="Inbox";
                System.out.println("Time in minutes: " + list.startTime + "---" + list.endTime);
                vhItemHolder.viewParadeTextView.setVisibility(View.VISIBLE);
                vhItemHolder.contactLayout.setVisibility(View.GONE);
                vhItemHolder.timeLayout.setVisibility(View.VISIBLE);

                vhItemHolder.text.setText("You have received a Parade invite");
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                try {

                    Date starttime = format.parse(list.startTime);
                    Date endtime = format.parse(list.endTime);

                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(starttime);

                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(endtime);

                    if (endTime.getTimeInMillis() > System.currentTimeMillis()) {
                        long milliseconds = endTime.getTimeInMillis() - System.currentTimeMillis();
                        System.out.println("milliseconds " + milliseconds);
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
                                vhItemHolder.time.setText("Voting Duration End");
                            }
                        }.start();
                    } else {
                        vhItemHolder.time.setText("Voting Duration End");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                vhItemHolder.paradeName.setText(list.userName);

                if (!list.profilePic.equals("")) {
                    Picasso.with(activity)
                            .load(list.profilePic)
                            .placeholder(R.drawable.actionbar_profileicon)
                            .into(vhItemHolder.imageView);
                }

                vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                        alertDialog.setMessage("Do you want to delete this parade?");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                pos = position;
                                makedeleteInviteRequest(user.id, list.paradeId);
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


                    }
                });

                vhItemHolder.viewParadeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pref.getInt("InvitesCount",0)!=0) {
                            editor = pref.edit();
                            if (!pref.getBoolean("viewed" + list.paradeId, false)) {
                                editor.putInt("InvitesCount", (pref.getInt("InvitesCount", 0) - 1));
                            }
                            editor.putBoolean("viewed" + list.paradeId, true);
                            editor.commit();
                        }
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(InboxFragment.myParadeList);
                        FashionHomeActivity.parade_clicked=false;
                        Intent intent = new Intent(activity, InboxPageActivity.class);
                        intent.putExtra("data", jsonData);
                        intent.putExtra("jsonArray", String.valueOf(InboxFragment.myParadeList.get(position).imagePathJson));
                        intent.putExtra("index", String.valueOf(position));
                        activity.startActivity(intent);
                    }
                });

            }else if(list.type.equals("contact"))
            {
                status = "Contact";
                vhItemHolder.viewParadeTextView.setVisibility(View.GONE);
                vhItemHolder.contactLayout.setVisibility(View.VISIBLE);
                vhItemHolder.timeLayout.setVisibility(View.GONE);

                vhItemHolder.text.setText("You have received a Contact request");
                vhItemHolder.paradeName.setText(list.userName);

                if (!list.profilePic.equals("")) {
                    Picasso.with(activity)
                            .load(list.profilePic)
                            .placeholder(R.drawable.actionbar_profileicon)
                            .into(vhItemHolder.imageView);
                }

                vhItemHolder.declineTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pos = position;
                        makeAcceptDeclineRequest(list.friendId, "2");
                    }
                });
                vhItemHolder.acceptTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pos = position;
                        makeAcceptDeclineRequest(list.friendId,"1");

                    }
                });
                vhItemHolder.closeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                        alertDialog.setMessage("Do you want to delete this contact?");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                pos = position;
                                makeAcceptDeclineRequest(list.friendId, "2");
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

                    }
                });
            }


        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, closeImageView;
        TextView viewParadeTextView, time, paradeName,text;
        LinearLayout contactLayout,timeLayout;
        TextView declineTextView,acceptTextView;

        public VHItem(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.thumbImageView);
            closeImageView = (ImageView) itemView.findViewById(R.id.closeImageView);
            viewParadeTextView = (TextView) itemView.findViewById(R.id.viewParadeTextView);
            time = (TextView) itemView.findViewById(R.id.time);
            paradeName = (TextView) itemView.findViewById(R.id.paradeName);
            text = (TextView) itemView.findViewById(R.id.text);
            contactLayout =(LinearLayout) itemView.findViewById(R.id.contactLayout);
            timeLayout =(LinearLayout) itemView.findViewById(R.id.timeLayout);
            declineTextView =(TextView) itemView.findViewById(R.id.declineTextView);
            acceptTextView =(TextView) itemView.findViewById(R.id.acceptTextView);


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

    private void makeAcceptDeclineRequest(String friendId,String confirmStatus) {
        cd = new ConnectionCheck(activity);
        User user = Utils.getUserFromPreference(activity);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.acceptContact();
            url = url + "userId=" + user.id + "&friendId=" +friendId+"&confirmStatus="+confirmStatus;
            new AcceptDeclineContact().execute(url);
        }
    }

    private void makedeleteInviteRequest(String userId,String paradeId) {
        cd = new ConnectionCheck(activity);
        User user = Utils.getUserFromPreference(activity);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(activity, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.deleteInbox();
            url = url + "userId=" + userId + "&paradeId=" +paradeId;
            if(pref.getInt("InvitesCount",0)!=0) {
                editor = pref.edit();
                if (!pref.getBoolean("viewed" + paradeId, false)) {
                    editor.putInt("InvitesCount", (pref.getInt("InvitesCount", 0) - 1));
                }
                editor.commit();
            }
            new AcceptDeclineContact().execute(url);
        }
    }

    private class AcceptDeclineContact extends AsyncTask<String, Void, String> {
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
                productList.remove(pos);
                notifyDataSetChanged();
                if(status.equals("Contact")) {
                    if(pref.getInt("InvitesCount",0)!=0) {
                        editor = pref.edit();
                        editor.putInt("InvitesCount", (pref.getInt("InvitesCount", 0) - 1));
                        editor.commit();
                    }
                }
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
