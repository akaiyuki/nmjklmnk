package com.fasionparade.fasionparadeApp.Models.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fasionparade.fasionparadeApp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ViewVideoActivity extends AppCompatActivity {


    private VideoView  mVideoView;
    Bitmap bmThumbnail;
    String FileUrl="",VideoUrl="";
    MediaController mediaController;
    TextView user_this_video,textView_cancel;
    final Context context = this;
    MediaMetadataRetriever mediaMetadataRetriever;
    Bitmap bmFrame;
    byte[] byteArray;

    private ArrayList<Uri> pass ;
    Uri uri;
    private File imageFile;
    private int rotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        mVideoView =(VideoView)findViewById(R.id.imageView_video_thumbail);
        user_this_video =(TextView)findViewById(R.id.textView_use_video);
        textView_cancel= (TextView)findViewById(R.id.textView_cancel);

        pass = new ArrayList<>();


        mediaController= new MediaController(this);
        mediaController.setAnchorView(mVideoView);


        Bundle bundle = getIntent().getExtras();

        String message = bundle.getString("Videopass");
        //Fileurl ="/storage/sdcard0/MyCamera/"+message;

        FileUrl = Environment.getExternalStorageDirectory()+"/MyCamera/"+message;


        VideoUrl = FileUrl;

        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(FileUrl);

        Uri video = Uri.parse(FileUrl);




        mVideoView.setMediaController(mediaController);
        mVideoView.setOnCompletionListener(myVideoViewCompletionListener);
        mVideoView.setOnPreparedListener(MyVideoViewPreparedListener);
        mVideoView.setOnErrorListener(myVideoViewErrorListener);
        mVideoView.setVideoURI(video);
        mVideoView.requestFocus();
        mVideoView.start();


        textView_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        user_this_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int currentPosition = mVideoView.getCurrentPosition(); //in millisecond
                bmFrame = mediaMetadataRetriever.getFrameAtTime(currentPosition * 1000); //unit in microsecond

                if(bmFrame == null)
                {
                    Toast.makeText(ViewVideoActivity.this, "bmFrame == null!", Toast.LENGTH_LONG).show();
                }else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmFrame.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    try
                    {
                    bmFrame = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(rotation);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmFrame, 0, 0, bmFrame.getWidth(), bmFrame.getHeight(), rotateMatrix, false);

                    String state = Environment.getExternalStorageState();
                    File folder = null;
                    if (state.contains(Environment.MEDIA_MOUNTED)) {
                        folder = new File(Environment.getExternalStorageDirectory() + "/MyCamera/Thumbnail");
                    } else {
                        folder = new File(Environment.getExternalStorageDirectory() + "/MyCamera/Thumbnail");
                    }

                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }
                    if (success) {
                        java.util.Date date = new java.util.Date();
                        imageFile = new File(folder.getAbsolutePath() + File.separator + new Timestamp(date.getTime()).toString() + ".jpg");
                        imageFile.createNewFile();


                    } else {
                        Toast.makeText(getBaseContext(), "Image Not saved", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    // save image into gallery
                    bmFrame.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    FileOutputStream fout = new FileOutputStream(imageFile);
                    fout.write(ostream.toByteArray());
                    fout.close();
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());
                    ViewVideoActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


                        String Thumbnail = imageFile.getPath();
                        Log.i("thumbnail",imageFile.getPath());
//                        uri = Uri.parse(Thumbnail);
//                        pass.add(uri);
                        Intent intent = new Intent();
                        intent.putExtra("thumbnailimage",Thumbnail);
                        intent.putExtra("Videourl",VideoUrl);
                        setResult(2, intent);
                        finish();

                    }

                catch (Exception e)
                {


                }





                }
            }
        });








    }


    MediaPlayer.OnCompletionListener myVideoViewCompletionListener =
            new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
//                    Toast.makeText(View_video.this, "End of Video", Toast.LENGTH_LONG).show();
                }
            };

    MediaPlayer.OnPreparedListener MyVideoViewPreparedListener =
            new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    long duration = mVideoView.getDuration(); //in millisecond
//                    Toast.makeText(View_video.this, "Duration: " + duration + " (ms)", Toast.LENGTH_LONG).show();

                }
            };

    MediaPlayer.OnErrorListener myVideoViewErrorListener =
            new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    Toast.makeText(ViewVideoActivity.this, "Error!!!", Toast.LENGTH_LONG).show();
                    return true;
                }
            };


    @Override
    public void onBackPressed()
    {
        // do something on back.

        Intent i = new Intent(ViewVideoActivity.this, NewParadeActivity.class);
        startActivity(i);
        finish();
    }


}
