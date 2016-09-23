package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.Functions.Service.RegistrationIntentService;
import com.fasionparade.fasionparadeApp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class FashionMainActivity extends AppCompatActivity {

    TextView loginButton,signUpButton;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion_main);

        loginButton=(TextView)findViewById(R.id.loginButton);
        signUpButton=(TextView)findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FashionMainActivity.this,FashionLoginActivity.class);
                startActivity(intent);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FashionMainActivity.this, FashionSignUpActivity.class);
                startActivity(intent);

            }
        });
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }else{
            Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(FashionMainActivity.this);
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, FashionMainActivity.this, 0);
            if (dialog != null) {
                //This dialog will help the user update to the latest GooglePlayServices
                dialog.show();
            }
        }

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(FashionMainActivity.this,FashonLoginActivity.class);
                startActivity(intent);

            }
        },3000);*/
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
