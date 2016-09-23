package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONObject;


public class PushSettings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView img_publicParadeNotification, img_paradeInvitation,
                      img_paradeWinner,img_contactRequest,
                      img_chatMessages,img_newFollowers;


    static String publicParadeNotification="1",paradeInvitation="1",
                  paradeWinner="1",contactRequest="1",
                  chatMessages="1",newFollowers="1";


    private TextView textView_save,Back_text;

    Context context;
    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    User user;
    Boolean is_enablePublicParadeNotification =true;
    Boolean is_enableParadeInvitation =true;
    Boolean is_enableParadeWinner =true;
    Boolean is_enableContactRequest =true;
    Boolean is_enableChatMessages =true;
    Boolean is_enableNewFollowers =true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;








    public static PushSettings newInstance(String param1, String param2) {
        PushSettings fragment = new PushSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PushSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_push_settings, container, false);




        sharedPreferences = getActivity().getSharedPreferences("PUSH_SETTING", Context.MODE_PRIVATE);

        img_publicParadeNotification = (ImageView)view.findViewById(R.id.ImageView_publicParadeNotification);
        img_chatMessages =(ImageView)view.findViewById(R.id.ImageView_chatMessages);
        img_contactRequest =(ImageView)view.findViewById(R.id.ImageView_contactRequest);
        img_newFollowers =(ImageView)view.findViewById(R.id.ImageView_newFollowers);
        img_paradeInvitation =(ImageView)view.findViewById(R.id.ImageView_paradeInvitation);
        img_paradeWinner =(ImageView)view.findViewById(R.id.ImageView_paradeWinner);
        textView_save=(TextView)view.findViewById(R.id.startParadeTextView_save);
        Back_text =(TextView)view.findViewById(R.id.cancelTextView);

        Back_text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                getActivity().onBackPressed();

            }
        });

        boolean status1=sharedPreferences.getBoolean("publicParadeNotification",true);
        boolean status2=sharedPreferences.getBoolean("paradeInvitation",true);
        boolean status3=sharedPreferences.getBoolean("paradeWinner",true);
        boolean status4=sharedPreferences.getBoolean("contactRequest",true);
        boolean status5=sharedPreferences.getBoolean("chatMessages",true);
        boolean status6=sharedPreferences.getBoolean("newFollowers",true);


        if(status1)
        {
            is_enablePublicParadeNotification = true;
            img_publicParadeNotification.setImageResource(R.drawable.on_icon);
            publicParadeNotification="1";

        }
        else
        {
            is_enablePublicParadeNotification = false;
            img_publicParadeNotification.setImageResource(R.drawable.off_icon);
            publicParadeNotification="0";

        }


        if(status2)
        {
            is_enableParadeInvitation = true;
            img_paradeInvitation.setImageResource(R.drawable.on_icon);
            paradeInvitation="1";

        }
        else
        {
            is_enableParadeInvitation = false;
            img_paradeInvitation.setImageResource(R.drawable.off_icon);
            paradeInvitation="0";

        }

        if(status3)
        {
            is_enableParadeWinner = true;
            img_paradeWinner.setImageResource(R.drawable.on_icon);
            paradeWinner="1";

        }
        else
        {
            is_enableParadeWinner = false;
            img_paradeWinner.setImageResource(R.drawable.off_icon);
            paradeWinner="0";

        }

        if(status4)
        {
            is_enableContactRequest = true;
            img_contactRequest.setImageResource(R.drawable.on_icon);
            contactRequest="1";

        }
        else
        {
            is_enableContactRequest = false;
            img_contactRequest.setImageResource(R.drawable.off_icon);
            contactRequest="0";

        }

        if(status5)
        {
            is_enableChatMessages = true;
            img_chatMessages.setImageResource(R.drawable.on_icon);
            chatMessages="1";

        }
        else
        {
            is_enableChatMessages = false;
            img_chatMessages.setImageResource(R.drawable.off_icon);
            chatMessages="0";

        }


        if(status6)
        {
            is_enableNewFollowers = true;
            img_newFollowers.setImageResource(R.drawable.on_icon);
            newFollowers="1";

        }
        else
        {
            is_enableNewFollowers = false;
            img_newFollowers.setImageResource(R.drawable.off_icon);
            newFollowers="0";

        }



        img_publicParadeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if(is_enableParadeInvitation)
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("publicParadeNotification",false);
                    prefsEditor.commit();
                    is_enablePublicParadeNotification = false;
                    img_publicParadeNotification.setImageResource(R.drawable.off_icon);
                    publicParadeNotification="0";
                }
                else
                {   SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("publicParadeNotification",true);
                    prefsEditor.commit();
                    is_enablePublicParadeNotification = true;
                    img_publicParadeNotification.setImageResource(R.drawable.on_icon);
                    publicParadeNotification="1";

                }
            }
        });



        //Parade Invitiation
        img_paradeInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if(is_enableParadeInvitation)
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("paradeInvitation",false);
                    prefsEditor.commit();
                    is_enableParadeInvitation = false;
                    img_paradeInvitation.setImageResource(R.drawable.off_icon);
                    paradeInvitation="0";
                }
                else
                {
                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("paradeInvitation",true);
                    prefsEditor.commit();
                    is_enableParadeInvitation = true;
                    img_paradeInvitation.setImageResource(R.drawable.on_icon);
                    paradeInvitation="1";

                }

            }
        });


        //Contact Request
        img_contactRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(is_enableContactRequest)
                {
                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("contactRequest",false);
                    prefsEditor.commit();
                    is_enableContactRequest = false;
                    img_contactRequest.setImageResource(R.drawable.off_icon);
                    contactRequest="0";
                }
                else
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("contactRequest",true);
                    prefsEditor.commit();
                    is_enableContactRequest = true;
                    img_contactRequest.setImageResource(R.drawable.on_icon);
                    contactRequest="1";

                }

            }
        });




        // New Follower
        img_newFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(is_enableNewFollowers)
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("newFollowers",false);
                    prefsEditor.commit();
                    is_enableNewFollowers = false;
                    img_newFollowers.setImageResource(R.drawable.off_icon);
                    newFollowers="0";
                }
                else
                {
                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("newFollowers",true);
                    prefsEditor.commit();
                    is_enableNewFollowers = true;
                    img_newFollowers.setImageResource(R.drawable.on_icon);
                    newFollowers="1";

                }

            }
        });



        //ParadeWinner
        img_paradeWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(is_enableParadeWinner)
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("paradeWinner",false);
                    prefsEditor.commit();
                    is_enableParadeWinner = false;
                    img_paradeWinner.setImageResource(R.drawable.off_icon);
                    paradeWinner="0";
                }
                else
                {
                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("paradeWinner",true);
                    prefsEditor.commit();
                    is_enableParadeWinner = true;
                    img_paradeWinner.setImageResource(R.drawable.on_icon);
                    paradeWinner="1";

                }

            }
        });


        //Chat Mesages onclick
        img_chatMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(is_enableChatMessages)
                {
                    SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("chatMessages",false);
                    prefsEditor.commit();
                    is_enableChatMessages = false;
                    img_chatMessages.setImageResource(R.drawable.off_icon);
                    chatMessages="0";
                }
                else
                {         SharedPreferences.Editor prefsEditor; prefsEditor = sharedPreferences.edit();
                    prefsEditor.putBoolean("chatMessages",true);
                    prefsEditor.commit();
                    is_enableChatMessages = true;
                    img_chatMessages.setImageResource(R.drawable.on_icon);
                    chatMessages="1";

                }


            }
        });


        textView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getNotificationSetting();
            }
        });
        return  view;

    }


    public  void getNotificationSetting()
    {

        cd = new ConnectionCheck(getActivity());
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.Notification_setting();
            user = Utils.getUserFromPreference(getActivity());
            String newURL = url + "userId=" + user.id + "&notification=" + publicParadeNotification + ","
                    + paradeInvitation + "," +  paradeWinner  + "," + contactRequest + "," + chatMessages + "," + newFollowers;
            Log.i("Notification Setting", newURL);
            new NotificationSetting().execute(newURL);

        }




    }


    @Override
    public void onDetach()
    {
        super.onDetach();

    }

    private class NotificationSetting extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
                String notification;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {


                }
                else
                {
                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pDialog.cancel();

        }

    }

}
