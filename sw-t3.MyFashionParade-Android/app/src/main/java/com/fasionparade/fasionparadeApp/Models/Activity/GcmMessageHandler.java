package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Database.DatabaseHandler;
import com.fasionparade.fasionparadeApp.Models.Fragments.InboxFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.MyParadesFragment;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Object.ChatDetail;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ksuresh on 4/5/2016.
 */
public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    String senderId;
    String receiverId = "";

    String messageText;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        try {
            Log.i("From", from);
            Log.i("Bundle", data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String message = data.getString("message");
        Log.i("mess string", message);

        String title = data.getString("title");
        Log.i("Title",title);

        sharedPreferences = getApplicationContext().getSharedPreferences("Preference",
                Context.MODE_PRIVATE);

        if(title.equals("Text Message")  || title.equals("Image Message")) {   //position 2  (starts from 0)

            try {
                //Receiver id from shared preference

                User user = Utils.getUserFromPreference(GcmMessageHandler.this);

                receiverId = user.id;

                //current time in millis


                //json from bundle
                //        JSONObject jsonObject = new JSONObject(message);

                senderId = data.getString("senderId");
                messageText = data.getString("message");
                messageText = messageText.replace("+", " ");

                String userDetailsString = data.getString("userDetails");
                JSONObject userDetailsJSONObject = new JSONObject(userDetailsString);

                String userName = userDetailsJSONObject.getString("userName");
                String name = userDetailsJSONObject.getString("name");


                Log.i("Title", title);


                //Time
                long milliSec = System.currentTimeMillis();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(milliSec);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh.mm a");


                //Checking current visible activity
                Activity currentActivity = ((AppController) getApplicationContext()).getCurrentActivity();
                if (currentActivity != null && currentActivity instanceof ChatDetailActivity) {  //Chat Activity opened
                    Log.i("aa","Entered");


                    ChatDetailActivity chatDetailActivity = (ChatDetailActivity) currentActivity;

                    if (senderId.equals(chatDetailActivity.receiverId)) { //current activity is chatdetail and same user
                        Log.i("If", "ent");
                        if (title.equals("Text Message")) {  //Text
                            Log.i("Text", "Entered");

                            DatabaseHandler databaseHandler = new DatabaseHandler(GcmMessageHandler.this);
                            databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, userName, name, messageText, "0", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "READ"));

                        } else {  //Image
                            Log.i("Image", "Entered");

                            DatabaseHandler databaseHandler = new DatabaseHandler(GcmMessageHandler.this);
                            databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, userName, name, messageText, "1", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "READ"));

                        }


                        chatDetailActivity.updateData(senderId);


                    } else {  //current activity is chatdetails page but different user
                        Log.i("else", "ent");

                        if (title.equals("Text Message")) {  //Text
                            Log.i("Text", "Entered");

                            DatabaseHandler databaseHandler = new DatabaseHandler(GcmMessageHandler.this);
                            databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, userName, name, messageText, "0", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "UNREAD"));

                        } else {  //Image
                            Log.i("Image", "Entered");

                            DatabaseHandler databaseHandler = new DatabaseHandler(GcmMessageHandler.this);
                            databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, userName, name, messageText, "1", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "UNREAD"));
                            //      databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, "c", "cc", messageText, "1", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "UNREAD"));

                        }


                        //createNotification("MFP", message);
                        setNotification("MFP", messageText,"TWO_CHAT");

                    }


                } else {//wh3en the current activity is not chatdetail page
                    Log.i("11","Entered");




                    //here always status is unread
                    if (title.equals("Text Message")) {  //Text
                        Log.i("Text", "Entered");

                        DatabaseHandler databaseHandler = new DatabaseHandler(GcmMessageHandler.this);
                        databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, userName, name, messageText, "0", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "UNREAD"));

                    } else {  //Image
                        Log.i("Image", "Entered");

                        DatabaseHandler databaseHandler = new DatabaseHandler(GcmMessageHandler.this);
                        databaseHandler.addChat(new ChatDetail(senderId, receiverId, receiverId, senderId, userName, name, messageText, "1", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "RECEIVE", "UNREAD"));

                    }


                    //checking current activity is home Activity or not
                    if (currentActivity != null && currentActivity instanceof FashionHomeActivity) {

                        Log.i("aaa if", "entered");


                        FashionHomeActivity fashionHomeActivity = (FashionHomeActivity) currentActivity;

                        fashionHomeActivity.updateData();


                        if (((AppController) getApplicationContext()).getInboxFragment() != null) {
                            Log.i("bbb if", "entered");

                            if (((AppController) getApplicationContext()).getInboxFragment() instanceof InboxFragment) {
                                Log.i("ccc if", "entered");
                                ((AppController) getApplicationContext()).getInboxFragment().update();

                            } else {
                                Log.i("ccc else", "entered");

                            }

                        } else {
                            Log.i("bbb else", "entered");

                        }


                    } else {
                        Log.i("aaa else", "entered");

                    }


                    //     createNotification("MFP", messageText);
                    setNotification("MFP", messageText,"TWO_CHAT");

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }else if(title.equals("Parade Invitation")  || title.equals("contact request")){   // position 2  (starts from 0)   invite
                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putInt("InvitesCount",(sharedPreferences.getInt("InvitesCount", 0)+1));
                sharedPreferencesEditor.commit();

            messageText = data.getString("message");
            messageText = messageText.replace("+", " ");

            Activity currentActivity = ((AppController) getApplicationContext()).getCurrentActivity();
            //checking current activity is home Activity or not
            if (currentActivity != null && currentActivity instanceof FashionHomeActivity) {

                Log.i("aaa if", "entered");


                FashionHomeActivity fashionHomeActivity = (FashionHomeActivity) currentActivity;

                fashionHomeActivity.updateData();


                if (((AppController) getApplicationContext()).getInboxFragment() != null) {
                    Log.i("bbb if", "entered");

                    if (((AppController) getApplicationContext()).getInboxFragment() instanceof InboxFragment) {
                        Log.i("ccc if", "entered");
                        ((AppController) getApplicationContext()).getInboxFragment().update();

                    } else {
                        Log.i("ccc else", "entered");

                    }

                } else {
                    Log.i("bbb else", "entered");

                }


            }
            setNotification("MFP", messageText,"TWO_INVITE");


        }else if(title.equals("Follower Added"))  {  //  main page   //position 0  (starts from 0)
            messageText = data.getString("message");
            messageText = messageText.replace("+", " ");
            setNotification("MFP", messageText,"ZERO_FOLLOW");


        }else if(title.equals("Winner") || title.equals("vote") ){

            messageText = data.getString("message");
            messageText = messageText.replace("+", " ");
            setNotification("MFP", messageText,"ONE_WINNER_OR_VOTE");

            sharedPreferencesEditor = sharedPreferences.edit();
            if( title.equals("vote")) {
                sharedPreferencesEditor.putInt("VoteCount", (sharedPreferences.getInt("VoteCount", 0) + 1));
            }
            if(title.equals("Winner"))
            {
                sharedPreferencesEditor.putInt("WinnerCount", (sharedPreferences.getInt("WinnerCount", 0) + 1));
            }
            sharedPreferencesEditor.commit();

            messageText = data.getString("message");
            messageText = messageText.replace("+", " ");

            Activity currentActivity = ((AppController) getApplicationContext()).getCurrentActivity();
            //checking current activity is home Activity or not
            if (currentActivity != null && currentActivity instanceof FashionHomeActivity) {

                Log.i("aaa if", "entered");


                FashionHomeActivity fashionHomeActivity = (FashionHomeActivity) currentActivity;

                fashionHomeActivity.updateParadeData();


                if (((AppController) getApplicationContext()).getMyParadesFragment() != null) {
                    Log.i("bbb if", "entered");

                    if (((AppController) getApplicationContext()).getMyParadesFragment() instanceof MyParadesFragment) {
                        Log.i("ccc if", "entered");
                        ((AppController) getApplicationContext()).getMyParadesFragment().update();

                    } else {
                        Log.i("ccc else", "entered");

                    }

                } else {
                    Log.i("bbb else", "entered");

                }


            }// position 1  (starts from 0)   My Parade


        }




    }


    // Creates notification based on title and body received

    private void createNotification(String title, String body) {

        Context context = getBaseContext();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)

                .setContentText(body);

        NotificationManager mNotificationManager = (NotificationManager) context

                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }






    private void setNotification(String title,String notificationMessage,String positionFrom) {


        int requestID = (int) System.currentTimeMillis();

  //      Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager  = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(), FashionHomeActivity.class);

        Log.i("Position from",positionFrom);

        if(positionFrom.equals("TWO_CHAT"))
        notificationIntent.putExtra("FROM","INBOX_CHAT_NOTIFICATION");

        if(positionFrom.equals("TWO_INVITE"))
            notificationIntent.putExtra("FROM","INBOX_INVITE_NOTIFICATION");

        if(positionFrom.equals("ZERO_FOLLOW"))
            notificationIntent.putExtra("FROM","MAIN_PAGE_NOTIFICATION");

        if(positionFrom.equals("ONE_WINNER_OR_VOTE"))
            notificationIntent.putExtra("FROM","MY_PARADE_NOTIFICATION");


        //      **add this line**
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

  //      **edit this line to put requestID as requestCode**
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setContentText(notificationMessage).setAutoCancel(true);
   //     mBuilder.setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());

    }

}
