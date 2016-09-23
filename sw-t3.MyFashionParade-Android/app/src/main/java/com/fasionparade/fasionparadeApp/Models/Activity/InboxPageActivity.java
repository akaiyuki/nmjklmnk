package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.VideoView;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.FileType;
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


public class InboxPageActivity extends ActionBarActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ArrayList<String> productList, productIdList, productTypeList;

    ArrayList<FileType> productListInbox;
    ImageView backImageView, profileImageView, paradeprofileImage;
    TextView paradeprofileName, followTextView, exitparadeTextView, setDurationTextView;

    RelativeLayout rowOneLayout, rowTwoLayout;
    LinearLayout rowOneEvenLayout, rowOneOddLayout;
    ImageView rowOneEvenOne, rowOneEvenTwo, rowOneOddOne, rowOneOddTwo, rowOneOddThree;
    VideoView mVideoView1, mVideoView2, mVideoView3, mVideoView4, mVideoView5, mVideoView21, mVideoView22, mVideoView23, mVideoView24, mVideoView25;

    LinearLayout rowTwoEvenLayout, rowTwoOddLayout;
    ImageView rowTwoEvenOne, rowTwoEvenTwo, rowTwoOddOne, rowTwoOddTwo, rowTwoOddThree;
    Context context;
    public static int size = 2;
    boolean follow_clicked;
    String jsonData, index, jsonArray;
    List<ActiveParade> dataList;

    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    String userId = "", userName = "", profilePic = "", followingStatus = "";
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_page);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.imageRecycler);

        followTextView = (TextView) findViewById(R.id.followTextView);
        exitparadeTextView = (TextView) findViewById(R.id.exitparadeTextView);
        paradeprofileName = (TextView) findViewById(R.id.paradeprofileName);
        setDurationTextView = (TextView) findViewById(R.id.setDurationTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        paradeprofileImage = (ImageView) findViewById(R.id.paradeprofileImage);
        backImageView = (ImageView) findViewById(R.id.backImageView);


        rowOneLayout = (RelativeLayout) findViewById(R.id.rowOneLayout);
        rowTwoLayout = (RelativeLayout) findViewById(R.id.rowTwoLayout);

        rowOneEvenLayout = (LinearLayout) findViewById(R.id.rowOneEvenLayout);
        rowOneOddLayout = (LinearLayout) findViewById(R.id.rowOneOddLayout);
        rowOneEvenOne = (ImageView) findViewById(R.id.rowOneEvenOne);
        rowOneEvenTwo = (ImageView) findViewById(R.id.rowOneEvenTwo);
        rowOneOddOne = (ImageView) findViewById(R.id.rowOneOddOne);
        rowOneOddTwo = (ImageView) findViewById(R.id.rowOneOddTwo);
        rowOneOddThree = (ImageView) findViewById(R.id.rowOneOddThree);

        mVideoView1 = (VideoView) findViewById(R.id.rowOneEvenOneplayer);
        mVideoView2 = (VideoView) findViewById(R.id.rowOneEvenTwoplayer);

        mVideoView3 = (VideoView) findViewById(R.id.rowOneOddOneplayer);
        mVideoView4 = (VideoView) findViewById(R.id.rowOneOddTwoplayer);
        mVideoView5 = (VideoView) findViewById(R.id.rowOneOddThreeplayer);

        mVideoView21 = (VideoView) findViewById(R.id.rowTwoEvenOneplayer);
        mVideoView22 = (VideoView) findViewById(R.id.rowTwoEvenTwoplayer);

        mVideoView23 = (VideoView) findViewById(R.id.rowTwoOddOneplayer);
        mVideoView24 = (VideoView) findViewById(R.id.rowTwoOddTwoplayer);
        mVideoView25 = (VideoView) findViewById(R.id.rowTwoOddThreeplayer);


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

        mVideoView1.setOnClickListener(this);
        mVideoView2.setOnClickListener(this);
        mVideoView3.setOnClickListener(this);
        mVideoView4.setOnClickListener(this);
        mVideoView5.setOnClickListener(this);
        mVideoView21.setOnClickListener(this);
        mVideoView22.setOnClickListener(this);
        mVideoView23.setOnClickListener(this);
        mVideoView24.setOnClickListener(this);
        mVideoView25.setOnClickListener(this);

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

        mVideoView1.setLayoutParams(layoutParams);
        mVideoView2.setLayoutParams(layoutParams);
        mVideoView3.setLayoutParams(layoutParams);
        mVideoView4.setLayoutParams(layoutParams);
        mVideoView5.setLayoutParams(layoutParams);
        mVideoView21.setLayoutParams(layoutParams);
        mVideoView22.setLayoutParams(layoutParams);
        mVideoView23.setLayoutParams(layoutParams);
        mVideoView24.setLayoutParams(layoutParams);
        mVideoView25.setLayoutParams(layoutParams);

        jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        jsonArray = getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>() {
        }.getType();
        dataList = gson.fromJson(jsonData, type);
        System.out.println("string--->" + jsonArray);
        try {

            JSONArray imageArray = new JSONArray(jsonArray);
            System.out.println("imageArray" + imageArray.length());
            productList = new ArrayList<>();
            productIdList = new ArrayList<>();
            productTypeList = new ArrayList<>();
            productListInbox = new ArrayList<>();
            JSONObject imageObject;
            String image, imageid;
            for (int i = 0; i < imageArray.length(); i++) {
                imageObject = (JSONObject) imageArray.get(i);

                image = imageObject.getString("fileName");
                imageid = imageObject.getString("imageId");
                productList.add(image);
                productIdList.add(imageid);
                productTypeList.add(imageObject.getString("fileType"));
                FileType fileType = new FileType();

                //  imageObject = (JSONObject) imageArray.get(i);
                fileType.fileName = imageObject.getString("fileName");
                fileType.type = imageObject.getString("fileType");
                fileType.imageId = imageObject.getString("imageId");
                productListInbox.add(fileType);
            }
            userId = dataList.get(Integer.parseInt(index)).userId;

            if (FashionHomeActivity.parade_clicked) {
                userName = getIntent().getStringExtra("userName");
                profilePic = getIntent().getStringExtra("profilePic");
                followingStatus = getIntent().getStringExtra("followingStatus");
            }
            else
            {
                userName = dataList.get(Integer.parseInt(index)).userName;
                profilePic = dataList.get(Integer.parseInt(index)).profilePic;
                followingStatus = dataList.get(Integer.parseInt(index)).followingStatus;

            }
            paradeprofileName.setText(userName);

            if (!profilePic.equals("") && profilePic != null) {
                Picasso.with(InboxPageActivity.this)
                        .load(dataList.get(Integer.parseInt(index)).profilePic)
                        .placeholder(R.drawable.actionbar_profileicon)
                        .into(paradeprofileImage);
            }

            if (followingStatus.equals("1")) {
                followTextView.setText("Following");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
                } else {
                    followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
                }

                follow_clicked = true;
            } else if (followingStatus.equals("0")) {
                followTextView.setText("+Follow");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                } else {
                    followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                }

                follow_clicked = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("err", e.toString());
            //System.out.println(e.getMessage());
        }
        getVotingDurationTimer();

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        exitparadeTextView.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(InboxPageActivity.this, FashionHomeActivity.class);
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

            if (productListInbox.get(0).type.equals("1")) {
                rowOneEvenOne.setVisibility(View.GONE);
                mVideoView1.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView1, productListInbox.get(0).fileName);
            } else {
                rowOneEvenOne.setVisibility(View.VISIBLE);
                mVideoView1.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenOne);
            }
            if (productListInbox.get(1).type.equals("1")) {
                rowOneEvenTwo.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView2, productListInbox.get(1).fileName);
            } else {
                rowOneEvenTwo.setVisibility(View.VISIBLE);
                mVideoView2.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenTwo);
            }


        } else if (productList.size() == 3) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.GONE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);

            if (productListInbox.get(0).type.equals("1")) {
                rowOneOddOne.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView3, productListInbox.get(0).fileName);
            } else {
                rowOneOddOne.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddOne);
            }
            if (productListInbox.get(1).type.equals("1")) {
                rowOneOddTwo.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView4, productListInbox.get(1).fileName);
            } else {
                rowOneOddTwo.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddTwo);
            }
            if (productListInbox.get(2).type.equals("1")) {
                rowOneOddThree.setVisibility(View.GONE);
                mVideoView5.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView5, productListInbox.get(2).fileName);
            } else {
                rowOneOddThree.setVisibility(View.VISIBLE);
                mVideoView5.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddThree);

            }


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


            if (productListInbox.get(0).type.equals("1")) {
                rowOneEvenOne.setVisibility(View.GONE);
                mVideoView1.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView1, productListInbox.get(0).fileName);
            } else {
                rowOneEvenOne.setVisibility(View.VISIBLE);
                mVideoView1.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenOne);
            }
            if (productListInbox.get(1).type.equals("1")) {
                rowOneEvenTwo.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView2, productListInbox.get(1).fileName);
            } else {
                rowOneEvenTwo.setVisibility(View.VISIBLE);
                mVideoView2.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenTwo);
            }
            if (productListInbox.get(2).type.equals("1")) {
                rowTwoEvenOne.setVisibility(View.GONE);
                mVideoView21.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView21, productListInbox.get(2).fileName);
            } else {
                rowTwoEvenOne.setVisibility(View.VISIBLE);
                mVideoView21.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoEvenOne);
            }
            if (productListInbox.get(3).type.equals("1")) {
                rowTwoEvenTwo.setVisibility(View.GONE);
                mVideoView22.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView22, productListInbox.get(3).fileName);
            } else {
                rowTwoEvenTwo.setVisibility(View.VISIBLE);
                mVideoView22.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(3).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoEvenTwo);
            }


        } else if (productList.size() == 5) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.VISIBLE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);
            rowTwoEvenLayout.setVisibility(View.VISIBLE);
            rowTwoOddLayout.setVisibility(View.GONE);

            if (productListInbox.get(0).type.equals("1")) {
                rowOneOddOne.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView3, productListInbox.get(0).fileName);
            } else {
                rowOneOddOne.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddOne);
            }
            if (productListInbox.get(1).type.equals("1")) {
                rowOneOddTwo.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView4, productListInbox.get(1).fileName);
            } else {
                rowOneOddTwo.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddTwo);
            }
            if (productListInbox.get(2).type.equals("1")) {
                rowOneOddThree.setVisibility(View.GONE);
                mVideoView5.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView5, productListInbox.get(2).fileName);
            } else {
                rowOneOddThree.setVisibility(View.VISIBLE);
                mVideoView5.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddThree);

            }
            if (productListInbox.get(3).type.equals("1")) {
                rowTwoEvenOne.setVisibility(View.GONE);
                mVideoView21.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView21, productListInbox.get(3).fileName);
            } else {
                rowTwoEvenOne.setVisibility(View.VISIBLE);
                mVideoView21.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(3).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoEvenOne);
            }
            if (productListInbox.get(4).type.equals("1")) {
                rowTwoEvenTwo.setVisibility(View.GONE);
                mVideoView22.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView22, productListInbox.get(4).fileName);
            } else {
                rowTwoEvenTwo.setVisibility(View.VISIBLE);
                mVideoView22.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(4).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoEvenTwo);
            }


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

            if (productListInbox.get(0).type.equals("1")) {
                rowOneOddOne.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView3, productListInbox.get(0).fileName);
            } else {
                rowOneOddOne.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddOne);
            }
            if (productListInbox.get(1).type.equals("1")) {
                rowOneOddTwo.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView4, productListInbox.get(1).fileName);
            } else {
                rowOneOddTwo.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddTwo);
            }
            if (productListInbox.get(2).type.equals("1")) {
                rowOneOddThree.setVisibility(View.GONE);
                mVideoView5.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView5, productListInbox.get(2).fileName);
            } else {
                rowOneOddThree.setVisibility(View.VISIBLE);
                mVideoView5.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddThree);

            }


            if (productListInbox.get(3).type.equals("1")) {
                rowTwoOddOne.setVisibility(View.GONE);
                mVideoView23.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView23, productListInbox.get(3).fileName);
            } else {
                rowTwoOddOne.setVisibility(View.VISIBLE);
                mVideoView23.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(3).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoOddOne);
            }
            if (productListInbox.get(4).type.equals("1")) {
                rowTwoOddTwo.setVisibility(View.GONE);
                mVideoView24.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView24, productListInbox.get(4).fileName);
            } else {
                rowTwoOddTwo.setVisibility(View.VISIBLE);
                mVideoView24.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(4).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoOddTwo);
            }
            if (productListInbox.get(5).type.equals("1")) {
                rowTwoOddThree.setVisibility(View.GONE);
                mVideoView25.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView25, productListInbox.get(5).fileName);
            } else {
                rowTwoOddThree.setVisibility(View.VISIBLE);
                mVideoView25.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productListInbox.get(5).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoOddThree);
            }


        }
        dialogShowParadeDetail();
    }

    public void dialogShowParadeDetail()

    {
        final Dialog dialogParadeDetail = new Dialog(this, R.style.custom_dialog_theme);
        dialogParadeDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogParadeDetail.setContentView(R.layout.layout_popup_inbox);

        TextView aboutParadeTextView = (TextView) dialogParadeDetail.findViewById(R.id.aboutParadeTextView);
        TextView okTextView = (TextView) dialogParadeDetail.findViewById(R.id.okTextView);
        TextView exitTextView = (TextView) dialogParadeDetail.findViewById(R.id.exitTextView);

        aboutParadeTextView.setText(dataList.get(Integer.parseInt(index)).aboutParade);

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
                position = 0;
                showImageDetail();
                break;
            case R.id.rowOneEvenOneplayer:
                position = 0;
                showImageDetail();
                break;
            case R.id.rowOneEvenTwo:
                position = 1;
                showImageDetail();
                break;
            case R.id.rowOneEvenTwoplayer:
                position = 1;
                showImageDetail();
                break;
            case R.id.rowOneOddOne:
                position = 0;
                showImageDetail();
                break;
            case R.id.rowOneOddOneplayer:
                position = 0;
                showImageDetail();
                break;
            case R.id.rowOneOddTwo:
                position = 1;
                showImageDetail();
                break;
            case R.id.rowOneOddTwoplayer:
                position = 1;
                showImageDetail();
                break;
            case R.id.rowOneOddThree:
                position = 2;
                showImageDetail();
                break;
            case R.id.rowOneOddThreeplayer:
                position = 2;
                showImageDetail();
                break;
            case R.id.rowTwoEvenOne:
                position = 3;
                showImageDetail();
                break;
            case R.id.rowTwoEvenOneplayer:
                position = 3;
                showImageDetail();
                break;
            case R.id.rowTwoEvenTwo:
                position = 4;
                showImageDetail();
                break;
            case R.id.rowTwoEvenTwoplayer:
                position = 4;
                showImageDetail();
                break;
            case R.id.rowTwoOddOne:
                position = 3;
                showImageDetail();
                break;
            case R.id.rowTwoOddOneplayer:
                position = 3;
                showImageDetail();
                break;
            case R.id.rowTwoOddTwo:
                position = 4;
                showImageDetail();
                break;
            case R.id.rowTwoOddTwoplayer:
                position = 4;
                showImageDetail();
                break;
            case R.id.rowTwoOddThree:
                position = 5;
                showImageDetail();
                break;
            case R.id.rowTwoOddThreeplayer:
                position = 5;
                showImageDetail();
                break;

        }
    }

    public void showImageDetail() {
        Intent intent = new Intent(InboxPageActivity.this, InboxParadeImageActivity.class);
        intent.putStringArrayListExtra("products", (ArrayList<String>) productList);
        intent.putStringArrayListExtra("productsId", (ArrayList<String>) productIdList);
        intent.putStringArrayListExtra("productsType", (ArrayList<String>) productTypeList);
        intent.putExtra("aboutParade", dataList.get(Integer.parseInt(index)).aboutParade);
        intent.putExtra("paradeId", dataList.get(Integer.parseInt(index)).paradeId);
        intent.putExtra("userId", dataList.get(Integer.parseInt(index)).userId);
        intent.putExtra("position", position);
        startActivity(intent);
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
                    Toast.makeText(InboxPageActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InboxPageActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    public void getVotingDurationTimer() {

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

    public void LoadVideoFile(VideoView video, String url) {

        Uri uri = Uri.parse(url);
        video.setVideoURI(uri);
        video.requestFocus();

        video.setZOrderOnTop(true);
        video.start();

    }
}