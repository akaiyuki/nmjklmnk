package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Views.VideoViews.VideoHelper;

public class VideoCaptureActivity extends Activity implements Animation.AnimationListener {
    private static final String TAG = "VideoCaptureActivity";

    Camera camera;
    ImageButton recordButton;
    FrameLayout cameraPreviewFrame;
    VideoHelper videoHelper;
    MediaRecorder mediaRecorder;
    private int rotation;
    private int cameraId;
    TextView timer;
    // Animation
    Animation animBlink;
    View view1;
    File file, PassValue;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        super.setContentView(R.layout.video);


        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        // initialize the camera in background, as this may take a while
        new AsyncTask<Void, Void, Camera>() {
            @Override
            protected Camera doInBackground(Void... params) {
                try {
                    Camera camera = Camera.open();
                    return camera == null ? Camera.open(0) : camera;
                } catch (RuntimeException e) {
                    Log.wtf(TAG, "Failed to get camera", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Camera camera) {
                if (camera == null) {
                    Toast.makeText(VideoCaptureActivity.this, R.string.cannot_record, Toast.LENGTH_SHORT);
                } else {
                    VideoCaptureActivity.this.initCamera(camera);

                }
            }
        }.execute();


        this.cameraPreviewFrame = (FrameLayout) super.findViewById(R.id.camera_preview);
        this.recordButton = (ImageButton) super.findViewById(R.id.recordButton);
        this.timer = (TextView) super.findViewById(R.id.textView_timer);


        // load the animation
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        // set animation listener
        animBlink.setAnimationListener(this);

        timer.setVisibility(View.GONE);


        CountDownTimer cT = new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                String v = String.format("%2d", millisUntilFinished / 1000);
                int va = (int) ((millisUntilFinished % 1000) / 1000);
            }

            public void onFinish() {
                timer.setVisibility(View.VISIBLE);
                startRecording(view1);

            }
        };
        cT.start();

    }


    void initCamera(Camera camera) {
        // we now have the camera
        this.camera = camera;
        // create a preview for our camera
        // position on camera
        this.setUpCamera(camera);
        this.videoHelper = new VideoHelper(VideoCaptureActivity.this, this.camera);
        // add the preview to our preview frame
        this.cameraPreviewFrame.addView(VideoCaptureActivity.this.videoHelper, 0);


    }

    void releaseCamera() {
        if (this.camera != null) {
            this.camera.lock(); // unnecessary in API >= 14
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
            this.cameraPreviewFrame.removeView(this.videoHelper);
        }
    }


    void releaseCameraNew() {
        if (this.camera != null) {
            this.camera.lock(); // unnecessary in API >= 14
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
            this.cameraPreviewFrame.removeView(this.videoHelper);


        }


    }

    void releaseMediaRecorder() {
        if (this.mediaRecorder != null) {
            this.mediaRecorder.reset(); // clear configuration (optional here)
            this.mediaRecorder.release();
            this.mediaRecorder = null;
        }
    }

    void releaseResources() {
        this.releaseMediaRecorder();
        this.releaseCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.releaseResources();
    }

    // gets called by the button press
    public void startRecording(View view) {

        Log.d(TAG, "startRecording()");
        // we need to unlock the camera so that mediaRecorder can use it
        this.camera.unlock(); // unnecessary in API >= 14
        // now we can initialize the media recorder and set it up with our
        // camera
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setCamera(this.camera);
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        this.mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        this.mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
        this.mediaRecorder.setOrientationHint(90);
        this.mediaRecorder.setOutputFile(this.initFile().getAbsolutePath());
        this.mediaRecorder.setPreviewDisplay(this.videoHelper.getHolder().getSurface());
        try {
            this.mediaRecorder.prepare();
            // start the actual recording
            // throws IllegalStateException if not prepared
            this.mediaRecorder.start();
            Toast.makeText(this, R.string.recording, Toast.LENGTH_SHORT).show();

            CountDownTimer cT = new CountDownTimer(6000, 1000) {

                public void onTick(long millisUntilFinished) {
                    String v = String.format("%2d", millisUntilFinished / 6000);
                    int va = (int) ((millisUntilFinished % 6000) / 1000);

                    // start the animation
                    timer.startAnimation(animBlink);
                    timer.setText("" + String.format("%2d", va));
                }

                public void onFinish() {

                    timer.setText("0");
                    timer.startAnimation(animBlink);
                    stopRecording(view1);

                }
            };
            cT.start();
            // enable the stop button by indicating that we are recording
        } catch (Exception e) {
            Log.wtf(TAG, "Failed to prepare MediaRecorder", e);
            Toast.makeText(this, R.string.cannot_record, Toast.LENGTH_SHORT).show();
            this.releaseMediaRecorder();
        }
    }

    // gets called by the button press
    public void stopRecording(View v) {
        Log.d(TAG, "stopRecording()");
        assert this.mediaRecorder != null;
        try {
            this.mediaRecorder.stop();
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            // we are no longer recording
        } catch (RuntimeException e) {
            // the recording did not succeed
            Log.w(TAG, "Failed to record", e);
            if (this.file != null && this.file.exists() && this.file.delete()) {
                Log.d(TAG, "Deleted " + this.file.getAbsolutePath());
            }
            return;
        } finally {
            this.releaseMediaRecorder();
        }
        if (this.file == null || !this.file.exists()) {
            Log.w(TAG, "File does not exist after stop: " + this.file.getAbsolutePath());

        } else {
            Log.d(TAG, "Going to display the video: " + this.file.getAbsolutePath());

            String filename = this.file.getName();
            Log.d(TAG, "The video name : " + filename);
            finish();
            Intent i = new Intent(VideoCaptureActivity.this, ViewVideoActivity.class);
            i.putExtra("Videopass", filename);
            startActivityForResult(i, NewParadeActivity.INTENT_REQUEST_GET_VIDOE);
            releaseCameraNew();


        }
    }

    private File initFile() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/MyCamera");
        if (!dir.exists() && !dir.mkdirs()) {
            Log.wtf(TAG, "Failed to create storage directory: " + dir.getAbsolutePath());
            Toast.makeText(VideoCaptureActivity.this, R.string.cannot_record, Toast.LENGTH_SHORT);
            this.file = null;
        } else {
            this.file = new File(dir.getAbsolutePath(), new SimpleDateFormat("'Video_'HHmmss1'.mp4'").format(new Date()));
        }
        return this.file;
    }


    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();


        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for blink animation
        if (animation == animBlink) {
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        Log.i("VReques code", "" + requestCode);

        Log.i("Vresule code", "" + resuleCode);
        if (requestCode == NewParadeActivity.INTENT_REQUEST_GET_VIDOE && resuleCode == 2) //
        {
            String image_uri = intent.getStringExtra("thumbnailimage");
            String video_uri = intent.getStringExtra("Videourl");
            Intent i = new Intent();
            i.putExtra("thumbnailimage", image_uri);
            i.putExtra("Videourl", video_uri);

            setResult(2, i);
            finish();
            //do something
        }


    }


}