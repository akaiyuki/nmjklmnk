//package com.fasionparade.fasionparadeApp.Backup;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.ColorDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
//import com.fasionparade.fasionparadeApp.R;
//
//
///**
// * Created by smartwavedev on 4/29/16.
// */
//public class PhotoFromCameraActivity extends BaseActivity implements View.OnClickListener {
//
//    private Bitmap bitmap;
//    private ImageView imageView;
//
//    private Button buttonChoose;
//    private Button buttonUpload;
//
//    public static PhotoFromCameraActivity INSTANCE = null;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_layout);
//
//        INSTANCE = this;
//
//        imageView  = (ImageView) findViewById(R.id.imageView1);
//
//        buttonChoose = (Button) findViewById(R.id.btnNewImage);
//        buttonUpload = (Button) findViewById(R.id.btnUpload);
//
//
////        bitmap = Singleton.getImageBitmap();
//
////        imageView.setImageBitmap(bitmap);
//
//        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        if(v == buttonChoose){
//            startActivity(new Intent(PhotoFromCameraActivity.this, PictureActivity.class));
//        }
//
//
//    }
//
//}
