package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.NewParadeAdapter;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.FileType;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewParadeActivity extends AppCompatActivity {

    public static final int INTENT_REQUEST_GET_IMAGES = 6;


    public static final int INTENT_REQUEST_GET_VIDOE = 14;

    public static Activity newParadeActivity;
    RecyclerView imageRecyclerView;
    static RelativeLayout clockDropDownLayout, cameraLayout, cameraSubLayout, imageLayout;
    static TextView startParadeTextView, cancelTextView, setDurationTextView, back;
    static ScrollView mainLayout;

    ArrayList<Uri> totalImageUris = new ArrayList<>();

    ArrayList<Uri> image_uris = new ArrayList<>();

    NewParadeAdapter newParadeAdapter;
    boolean alreadyAdded = false;
    boolean fromCamera = false;
    ImageView backImageView, profileImageView;
    //  String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example"};
    LinearLayout publicLayout, groupLayout, contactLayout;
    EditText tapHereHashTagTextView, tapHereVoterInfoTextView;
    static String paradeDisc, paradeTag, duration, userId, userType, sharedWith, groupId = "";
    String images, jsonData, index;
    List<ActiveParade> dataList;
    List<FileType> productList;
    User user;
    static Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    Config config = new Config();
    String paradeId="",timeDuration="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_parade);
        context = this;
        newParadeActivity = this;

        imageRecyclerView = (RecyclerView) findViewById(R.id.imageRecycler);
        // choseImageTextView = (TextView) findViewById(R.id.choseImageTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        clockDropDownLayout = (RelativeLayout) findViewById(R.id.clockDropDownLayout);
        startParadeTextView = (TextView) findViewById(R.id.startParadeTextView);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        back = (TextView) findViewById(R.id.back);
        setDurationTextView = (TextView) findViewById(R.id.setDurationTextView);
        publicLayout = (LinearLayout) findViewById(R.id.publicLayout);
        groupLayout = (LinearLayout) findViewById(R.id.groupLayout);
        contactLayout = (LinearLayout) findViewById(R.id.contactLayout);
        cameraLayout = (RelativeLayout) findViewById(R.id.cameraLayout);
        mainLayout = (ScrollView) findViewById(R.id.mainLayout);
        cameraSubLayout = (RelativeLayout) findViewById(R.id.cameraSubLayout);
        imageLayout = (RelativeLayout) findViewById(R.id.imageLayout);
        cameraLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        tapHereHashTagTextView = (EditText) findViewById(R.id.tapHereHashTagTextView);
        tapHereVoterInfoTextView = (EditText) findViewById(R.id.tapHereVoterInfoTextView);
        //tapHereTextView = (EditText) findViewById(R.id.tapHereTextView);
        user = Utils.getUserFromPreference(context);
        FashionHomeActivity.userType = user.userType;

        sharedWith = "1";
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gridSpacing);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, spacingInPixels, true);

        imageRecyclerView.addItemDecoration(gridSpacingItemDecoration);


        //  config.setCameraHeight(android.R.dimen.app_camera_height);
        config.setToolbarTitleRes(R.string.app_name);

        config.setSelectionMin(1);
        config.setSelectionLimit(Flag.imageLimit);
        // config.setSelectedBottomHeight(R.dimen.bottom_height);
        ImagePickerActivity.setConfig(config);

        if (FashionHomeActivity.repost_clicked) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            jsonData = getIntent().getStringExtra("data");
            index = getIntent().getStringExtra("index");
            String jsonArray = getIntent().getStringExtra("jsonArray");
            Gson gson = new Gson();
            Type type = new TypeToken<List<ActiveParade>>() {
            }.getType();
            dataList = gson.fromJson(jsonData, type);
            try {
                tapHereVoterInfoTextView.setText(dataList.get(Integer.parseInt(index)).aboutParade);
                tapHereHashTagTextView.setText(dataList.get(Integer.parseInt(index)).tag);
                getDuration(dataList.get(Integer.parseInt(index)).duration);
                paradeId = dataList.get(Integer.parseInt(index)).paradeId;
                duration = dataList.get(Integer.parseInt(index)).duration;
                JSONArray imageArray = new JSONArray(jsonArray);
                productList = new ArrayList<FileType>();

                JSONObject imageObject;
                String image;
                Flag.totalImageUris.clear();
                for (int i = 0; i < imageArray.length(); i++) {

                    FileType fileType = new FileType();

                    imageObject = (JSONObject) imageArray.get(i);
                    fileType.fileName = imageObject.getString("fileName");
                    fileType.type = imageObject.getString("fileType");
                     URL url = ConvertToUrl((imageObject.getString("fileName")));
                    Bitmap imagebitmap = null;
                    try {
                        imagebitmap = BitmapFactory.decodeStream(url.openConnection()
                                .getInputStream());
                        Flag.totalImageUris.add(getImageUri(NewParadeActivity.this, imagebitmap));
                        Flag.totalImage.add(imageObject.getString("imageId"));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        Flag.totalImageUris.add(Uri.parse(imageObject.getString("fileName")));
                        Flag.totalImage.add(imageObject.getString("imageId"));
                    }

                    productList.add(fileType);
                }
                if (dataList.get(Integer.parseInt(index)).sharedWith.equals("2")) {
                    sharedWith = "2";
                    groupId = dataList.get(Integer.parseInt(index)).groupId;

                    tapHereHashTagTextView.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        publicLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                        groupLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                        contactLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                    } else {
                        publicLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                        groupLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                        contactLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                    }

                } else if (dataList.get(Integer.parseInt(index)).sharedWith.equals("3")) {
                    sharedWith = "3";
                    groupId = dataList.get(Integer.parseInt(index)).groupId;

                    tapHereHashTagTextView.setVisibility(View.INVISIBLE);

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
                else
                    groupId = "";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        cameraSubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fromCamera = true;
                Intent i = new Intent(NewParadeActivity.this, CameraCaptureActivity.class);
                startActivityForResult(i, INTENT_REQUEST_GET_IMAGES);
                cameraLayout.setVisibility(View.GONE);


            }
        });
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromCamera = false;
                if (FashionHomeActivity.userType.equals("0"))
                    Flag.imageLimit = 2;
                else if (FashionHomeActivity.userType.equals("1"))
                    Flag.imageLimit = 6;
                Flag.imageLimit = Flag.imageLimit - Flag.totalImageUris.size();
                Toast.makeText(context, "ss" + FashionHomeActivity.userType, Toast.LENGTH_LONG).show();
                // Toast.makeText(context, "Flag.imageLimit" +Flag.imageLimit, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Flag.imageLimit);
                User user = Utils.getUserFromPreference(NewParadeActivity.this);
                intent.putExtra("UserType", user.userType);
                startActivityForResult(intent, Constants.REQUEST_CODE);
                cameraLayout.setVisibility(View.GONE);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }
        });
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
                Intent intent = new Intent(NewParadeActivity.this, FashionHomeActivity.class);
                startActivity(intent);
                finish();


            }
        });
        clockDropDownLayout.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {

                                                       DialogFragment newFragment = new TimePickerFragment();
                                                       newFragment.show(getSupportFragmentManager(), "timePicker");

                                                   }
                                               }

        );
        //Initially grid show with eight Plus images
        newParadeAdapter = new NewParadeAdapter(context, Flag.totalImageUris, user.userType);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(NewParadeActivity.this, 3);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        imageRecyclerView.setAdapter(newParadeAdapter);


        startParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startNewParade();
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        publicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedWith = "1";
                groupId = "";

                tapHereHashTagTextView.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    publicLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted));
                    groupLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                    contactLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                }else {
                    publicLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                    groupLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                    contactLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                }

            }
        });

        groupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedWith = "2";
                groupId = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    publicLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                    groupLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                    contactLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                } else {
                    publicLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                    groupLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                    contactLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                }

                tapHereHashTagTextView.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(NewParadeActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });

        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedWith = "3";
                groupId = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    publicLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                    groupLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundColor));
                    contactLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                } else {
                    publicLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                    groupLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundColor));
                    contactLayout.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                }

                tapHereHashTagTextView.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(NewParadeActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        //paradeImageUpload("ass");




        //trial for on touch edittext removed hint text
        tapHereVoterInfoTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    tapHereVoterInfoTextView.setHint("");
                else
                    tapHereVoterInfoTextView.setHint("Your hint");
            }
        });



    }

    public void showAlertDialog(final Context context, String title, String message,
                                Boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)

            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Upgrade to Premium", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // sendIntent();
                    Intent intent = new Intent(context, UpgradeActivity.class);
                    context.startActivity(intent);

