package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;

public class CameraCaptureActivity extends Activity implements SurfaceHolder.Callback {
    TextView testView;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    private int rotation;
    private int cameraId;
    PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    PictureCallback jpegCallback;

    private File imageFile;
    TextView Clic_camera,  Change_position;

    byte[] byteArray_image;

    TextView message_text,button_txt_dialog;

    int angleToRotate;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Boolean enable_cfac = true;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camerademo_activity);



        // camera surface view created
        cameraId = CameraInfo.CAMERA_FACING_BACK;

        //position change boolean
        enable_cfac = false;

        pref = getApplicationContext().getSharedPreferences(Flag.CAMERA_DETAILS,
                Context.MODE_PRIVATE);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        Clic_camera =(TextView)findViewById(R.id.capture_button);
        Change_position=(TextView)findViewById(R.id.imageView_front);
        button_txt_dialog=(TextView)findViewById(R.id.textView_buttom_dialog);
        message_text=(TextView)findViewById(R.id.textView_message_dialog);
        Clic_camera.setEnabled(false);

        if(Utils.isCameraIn(this)){
            message_text.setVisibility(View.GONE);
            button_txt_dialog.setVisibility(View.GONE);
            Clic_camera.setEnabled(true);
        }



        button_txt_dialog.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editor = pref.edit();
                editor.putBoolean(Flag.IS_CAMERA, true);
                editor.commit();

                message_text.setVisibility(View.GONE);
                button_txt_dialog.setVisibility(View.GONE);
                Clic_camera.setEnabled(true);

            }
        });


        ImageView mButtonVideo = (ImageView) findViewById(R.id.videoicon);
        mButtonVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CameraCaptureActivity.this, VideoCaptureActivity.class);
                camera.release();
                camera = null;
                startActivityForResult(i, NewParadeActivity.INTENT_REQUEST_GET_VIDOE);

            }
        });



        Change_position.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                frontfacing();

            }
        });



        Clic_camera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(FashionHomeActivity.userType.equals("1")) {
                    try {
                        Intent i = new Intent(CameraCaptureActivity.this, VideoCaptureActivity.class);
                        camera.release();
                        camera = null;
                        startActivityForResult(i, NewParadeActivity.INTENT_REQUEST_GET_VIDOE);
                    } catch (Exception e) {
                        // ignore: tried to stop a non-existent preview
                    }
                }

                return true;
            }
        });



        Clic_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {

                captureImage();

            }
        });






        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        jpegCallback = new PictureCallback() {



            public void onPictureTaken(byte[] data, Camera camera) {

                try {
                    // convert byte array into bitmap

//
////                    // Solve image inverting problem
//                    if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
//                    {

//
//
//                    }else {
//                        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
//                        angleToRotate = getRoatationAngle(CameraCaptureActivity.this, CameraInfo.CAMERA_FACING_BACK);
//                        angleToRotate = angleToRotate + 180;
//
//                    }






                    if(enable_cfac)
                    {
                        cameraId = CameraInfo.CAMERA_FACING_FRONT;
                        angleToRotate = getRoatationAngle(CameraCaptureActivity.this, CameraInfo.CAMERA_FACING_FRONT);
                        angleToRotate = angleToRotate + 180;
                        Bitmap loadedImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Bitmap bitmap = rotate(loadedImage,angleToRotate);
                        // rotate Image
                        String state = Environment.getExternalStorageState();
                        File folder = null;
                        if (state.contains(Environment.MEDIA_MOUNTED))
                        {
                            folder = new File(Environment.getExternalStorageDirectory() + "/MyCamera");
                        } else
                        {
                            folder = new File(Environment.getExternalStorageDirectory() + "/MyCamera");
                        }

                        boolean success = true;
                        if (!folder.exists())
                        {
                            success = folder.mkdirs();
                        }
                        if (success)
                        {
                            java.util.Date date = new java.util.Date();
                            imageFile = new File(folder.getAbsolutePath() + File.separator + new Timestamp(date.getTime()).toString() + ".jpg");
                            imageFile.createNewFile();

                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Image Not saved", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                        // save image into gallery
                        bitmap.compress(CompressFormat.JPEG,50,ostream);
                        FileOutputStream fout = new FileOutputStream(imageFile);
                        byteArray_image = ostream.toByteArray();
                        fout.write(ostream.toByteArray());
                        fout.close();
                        ContentValues values = new ContentValues();
                        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
                        values.put(Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());
                        CameraCaptureActivity.this.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
                        refreshCamera_finished();

                    }

                    else
                    {
                        Bitmap loadedImage = BitmapFactory.decodeByteArray(data, 0, data.length);

                        // rotate Image
                        Matrix rotateMatrix = new Matrix();
                        rotateMatrix.postRotate(rotation);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), rotateMatrix, false);

                        String state = Environment.getExternalStorageState();
                        File folder = null;
                        if (state.contains(Environment.MEDIA_MOUNTED))
                        {
                            folder = new File(Environment.getExternalStorageDirectory() + "/MyCamera");
                        } else
                        {
                            folder = new File(Environment.getExternalStorageDirectory() + "/MyCamera");
                        }

                        boolean success = true;
                        if (!folder.exists())
                        {
                            success = folder.mkdirs();
                        }
                        if (success)
                        {
                            java.util.Date date = new java.util.Date();
                            imageFile = new File(folder.getAbsolutePath() + File.separator + new Timestamp(date.getTime()).toString() + ".jpg");
                            imageFile.createNewFile();

                        }



                        else
                        {
                            Toast.makeText(getBaseContext(), "Image Not saved", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                        // save image into gallery
                        rotatedBitmap.compress(CompressFormat.JPEG,50,ostream);
                        FileOutputStream fout = new FileOutputStream(imageFile);
                        byteArray_image = ostream.toByteArray();
                        fout.write(ostream.toByteArray());
                        fout.close();
                        ContentValues values = new ContentValues();
                        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
                        values.put(Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());
                        CameraCaptureActivity.this.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
                        refreshCamera_finished();





                    }













                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    public void captureImage()
    {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    public void frontfacing()
    {


        if (camera != null)
        {
            camera.stopPreview();
            camera.release();
            //mCamera = null;
        }

        //swap the id of the camera to be used
        if (cameraId == CameraInfo.CAMERA_FACING_BACK)
        {
            cameraId = CameraInfo.CAMERA_FACING_FRONT;
            enable_cfac = true;

        }else
        {
            cameraId = CameraInfo.CAMERA_FACING_BACK;
            enable_cfac = false;
        }
        try {
            camera = Camera.open(cameraId);
            camera.setDisplayOrientation(90);
            //You must get the holder of SurfaceView!!!
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) { e.printStackTrace(); }


    }









    public void refreshCamera_finished()
    {
        if (surfaceHolder.getSurface() == null)
        {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();

        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);

            camera.startPreview();
            camera.release();
            camera = null;
            Intent i = new Intent(CameraCaptureActivity.this,ViewImageActivity.class);
            i.putExtra("Imagepass", imageFile.getName());
            startActivityForResult(i, NewParadeActivity.INTENT_REQUEST_GET_IMAGES);
        } catch (Exception ignored) {

        }
    }


    public void  refreshCamera()
    {
        if (surfaceHolder.getSurface() == null)
        {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {


        refreshCamera();
    }


    private void setUpCamera(Camera c) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation)
        {
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

        if (info.facing == CameraInfo.CAMERA_FACING_FRONT)
        {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else
        {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Parameters params = c.getParameters();

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null)
        {
            if (focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            {
                params.setFlashMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }






    public void surfaceCreated(SurfaceHolder holder)
    {
        try {
            // open the camera
            camera = Camera.open();
            setUpCamera(camera);

        } catch (RuntimeException e)
        {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Parameters parameters = camera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size cs = sizes.get(0);
        parameters.setPreviewSize(cs.width, cs.height);
        camera.setParameters(parameters);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
//        camera.stopPreview();
//        camera.release();
        camera = null;
    }







    @Override
    public void onBackPressed()
    {
        // do something on back.
        try {
            Intent i = new Intent(CameraCaptureActivity.this, NewParadeActivity.class);
            camera.release();
            camera = null;
            startActivity(i);
            finish();
        } catch (Exception e)
        {
            // ignore: tried to stop a non-existent preview
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);


        Log.i("CReques code", "" + requestCode);

        Log.i("Cresule code",""+ resuleCode);

        if (requestCode == NewParadeActivity.INTENT_REQUEST_GET_IMAGES && resuleCode == 1) //
        {
            String image_uri = intent.getStringExtra("Imagepass");

            Intent i = new Intent();
            i.putExtra("Imagepass", image_uri);
            setResult(1, i);
            finish();
            //do something
        }

        else if(requestCode == NewParadeActivity.INTENT_REQUEST_GET_VIDOE && resuleCode == 2)

        {

            String image_uri = intent.getStringExtra("thumbnailimage");
            String video_uri = intent.getStringExtra("Videourl");
            Intent i = new Intent();
            i.putExtra("thumbnailimage", image_uri);
            i.putExtra("Videourl",video_uri);
            setResult(2, i);
            finish();
        }

    }
    public static int getRoatationAngle(Activity mContext, int cameraId) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }







}