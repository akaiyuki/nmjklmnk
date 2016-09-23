package com.fasionparade.fasionparadeApp.Functions.Service;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;


import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by ksuresh on 4/4/2016.
 */
public class WebserviceAssessor {


    JSONObject jArray = null;
    static JSONObject jsonResponse;

    static String bufferData;
    public static int RESPONSE_OK = 200;
    static InputStream is = null;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    private static OkHttpClient getClient() {
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        client.writeTimeout(1000, TimeUnit.MILLISECONDS);
//        client.readTimeout(1000, TimeUnit.MILLISECONDS);
//        client.retryOnConnectionFailure(true);
//        return client.build();

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setWriteTimeout(60, TimeUnit.SECONDS);
        return client;
    }

    public static String postImageData(Context context, String url, String userId, String image, String imageType) {
        String result = null;
        try {

            final Context mContext = context;
            MediaType MEDIA_TYPE = null;
            String mediaType = null;
//
//            final Handler mHandler;
//            mHandler = new Handler(Looper.getMainLooper());


            if (imageType.equals("0")) {
                MEDIA_TYPE = image.endsWith("png") ?
                        MediaType.parse("image/png") : MediaType.parse("image/jpeg");
                mediaType = "image";
            } else if (imageType.equals("1")) {
                MEDIA_TYPE = MediaType.parse("video/mp4");
                mediaType = "video";
                // image="/storage/sdcard0/Folder/parade.mp4";
            }
            File myFile = new File(image);
            Log.d("TAG", "File...::::" + myFile + " : " + myFile.exists());


            OkHttpClient client = getClient();

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart(mediaType, "file", RequestBody.create(MEDIA_TYPE, myFile))
                    .addFormDataPart("imageType", imageType)
                    .addFormDataPart("userId", userId)
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
//            client.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onFailure(final Request request, final IOException e) {
//
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("LOG_TAG", e.toString());
//                            Toast.makeText(mContext, "Time out Error", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    final String message = response.body().string();
//
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.i("LOG_TAG", message);
//                            result=message;
//                           // Toast.makeText(mContext,"suceess-->"+ message, Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//            });


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("LOG_TAG--->", result);
        return result;
    }

    public static String postData(Context context, String url, String paradeTag, String paradeDisc, String duration, String userId, String userType, String images, String Share, String name ,String groupId) {

        String result = null;
        try {
            final Context mContext = context;
            RequestBody formBody = null;

            // paradeName,tag,userId,userType,userName,sharedWith,duration,aboutParade,images,groupId,stTime,edTime
            OkHttpClient client = getClient();

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("userId", userId)
                    .addFormDataPart("sharedWith", Share)
                    .addFormDataPart("duration", duration)
                    .addFormDataPart("aboutParade", paradeDisc)
                    .addFormDataPart("tag", paradeTag)
                    .addFormDataPart("userName", name)
                    .addFormDataPart("userType", userType)
                    .addFormDataPart("images", images)
                    .addFormDataPart("groupId", groupId)

                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
    public static String postedItData(Context context, String url, String paradeTag, String paradeDisc, String duration, String userId, String userType, String images, String Share, String name ,String groupId,String paradeId) {

        String result = null;
        try {
            final Context mContext = context;
            RequestBody formBody = null;

            // paradeName,tag,userId,userType,userName,sharedWith,duration,aboutParade,images,groupId,stTime,edTime
            OkHttpClient client = getClient();

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("userId", userId)
                    .addFormDataPart("sharedWith", Share)
                    .addFormDataPart("duration", duration)
                    .addFormDataPart("aboutParade", paradeDisc)
                    .addFormDataPart("tag", paradeTag)
                    .addFormDataPart("userName", name)
                    .addFormDataPart("userType", userType)
                    .addFormDataPart("images", images)
                    .addFormDataPart("groupId", groupId)
                    .addFormDataPart("paradeId", paradeId)

                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    public static String postProfile(String url, String paradeNametag, String paradeDisc, String paradeName, String duration, String profile, String userId , String code , String phoneNo) {


        try {


            RequestBody formBody = null;
            RequestBody requestBody = null;
            if (paradeName.isEmpty())
                paradeName = "";
            if (paradeDisc.isEmpty())
                paradeDisc = "";

            final MediaType MEDIA_TYPE = profile.endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");
            File myFile = new File(profile);


            OkHttpClient client = getClient();

            if (profile.isEmpty()) {
                requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("userName", paradeNametag)
                        .addFormDataPart("profilePic", "")
                        .addFormDataPart("userId", userId)
                        .addFormDataPart("bio", paradeName)
                        .addFormDataPart("website", paradeDisc)
                        .addFormDataPart("mail", duration)
                        .addFormDataPart("countryCode", code)
                        .addFormDataPart("phoneNumber", phoneNo)
                        .build();
            } else {
                requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("profilePic", "file", RequestBody.create(MEDIA_TYPE, myFile))
                        .addFormDataPart("userName", paradeNametag)
                        .addFormDataPart("userId", userId)
                        .addFormDataPart("bio", paradeName)
                        .addFormDataPart("website", paradeDisc)
                        .addFormDataPart("mail", duration)
                        .addFormDataPart("countryCode", code)
                        .addFormDataPart("phoneNumber", phoneNo)
                        .build();
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            bufferData = response.body().string();
            System.out.println("code------>" + code+"\n"+phoneNo);
            System.out.println("bufferData------>" + bufferData);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bufferData;
    }

    public static String getData(String url) {

        try {
            OkHttpClient client = new OkHttpClient();

            Log.e("url-->", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            bufferData = response.body().string();
            System.out.println(bufferData);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return bufferData;
    }
}