//                    Intent intent = new Intent(NewParadeActivity.this, NotificationCongratulation.class);
//                    startActivity(intent);


                }
            });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });


        // Showing Alert Message
        alertDialog.show();
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            if (hourOfDay == 1) {
                setDurationTextView.setText(hourOfDay + " hour " + minute + " mins ");
            } else if (hourOfDay == 0) {
                setDurationTextView.setText(minute + " mins ");
            } else {
                setDurationTextView.setText(hourOfDay + " hours " + minute + " mins ");
            }
            duration = String.valueOf(hourOfDay * 60 + minute);
            System.out.println("duration" + duration);
        }
    }

    private void startNewParade() {
        //paradeName,tag,userId,userType,userName,sharedWith,duration,aboutParade,images,groupId,stTime,edTime
        //  paradeNametag = tapHereTextView.getText().toString();
        paradeDisc = tapHereVoterInfoTextView.getText().toString();
        paradeTag = tapHereHashTagTextView.getText().toString();
        //   images = "49,48";

        userId = user.id;
        userType = user.userType;

        if (Flag.totalImageUris.size() > 0) {
            if (groupId.isEmpty()) {
                if ((paradeTag == "" || paradeTag.isEmpty() || paradeDisc == "" || paradeDisc.isEmpty())) {
                    Toast.makeText(context,
                            "Please enter all the fields", Toast.LENGTH_SHORT)
                            .show();
                }/* else if (!(Flag.totalImageUris.size() == Flag.totalImage.size())) {
                    Toast.makeText(context,
                            "Please wait some time,picture uploading", Toast.LENGTH_SHORT)
                            .show();
                } */else {
                    sendNewParade();
                }
            } else {
                if (paradeDisc == "" || paradeDisc.isEmpty()) {
                    Toast.makeText(context,
                            "Please enter all the fields", Toast.LENGTH_SHORT)
                            .show();
                } else if (!(Flag.totalImageUris.size() == Flag.totalImage.size())) {
                    Toast.makeText(context,
                            "Please wait some time,picture uploading", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    sendNewParade();
                }
            }


        } else {
            Toast.makeText(context,
                    "Please select atleast two pictures ", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void sendNewParade() {
        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url;
            if (FashionHomeActivity.repost_clicked)
                url = ResourceManager.editParade();
            else
                url = ResourceManager.newParade();

            for (int i = 0; i < Flag.totalImage.size(); i++) {


                if (i == 0)
                    images = Flag.totalImage.get(i);
                else
                    images = images + "," + Flag.totalImage.get(i);

            }
            new paradeSend().execute(url);
        }
    }

    private class paradeSend extends AsyncTask<String, Void, String> {
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
            // paradeName,tag,userId,userType,userName,sharedWith,duration,aboutParade,images,groupId,stTime,edTime
            String result;
            if(FashionHomeActivity.repost_clicked) {
                result = WebserviceAssessor.postedItData(context, params[0], paradeTag, paradeDisc, duration, userId, userType, images, sharedWith, user.contactName, groupId, paradeId);
            }
            else
                result = WebserviceAssessor.postData(context, params[0], paradeTag, paradeDisc, duration, userId, userType, images, sharedWith, user.contactName, groupId);

            return result;


        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;

                errorCode = jsonObject.getString("errorCode");

                if (errorCode.equals("200")) {
                    Intent intent = new Intent(NewParadeActivity.this, NotificationCongratulationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    alert.showAlertDialog(context, "MFP Error",
                            "Somthing went wrong", false);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            pDialog.cancel();
        }
    }

    private class paradeImageSend extends AsyncTask<String, Void, String> {
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

            String result = WebserviceAssessor.postImageData(context, params[0], user.id, params[1], params[2]);


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

                if (errorCode.equals("200")) {
                    JSONArray array = jsonObject.getJSONArray("imageDetails");
                    JSONObject jObject;
                    for (int i = 0; i < array.length(); ++i) {

                        jObject = array.getJSONObject(i);

                        Flag.totalImage.add(jObject.getString("imageId"));

                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            pDialog.cancel();

        }
    }

    public void paradeImageUpload(String imagePath, String fileFormate) {
        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.imageUpload();


            new paradeImageSend().execute(url, imagePath, fileFormate);
        }
    }

    public static void cameraViewOpen(String userType, Context mContext) {
        System.out.print("userType====" + userType);
        if (userType.equals("0") && Flag.totalImageUris.size() >= 2) {
            NewParadeActivity camera = new NewParadeActivity();
            camera.upgradeAlertFunction(mContext);
        } else {
            cameraLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }

    public void upgradeAlertFunction(Context context) {
        showAlertDialog(context, "Upgrade MFP", "Sorry ,Free users can only add 2 pics per parade \n\n Upgrade to premium to add more pics, videos and unlock inlimited parades", false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);
        mainLayout.setVisibility(View.VISIBLE);
        if (fromCamera) {
            //Vishnu
            if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == 1) //Activity.RESULT_OK
            {

                String image_uri = intent.getStringExtra("Imagepass");
                Uri uri = Uri.parse(image_uri);
                paradeImageUpload(image_uri, "0");
                image_uris.add(uri);


                Flag.totalImageUris = image_uris;


            } else if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == 2)

            {
                String image_uri = intent.getStringExtra("thumbnailimage");
                String video_uri = intent.getStringExtra("Videourl");
                paradeImageUpload(video_uri, "1");
                Log.i("Video url", video_uri);
                Log.i("Image url", image_uri);
                Uri uri = Uri.parse(image_uri);
                image_uris.add(uri);
                Flag.totalImageUris = image_uris;
            }

        } else {
            //By Suresh

            if (requestCode == Constants.REQUEST_CODE && resuleCode == RESULT_OK && intent != null) {
                ArrayList<Image> images = intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

                for (int i = 0, l = images.size(); i < l; i++) {

                    paradeImageUpload(images.get(i).path, "0");
                    Flag.totalImageUris.add(Uri.parse(images.get(i).path));
                }
            } else if (requestCode == Constants.REQUEST_CODE && resuleCode == RESULT_CANCELED && intent != null) {


                if (intent.getStringExtra(Constants.INTENT_EXTRA_IMAGES).equals("upgrade")) {
                    Intent intent1 = new Intent(this, UpgradeActivity.class);

                    startActivity(intent1);
                }
            }
        }
        newParadeAdapter = new NewParadeAdapter(context, Flag.totalImageUris, user.userType);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(NewParadeActivity.this, 3);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        imageRecyclerView.setAdapter(newParadeAdapter);

    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public void getDuration(String duration) {
        int min = 0, hours;
        int time = Integer.parseInt(duration);
        if (time >= 60) {
            hours = time / 60;
            min = time % 60;
            if (hours > 0 && min > 0)
                setDurationTextView.setText(hours + " hours " + min + " mins");
            else if (hours > 0)
                setDurationTextView.setText(hours + " hours");
            else
                setDurationTextView.setText(min + " mins");
        } else
            setDurationTextView.setText(duration + " mins");

    }

    private URL ConvertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
