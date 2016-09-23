package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;

import java.io.File;
import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {


    private ImageView imageView;
    private TextView imageUser;
    private String Str_url = "";

    private ArrayList<Uri> pass;

    Uri uri;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        imageView = (ImageView) findViewById(R.id.imageView_view);

        imageUser = (TextView) findViewById(R.id.textView_use_image);

        activity = this;


        pass = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("Imagepass")) {
                String message = bundle.getString("Imagepass");
/*
                        Str_url ="/storage/sdcard0//MyCamera/"+message;
                        File file = new File("/storage/sdcard0//MyCamera/"+message); //your image file path*/
                Str_url = Environment.getExternalStorageDirectory() + "/MyCamera/" + message;
                File file = new File(Environment.getExternalStorageDirectory() + "/MyCamera/" + message); //your image file path*/


                imageView.setImageBitmap(decodeSampledBitmapFromFile(file.getAbsolutePath(), 400, 400));

                uri = Uri.parse(Str_url);
                pass.add(uri);

            }
        } else {
            imageView.setImageResource(R.drawable.photolayer_f);
        }


        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Imagepass", Str_url);
                setResult(1, i);
                finish();

            }
        });


    }


    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent i = new Intent(ViewImageActivity.this, NewParadeActivity.class);
        startActivity(i);
        finish();
    }


}
