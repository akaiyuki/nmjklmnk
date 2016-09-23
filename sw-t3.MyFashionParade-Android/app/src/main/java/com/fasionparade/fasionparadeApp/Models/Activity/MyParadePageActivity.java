package com.fasionparade.fasionparadeApp.Models.Activity;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fasionparade.fasionparadeApp.Models.Adapters.MyParadeAdapter;
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
import com.vdurmont.emoji.EmojiParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyParadePageActivity extends ActionBarActivity {
    //RecyclerView recyclerView;
    MyParadeAdapter newParadeAdapter;
    ArrayList<FileType> productList;

    EditText tapHereTextView, tapHereVoterInfoTextView, tapHereHashTagTextView;
    ImageView backImageView, profileImageView;
    VideoView mVideoView1, mVideoView2, mVideoView3, mVideoView4, mVideoView5, mVideoView21, mVideoView22, mVideoView23, mVideoView24, mVideoView25;
    RelativeLayout rowOneLayout, rowTwoLayout;
    LinearLayout rowOneEvenLayout, rowOneOddLayout;
    ImageView rowOneEvenOne, rowOneEvenTwo, rowOneOddOne, rowOneOddTwo, rowOneOddThree;
    LinearLayout rowTwoEvenLayout, rowTwoOddLayout;
    ImageView rowTwoEvenOne, rowTwoEvenTwo, rowTwoOddOne, rowTwoOddTwo, rowTwoOddThree;
    List<ActiveParade> dataList;
    public static int size = 2;
    String index, aboutParade="", tag="";
    public ArrayList<Integer> drawableArrayList;
    TextView setDurationTextView, cancelTextView, stopParadeTextView;
    LinearLayout publicLayout, groupLayout, contactLayout;
    String[] values = {"ud", "u2", "uf"};
    String[] characters = {"\\ud", "\\u2", "\\uf"};

    Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parade_page);
        context = this;
       // characters = getResources().getStringArray(R.array.replace);
        //   recyclerView = (RecyclerView) findViewById(R.id.imageRecycler);

        tapHereTextView = (EditText) findViewById(R.id.tapHereTextView);
        tapHereVoterInfoTextView = (EditText) findViewById(R.id.tapHereVoterInfoTextView);
        tapHereHashTagTextView = (EditText) findViewById(R.id.tapHereHashTagTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        setDurationTextView = (TextView) findViewById(R.id.setDurationTextView);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        stopParadeTextView = (TextView) findViewById(R.id.stopParadeTextView);

        publicLayout = (LinearLayout) findViewById(R.id.publicLayout);
        groupLayout = (LinearLayout) findViewById(R.id.groupLayout);
        contactLayout = (LinearLayout) findViewById(R.id.contactLayout);

        rowOneLayout = (RelativeLayout) findViewById(R.id.rowOneLayout);
        rowTwoLayout = (RelativeLayout) findViewById(R.id.rowTwoLayout);

        rowOneEvenLayout = (LinearLayout) findViewById(R.id.rowOneEvenLayout);
        rowOneOddLayout = (LinearLayout) findViewById(R.id.rowOneOddLayout);
        rowOneEvenOne = (ImageView) findViewById(R.id.rowOneEvenOne);
        rowOneEvenTwo = (ImageView) findViewById(R.id.rowOneEvenTwo);

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

        String jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        String jsonArray = getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>() {
        }.getType();
        dataList = gson.fromJson(jsonData, type);
        try {
            tapHereTextView.setText(dataList.get(Integer.parseInt(index)).formatstartTime);
            aboutParade = dataList.get(Integer.parseInt(index)).aboutParade;
            tag = dataList.get(Integer.parseInt(index)).tag;
            /*for (int i = 0; i < values.length; i++) {
                if (dataList.get(Integer.parseInt(index)).aboutParade.contains(values[i])) {
                    aboutparade = aboutparade.replaceAll(values[i], characters[i]);
                    System.out.println("characters[i]:"+ characters[i]);
                    System.out.println("aboutparade:"+ aboutparade);
                }
                if(dataList.get(Integer.parseInt(index)).tag.contains(values[i]))
                {
                    tag = tag.replaceAll(values[i], characters[i]);
                }
            }*/

            //System.out.println("aboutparade:"+aboutparade+"\n"+"tag:"+tag);
            tapHereVoterInfoTextView.setText(EmojiParser.parseToUnicode(aboutParade.toString()));
            tapHereHashTagTextView.setText(EmojiParser.parseToUnicode(tag.toString()));
            if (dataList.get(Integer.parseInt(index)).sharedWith.equals("2"))
                setGroup();
            else if (dataList.get(Integer.parseInt(index)).sharedWith.equals("3"))
                setContact();
            JSONArray imageArray = new JSONArray(jsonArray);

            productList = new ArrayList<FileType>();

            JSONObject imageObject;
            String image;
            for (int i = 0; i < imageArray.length(); i++) {

                FileType fileType = new FileType();

                imageObject = (JSONObject) imageArray.get(i);
                fileType.fileName = imageObject.getString("fileName");
                fileType.type = imageObject.getString("fileType");

                productList.add(fileType);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gridSpacing);
//        GridSpacingItemDecoration gridSpacingItemDecoration=new GridSpacingItemDecoration(4,spacingInPixels,true);
//
//        recyclerView.addItemDecoration(gridSpacingItemDecoration);
//        newParadeAdapter = new MyParadeAdapter(MyParadePage.this, productList);
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyParadePage.this, 4);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setAdapter(newParadeAdapter);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
                onBackPressed();
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
                onBackPressed();
            }
        });
        stopParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStopParadeRequest();
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flag.profilePage = true;
                Intent intent = new Intent(MyParadePageActivity.this, FashionHomeActivity.class);
                startActivity(intent);
                finish();


            }
        });

        if (productList.size() == 2) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.GONE);

            rowOneEvenLayout.setVisibility(View.VISIBLE);
            rowOneOddLayout.setVisibility(View.GONE);

            if (productList.get(0).type.equals("1")) {
                rowOneEvenOne.setVisibility(View.GONE);
                mVideoView1.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView1, productList.get(0).fileName);
            } else {
                rowOneEvenOne.setVisibility(View.VISIBLE);
                mVideoView1.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenOne);
            }
            if (productList.get(1).type.equals("1")) {
                rowOneEvenTwo.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView2, productList.get(1).fileName);
            } else {
                rowOneEvenTwo.setVisibility(View.VISIBLE);
                mVideoView2.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenTwo);
            }


        } else if (productList.size() == 3) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.GONE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);

            if (productList.get(0).type.equals("1")) {
                rowOneOddOne.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView3, productList.get(0).fileName);
            } else {
                rowOneOddOne.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddOne);
            }
            if (productList.get(1).type.equals("1")) {
                rowOneOddTwo.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView4, productList.get(1).fileName);
            } else {
                rowOneOddTwo.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddTwo);
            }
            if (productList.get(2).type.equals("1")) {
                rowOneOddThree.setVisibility(View.GONE);
                mVideoView5.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView5, productList.get(2).fileName);
            } else {
                rowOneOddThree.setVisibility(View.VISIBLE);
                mVideoView5.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(2).fileName)
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


            if (productList.get(0).type.equals("1")) {
                rowOneEvenOne.setVisibility(View.GONE);
                mVideoView1.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView1, productList.get(0).fileName);
            } else {
                rowOneEvenOne.setVisibility(View.VISIBLE);
                mVideoView1.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenOne);
            }
            if (productList.get(1).type.equals("1")) {
                rowOneEvenTwo.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView2, productList.get(1).fileName);
            } else {
                rowOneEvenTwo.setVisibility(View.VISIBLE);
                mVideoView2.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneEvenTwo);
            }
            if (productList.get(2).type.equals("1")) {
                rowTwoEvenOne.setVisibility(View.GONE);
                mVideoView21.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView21, productList.get(2).fileName);
            } else {
                rowTwoEvenOne.setVisibility(View.VISIBLE);
                mVideoView21.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoEvenOne);
            }
            if (productList.get(3).type.equals("1")) {
                rowTwoEvenTwo.setVisibility(View.GONE);
                mVideoView22.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView22, productList.get(3).fileName);
            } else {
                rowTwoEvenTwo.setVisibility(View.VISIBLE);
                mVideoView22.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(3).fileName)
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

            if (productList.get(0).type.equals("1")) {
                rowOneOddOne.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView3, productList.get(0).fileName);
            } else {
                rowOneOddOne.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddOne);
            }
            if (productList.get(1).type.equals("1")) {
                rowOneOddTwo.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView4, productList.get(1).fileName);
            } else {
                rowOneOddTwo.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddTwo);
            }
            if (productList.get(2).type.equals("1")) {
                rowOneOddThree.setVisibility(View.GONE);
                mVideoView5.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView5, productList.get(2).fileName);
            } else {
                rowOneOddThree.setVisibility(View.VISIBLE);
                mVideoView5.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddThree);

            }
            if (productList.get(3).type.equals("1")) {
                rowTwoEvenOne.setVisibility(View.GONE);
                mVideoView21.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView21, productList.get(3).fileName);
            } else {
                rowTwoEvenOne.setVisibility(View.VISIBLE);
                mVideoView21.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(3).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoEvenOne);
            }
            if (productList.get(4).type.equals("1")) {
                rowTwoEvenTwo.setVisibility(View.GONE);
                mVideoView22.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView22, productList.get(4).fileName);
            } else {
                rowTwoEvenTwo.setVisibility(View.VISIBLE);
                mVideoView22.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(4).fileName)
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

            if (productList.get(0).type.equals("1")) {
                rowOneOddOne.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView3, productList.get(0).fileName);
            } else {
                rowOneOddOne.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(0).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddOne);
            }
            if (productList.get(1).type.equals("1")) {
                rowOneOddTwo.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView4, productList.get(1).fileName);
            } else {
                rowOneOddTwo.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(1).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddTwo);
            }
            if (productList.get(2).type.equals("1")) {
                rowOneOddThree.setVisibility(View.GONE);
                mVideoView5.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView5, productList.get(2).fileName);
            } else {
                rowOneOddThree.setVisibility(View.VISIBLE);
                mVideoView5.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(2).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowOneOddThree);

            }


            if (productList.get(3).type.equals("1")) {
                rowTwoOddOne.setVisibility(View.GONE);
                mVideoView23.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView23, productList.get(3).fileName);
            } else {
                rowTwoOddOne.setVisibility(View.VISIBLE);
                mVideoView23.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(3).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoOddOne);
            }
            if (productList.get(4).type.equals("1")) {
                rowTwoOddTwo.setVisibility(View.GONE);
                mVideoView24.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView24, productList.get(4).fileName);
            } else {
                rowTwoOddTwo.setVisibility(View.VISIBLE);
                mVideoView24.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(4).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoOddTwo);
            }
            if (productList.get(5).type.equals("1")) {
                rowTwoOddThree.setVisibility(View.GONE);
                mVideoView25.setVisibility(View.VISIBLE);
                LoadVideoFile(mVideoView25, productList.get(5).fileName);
            } else {
                rowTwoOddThree.setVisibility(View.VISIBLE);
                mVideoView25.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(productList.get(5).fileName)
                        .placeholder(R.drawable.no_image)
                        .into(rowTwoOddThree);
            }


        }
        getVotingDurationTimer();

    }


    private void makeStopParadeRequest() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.stopParade();
            url = url + "userId=" + user.id + "&paradeId=" + dataList.get(Integer.parseInt(index)).paradeId;
            new stopParade().execute(url);
        }
    }

    private class stopParade extends AsyncTask<String, Void, String> {
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
                String message;
                message = jsonObject.getString("message");
                Toast.makeText(MyParadePageActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MyParadePageActivity.this,
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

            Date startTimeDate = format.parse(dataList.get(Integer.parseInt(index)).startTime);
            Date endTimeDate = format.parse(dataList.get(Integer.parseInt(index)).endTime);

            Calendar startTime = Calendar.getInstance();
            startTime.setTime(startTimeDate);

            Calendar endTime = Calendar.getInstance();
            endTime.setTime(endTimeDate);

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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void LoadVideoFile(VideoView video, String url) {

        Uri uri = Uri.parse(url);
        video.setVideoURI(uri);
        video.requestFocus();

        video.setZOrderOnTop(true);
        video.start();

    }

    public void setGroup() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            publicLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
            groupLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
            contactLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
        } else {
            publicLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
            groupLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
            contactLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
        }

    }

    public void setContact() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            publicLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
            groupLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
            contactLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
        } else {
            publicLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
            groupLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
            contactLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
        }

    }

}
