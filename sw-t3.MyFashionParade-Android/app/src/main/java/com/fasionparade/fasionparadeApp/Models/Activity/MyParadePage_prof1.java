package com.fasionparade.fasionparadeApp.Models.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.Models.Adapters.MyParadeAdapter;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.R;
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


public class MyParadePage_prof1 extends ActionBarActivity {
    RecyclerView recyclerView;
    MyParadeAdapter newParadeAdapter;
    ArrayList<String> productList;
    EditText tapHereTextView,tapHereVoterInfoTextView,tapHereHashTagTextView;
    ImageView backImageView,profileImageView;

    RelativeLayout rowOneLayout, rowTwoLayout;
    LinearLayout rowOneEvenLayout, rowOneOddLayout;
    ImageView rowOneEvenOne, rowOneEvenTwo, rowOneOddOne, rowOneOddTwo, rowOneOddThree;
    LinearLayout rowTwoEvenLayout, rowTwoOddLayout;
    ImageView rowTwoEvenOne, rowTwoEvenTwo, rowTwoOddOne, rowTwoOddTwo, rowTwoOddThree;

     TextView setDurationTextView;


   Context context;
    public static int size = 2;

    String index="";
    List<ActiveParade> dataList;
    public ArrayList<Integer> drawableArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parade_page_prof);
        context=this;
        recyclerView = (RecyclerView) findViewById(R.id.imageRecycler);

        tapHereTextView = (EditText) findViewById(R.id.tapHereTextView);
        tapHereVoterInfoTextView=(EditText) findViewById(R.id.tapHereVoterInfoTextView);
        tapHereHashTagTextView=(EditText) findViewById(R.id.tapHereHashTagTextView);
        profileImageView=(ImageView)findViewById(R.id.profileImageView);
        backImageView=(ImageView)findViewById(R.id.backImageView);
        setDurationTextView=(TextView)findViewById(R.id.setDurationTextView);
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








        String jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        String jsonArray=getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>(){}.getType();


         dataList = gson.fromJson(jsonData, type);
            try {
                tapHereTextView.setText(dataList.get(Integer.parseInt(index)).paradeName);
                tapHereVoterInfoTextView.setText(dataList.get(Integer.parseInt(index)).aboutParade);
                tapHereHashTagTextView.setText(dataList.get(Integer.parseInt(index)).tag);

                JSONArray imageArray=new JSONArray(jsonArray);

                productList= new ArrayList<>();
                JSONObject imageObject;
                String image;
                for (int i = 0; i < imageArray.length(); i++)
                {
                    imageObject = (JSONObject) imageArray.get(i);

                    image=imageObject.getString("fileName");

                    productList.add(image);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


        getVotingDurationTimer();

        backImageView.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(MyParadePage_prof1.this, FashionHomeActivity.class);
                startActivity(intent);
                finish();


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


        }
        else if (productList.size()  == 3) {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.GONE);

            rowOneEvenLayout.setVisibility(View.GONE);
            rowOneOddLayout.setVisibility(View.VISIBLE);

//            rowOneOddOne.setImageResource(R.drawable.home2);
//            rowOneOddTwo.setImageResource(R.drawable.home3);
//            rowOneOddThree.setImageResource(R.drawable.home4);

            Picasso.with(context).load(productList.get(0)).placeholder(R.drawable.photolayer_f).into(rowOneOddOne);
            Picasso.with(context).load(productList.get(1)).placeholder(R.drawable.photolayer_f).into(rowOneOddTwo);
            Picasso.with(context).load(productList.get(2)).placeholder(R.drawable.photolayer_f).into(rowOneOddThree);







        } else if (productList.size() == 4)
        {

            rowOneLayout.setVisibility(View.VISIBLE);
            rowTwoLayout.setVisibility(View.VISIBLE);

            rowOneEvenLayout.setVisibility(View.VISIBLE);
            rowOneOddLayout.setVisibility(View.GONE);
            rowTwoEvenLayout.setVisibility(View.VISIBLE);
            rowTwoOddLayout.setVisibility(View.GONE);


//            rowOneEvenOne.setImageResource(R.drawable.home2);
//            rowOneEvenTwo.setImageResource(R.drawable.home3);
//
//            rowTwoEvenOne.setImageResource(R.drawable.home4);
//            rowTwoEvenTwo.setImageResource(R.drawable.home5);

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

//            rowOneOddOne.setImageResource(R.drawable.home2);
//            rowOneOddTwo.setImageResource(R.drawable.home3);
//            rowOneOddThree.setImageResource(R.drawable.home4);
//
//            rowTwoEvenOne.setImageResource(R.drawable.home5);
//            rowTwoEvenTwo.setImageResource(R.drawable.home6);
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
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
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
                    public void onFinish()
                    {
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
