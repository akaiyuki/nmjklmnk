package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Core.MSharedPreferences;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;




public class FashionLoginActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    Button loginButton, fb;
    LoginButton fb_loginButton;
    ImageView topLogoImage;
    CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    List<String> permissionNeeds = Arrays.asList("user_photos", "email",
            "user_birthday", "public_profile", "AccessToken");
    String name, email, gender, sUserName, sPassword;
    EditText userName, password;
    Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_fashon_login);
        context = this;
        sharedPreferences = getApplicationContext().getSharedPreferences(Flag.USER_DETAILS,
                Context.MODE_PRIVATE);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.choosePasswordTextInputLayout);
        userName.setText("test2@gmail.com");
        password.setText("test1234");
        fb = (Button) findViewById(R.id.fb);
        fb_loginButton = (LoginButton) findViewById(R.id.login_button);
        fb_loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        fb_loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {

                                        try {
                                            name = object.getString("name");
                                            email = object.getString("email");
                                            gender = object.getString("gender");
                                            //  String birthday = object.getString("birthday");
                                            System.out.println(name);
                                            System.out.println(email);
                                            System.out.println(gender);
                                            //  System.out.println(birthday);

                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                updateUI();

            }
        };


        topLogoImage = (ImageView) findViewById(R.id.topLogoImage);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInUser();


            }
        });

        topLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FashionLoginActivity.this, ParadeResultActivity.class);
                startActivity(intent);
            }
        });
        Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode == ConnectionResult.SUCCESS) {
            //Do what you want
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, FashionLoginActivity.this, 0);
            if (dialog != null) {
                //This dialog will help the user update to the latest GooglePlayServices
                dialog.show();
            }
        }
       // setupWindowAnimations();
    }

    //comment method because of unused
//    private void setupWindowAnimations() {
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        getWindow().setEnterTransition(fade);
//    }
    private void signInUser() {

        sUserName = userName.getText().toString();
        sPassword = password.getText().toString();
        if ((sUserName == "" || sUserName.isEmpty())
                || (sPassword == "" || sPassword.isEmpty())
                ) {
            Toast.makeText(FashionLoginActivity.this,
                    "Please enter all the fields", Toast.LENGTH_SHORT)
                    .show();
        } else {

            getLoginUser(sUserName, sPassword);

        }
    }

    private void getLoginUser(String sUserName, String sPassword) {
        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.login();
            sUserName = sUserName.trim();
            sPassword = sPassword.trim();
           Log.e("deviceId-->", Flag.deviceId);

            // emailId=test@gmail.com&password=123
            url = url + "emailId=" + sUserName + "&password=" + sPassword;

            new userLogin().execute(url);

        }
    }

    public void onClick(View v) {
        if (v == fb) {
            fb_loginButton.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;


        Profile profile = Profile.getCurrentProfile();

        if (enableButtons && profile != null) {
            String accessToken = AccessToken.getCurrentAccessToken().getToken();
            Log.e("profile", profile.getFirstName());
            Log.e("id", profile.getId());
            Log.e("accessToken", accessToken);
            Log.e("accessToken", profile.getName());
            Log.e("accessToken", profile.toString());
            // profilePictureView.setProfileId(profile.getId());
            // greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
        } else {
            //  profilePictureView.setProfileId(null);
            //  greeting.setText(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);

        updateUI();
        stopAnim();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    //
//    private class userLogin {
//
//    }
    private class userLogin extends AsyncTask<String, Void, String> {
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
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {
                    Toast.makeText(FashionLoginActivity.this,
                            message, Toast.LENGTH_SHORT)
                            .show();
                   // List<User> user = JsonParser.getUser(jsonObject);
                    User user = JsonParser.getUser(jsonObject);
                    if (user.message.equals("success")) {
                        String loginUser = Utils.getGson().toJson(user,
                                User.class);
                        editor = sharedPreferences.edit();
                        editor.putString(Flag.USER_DATA,loginUser);
                        editor.commit();

//                        MSharedPreferences.setSomeStringValue(AppController.getInstance(),Flag.USER_DATA,loginUser);

                    }
                    Intent intent = new Intent(FashionLoginActivity.this, FashionHomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(FashionLoginActivity.this,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            }catch(Exception e){
                Toast.makeText(FashionLoginActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

    void startAnim() {
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }
}
