package com.fasionparade.fasionparadeApp.Models.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;


public class MyFaveSingleViewActivity extends AppCompatActivity {

   private ImageView imageViewSingleView;

   private TextView textViewBack;


    Context context;
    String imageurl="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfav_singleview);



        imageViewSingleView =(ImageView)findViewById(R.id.imageView_single_fav);

        textViewBack =(TextView)findViewById(R.id.exitParade_single_fav);

        imageurl = getIntent().getStringExtra("jsonArray");

        Log.i("Image Url",imageurl);
        Picasso.with(context).load(imageurl).placeholder(R.drawable.no_image).error(R.drawable.photolayer_f).into(imageViewSingleView);


        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });




    }



}
