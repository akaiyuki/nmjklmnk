package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Models.Adapters.MyParadeAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class InboxPageProfileActivity extends ActionBarActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    MyParadeAdapter newParadeAdapter;
    ArrayList<String> productList;
    ImageView backImageView, profileImageView,paradeProfileImage;
    TextView paradeProfileName, followTextView, exitParadeTextView,setDurationTextView;

    RelativeLayout rowOneLayout, rowTwoLayout;
    LinearLayout rowOneEvenLayout, rowOneOddLayout;
    ImageView rowOneEvenOne, rowOneEvenTwo, rowOneOddOne, rowOneOddTwo, rowOneOddThree;
    LinearLayout rowTwoEvenLayout, rowTwoOddLayout;
    ImageView rowTwoEvenOne, rowTwoEvenTwo, rowTwoOddOne, rowTwoOddTwo, rowTwoOddThree;
    Context context;
    public static int size = 2;
    boolean follow_clicked;
    String jsonData, index, jsonArray,profilePic="",userId="";
    List<ActiveParade> dataList;

    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_page_pof);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.imageRecycler);

        followTextView = (TextView) findViewById(R.id.followTextView);
        exitParadeTextView = (TextView) findViewById(R.id.exitparadeTextView);
        paradeProfileName = (TextView) findViewById(R.id.paradeprofileName);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        paradeProfileImage = (ImageView) findViewById(R.id.paradeprofileImage);
        backImageView = (ImageView) findViewById(R.id.backImageView);

        setDurationTextView=(TextView)findViewById(R.id.setDurationTextView_inboxpage);
        rowOneLayout = (RelativeLayout) findViewById(R.id.rowOneLayout);
        rowTwoLayout = (RelativeLayout) findViewById(R.id.rowTwoLayout);

        rowOneEvenLayout = (LinearLayout) findViewById(R.id.rowOneEvenLayout);
        rowOneOddLayout = (LinearLayout) findViewById(R.id.rowOneOddLayout);
        rowOneEvenOne = (ImageView) findViewById(R.id.rowOneEvenOne);
        rowOneEvenTwo = (ImageView) findViewById(R.id.rowOneEvenTwo);
        rowOneOddOne = (ImageView) findViewById(R.id.rowOneOddOne);
        rowOneOddTwo = (ImageView) findViewById(R.id.rowOneOddTwo);
        rowOneOddThree = (ImageView) findViewById(R.id.rowOneOddThree);


        rowTwoEvenLayout = (LinearLayout) findViewById(R.id.rowTwoEvenLayout);
        rowTwoOddLayout = (LinearLayout) findViewById(R.id.rowTwoOddLayout);
        rowTwoEvenOne = (ImageView) findViewById(R.id.rowTwoEvenOne);
        rowTwoEvenTwo = (ImageView) findViewById(R.id.rowTwoEvenTwo);
        rowTwoOddOne = (ImageView) findViewById(R.id.rowTwoOddOne);
        rowTwoOddTwo = (ImageView) findViewById(R.id.rowTwoOddTwo);
        rowTwoOddThree = (ImageView) findViewById(R.id.rowTwoOddThree);

        rowOneEvenOne.setOnClickListener(this);
        rowOneEvenTwo.setOnClickListener(this);
        rowOneOddOne.setOnClickListener(this);
        rowOneOddTwo.setOnClickListener(this);
        rowOneOddThree.setOnClickListener(this);

        rowTwoEvenOne.setOnClickListener(this);
        rowTwoEvenTwo.setOnClickListener(this);
        rowTwoOddOne.setOnClickListener(this);
        rowTwoOddTwo.setOnClickListener(this);
        rowTwoOddThree.setOnClickListener(this);


        int left = dpToPx(5);
        int right = dpToPx(5);
        int top = dpToPx(5);
        int bottom = dpToPx(5);
        //  int horizotalspacing = getResources().getDimension(R.dimen.gridspacing);
        //   int padding = dpToPx(4);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float width = ((displayMetrics.widthPixels / 3) - (left / 3) - (right / 3));
        Log.i("Width", width + "");
        int height = ((int) (width * 1.2));
        Log.i("Height", height + "");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) width, height);

        rowOneEvenOne.setLayoutParams(layoutParams);
        rowOneEvenOne.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rowOneEvenTwo.setLayoutParams(layoutParams);
        rowOneEvenTwo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        rowOneOddOne.setLayoutParams(layoutParams);
        rowOneOddOne.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rowOneOddTwo.setLayoutParams(layoutParams);
        rowOneOddTwo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rowOneOddThree.setLayoutParams(layoutParams);
        rowOneOddThree.setScaleType(ImageView.ScaleType.CENTER_CROP);


        rowTwoEvenOne.setLayoutParams(layoutParams);
        rowTwoEvenOne.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rowTwoEvenTwo.setLayoutParams(layoutParams);
        rowTwoEvenTwo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        rowTwoOddOne.setLayoutParams(layoutParams);
        rowTwoOddOne.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rowTwoOddTwo.setLayoutParams(layoutParams);
        rowTwoOddTwo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rowTwoOddThree.setLayoutParams(layoutParams);
        rowTwoOddThree.setScaleType(ImageView.ScaleType.CENTER_CROP);


        jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        jsonArray = getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>() {
        }.getType();
        dataList = gson.fromJson(jsonData, type);



        try {
            paradeProfileName.setText(dataList.get(Integer.parseInt(index)).userName);
            profilePic= dataList.get(Integer.parseInt(index)).profilePic;
            userId = dataList.get(Integer.parseInt(index)).userId;
            if (dataList.get(Integer.parseInt(index)).followingStatus.equals("1")) {
                followTextView.setText("Following");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
                } else {
                    followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
                }

                follow_clicked = true;
            } else if (dataList.get(Integer.parseInt(index)).followingStatus.equals("0")) {
                followTextView.setText("+Follow");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                } else {
                    followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                }

                follow_clicked = false;
            }

            JSONArray imageArray = new JSONArray(jsonArray);
            System.out.println("imageArray" + imageArray.length());
            productList = new ArrayList<>();
            JSONObject imageObject;
            String image;
            for (int i = 0; i < imageArray.length(); i++) {
                imageObject = (JSONObject) imageArray.get(i);

                image = imageObject.getString("fileName");
                System.out.println("image" + image);
                productList.add(image);
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        getVotingDurationTimer();


        if (profilePic != null && !profilePic.isEmpty())

        {

            Picasso.with(context)
                    .load(profilePic)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.actionbar_profileicon)
                    .into(paradeProfileImage);

        }
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        exitParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
                onBackPressed();
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flag.profilePage = true;
                Intent intent = new Intent(InboxPageProfileActivity.this, FashionHomeActivity.class);
                startActivity(intent);
                finish();


            }
        });
        followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FollowUnfollowParadeRequest();
            }
        });

        if (productList.size() == 2) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.GONE);

            rowOneEvenLayout.setVisibility(View.VISIBLE);
            rowOneOddLayout.setVisibility(View.GONE);
            // rowOneEvenOne.setImageResource(R.drawable.home2);
            // rowOneEvenTwo.setImageResource(R.drawable.home3);

            Picasso.with(context)
                    .load(productList.get(0))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneEvenOne);

            Picasso.with(context)
                    .load(productList.get(1))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneEvenTwo);


        } else if (productList.size() == 3) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.GONE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(productList.get(0))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddOne);

            Picasso.with(context)
                    .load(productList.get(1))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddTwo);
            Picasso.with(context)
                    .load(productList.get(2))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddThree);

        } else if (productList.size() == 4) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.VISIBLE);

            rowOneEvenLayout.setVisibility(View.VISIBLE);
            rowOneOddLayout.setVisibility(View.GONE);
            rowTwoEvenLayout.setVisibility(View.VISIBLE);
            rowTwoOddLayout.setVisibility(View.GONE);


            rowOneEvenOne.setImageResource(R.drawable.home2);
            rowOneEvenTwo.setImageResource(R.drawable.home3);

            rowTwoEvenOne.setImageResource(R.drawable.home4);
            rowTwoEvenTwo.setImageResource(R.drawable.home5);

            Picasso.with(context)
                    .load(productList.get(0))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneEvenOne);

            Picasso.with(context)
                    .load(productList.get(1))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneEvenTwo);
            Picasso.with(context)
                    .load(productList.get(2))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoEvenOne);
            Picasso.with(context)
                    .load(productList.get(3))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoEvenTwo);

        } else if (productList.size() == 5) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.VISIBLE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);
            rowTwoEvenLayout.setVisibility(View.VISIBLE);
            rowTwoOddLayout.setVisibility(View.GONE);

            Picasso.with(context)
                    .load(productList.get(0))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddOne);

            Picasso.with(context)
                    .load(productList.get(1))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddTwo);
            Picasso.with(context)
                    .load(productList.get(2))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddThree);
            Picasso.with(context)
                    .load(productList.get(3))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoEvenOne);
            Picasso.with(context)
                    .load(productList.get(4))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoEvenTwo);

        } else if (productList.size() == 6) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.VISIBLE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);
            rowTwoEvenLayout.setVisibility(View.GONE);
            rowTwoOddLayout.setVisibility(View.VISIBLE);

            rowOneOddOne.setImageResource(R.drawable.home2);
            rowOneOddTwo.setImageResource(R.drawable.home3);
            rowOneOddThree.setImageResource(R.drawable.home4);

            rowTwoOddOne.setImageResource(R.drawable.home5);
            rowTwoOddTwo.setImageResource(R.drawable.home6);
            rowTwoOddThree.setImageResource(R.drawable.home7);

            Picasso.with(context)
                    .load(productList.get(0))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddOne);

            Picasso.with(context)
                    .load(productList.get(1))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddTwo);
            Picasso.with(context)
                    .load(productList.get(2))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowOneOddThree);
            Picasso.with(context)
                    .load(productList.get(3))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoOddOne);
            Picasso.with(context)
                    .load(productList.get(4))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoOddTwo);
            Picasso.with(context)
                    .load(productList.get(5))
                    .placeholder(R.drawable.photolayer_f)
                    .into(rowTwoOddThree);
        }
        dialogshowParadeDetail();
    }

    public void dialogshowParadeDetail()

    {
        final Dialog dialogParadeDetail = new Dialog(this, R.style.custom_dialog_theme);
        dialogParadeDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogParadeDetail.setContentView(R.layout.layout_popup_inbox);

        TextView aboutParadeTextView = (TextView) dialogParadeDetail.findViewById(R.id.aboutParadeTextView);
        TextView okTextView = (TextView) dialogParadeDetail.findViewById(R.id.okTextView);
        TextView exitTextView = (TextView) dialogParadeDetail.findViewById(R.id.exitTextView);

        aboutParadeTextView.setText(dataList.get(Integer.parseInt(index)).aboutParade);

//        aboutParadeTextView.setText("\ud83d\ude00\ud83d\ude01");


        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogParadeDetail.dismiss();
            }
        });
        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                dialogParadeDetail.dismiss();
            }
        });

        dialogParadeDetail.show();
    }

    private void FollowUnfollowParadeRequest() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = "";
            if (!follow_clicked)
                url = ResourceManager.Followmtpuser();
            else
                url = ResourceManager.Unfollowmtpuser();

            url = url + "userId=" + user.id + "&followingId=" + userId;
            new followUnfollowParade().execute(url);
        }
    }

    private class followUnfollowParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
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
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {
                    Toast.makeText(InboxPageProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (!follow_clicked) {
                        followTextView.setText("Following");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
                        } else {
                            followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
                        }

                        follow_clicked = true;
                    } else {
                        followTextView.setText("+Follow");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                        } else {
                            followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                        }

                        follow_clicked = false;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(InboxPageProfileActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rowOneEvenOne:
                showimageDetail();
                break;
            case R.id.rowOneEvenTwo:
                showimageDetail();
                break;
            case R.id.rowOneOddOne:
                showimageDetail();
                break;
            case R.id.rowOneOddTwo:
                showimageDetail();
                break;
            case R.id.rowOneOddThree:
                showimageDetail();
                break;
            case R.id.rowTwoEvenOne:
                showimageDetail();
                break;
            case R.id.rowTwoEvenTwo:
                showimageDetail();
                break;
            case R.id.rowTwoOddOne:
                showimageDetail();
                break;
            case R.id.rowTwoOddTwo:
                showimageDetail();
                break;
            case R.id.rowTwoOddThree:
                showimageDetail();
                break;

        }
    }

    public void showimageDetail() {
//        Intent intent = new Intent(InboxPage_prof1.this,VotingActivity.class);
//        intent.putStringArrayListExtra("test", (ArrayList<String>) productList);
//        startActivity(intent);
    }


    public void getVotingDurationTimer()

    {

        System.out.println("Time in minutes: " + dataList.get(Integer.parseInt(index)).startTime + "---" + dataList.get(Integer.parseInt(index)).endTime);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        try {

            Date starttime = format.parse(dataList.get(Integer.parseInt(index)).startTime);
            Date endtime = format.parse(dataList.get(Integer.parseInt(index)).endTime);

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
                        String text = String.format("Time Remaining %02d:%02d:%02d", hours, minutes, seconds);
                        setDurationTextView.setText(text);
                    }

                    @Override
                    public void onFinish() {
                        setDurationTextView.setText("Voting Duration End");
                    }
                }.start();
            } else {
                setDurationTextView.setText("Voting Duration End");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}