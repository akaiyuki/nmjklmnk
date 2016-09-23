package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Service.RegistrationIntentService;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Views.Slider.FragmentOne;
import com.fasionparade.fasionparadeApp.Views.Slider.FragmentTwo;
import com.fasionparade.fasionparadeApp.Views.Slider.FragmentThree;
import com.fasionparade.fasionparadeApp.Views.Slider.FragmentFour;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends BaseActivity {
    public static ViewPager viewPager;
    SharedPreferences sharedPreferences;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        viewPager=(ViewPager)findViewById(R.id.viewPager);


        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        //  viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(3);
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }else{
            Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, 0);
            if (dialog != null) {
                //This dialog will help the user update to the latest GooglePlayServices
                dialog.show();
            }
        }
        loginSessionCheck();


//        trial for circle page
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator_default);
        indicator.setViewPager(viewPager);


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
    public void loginSessionCheck(){

        if(!Utils.isLoggedIn(context)){
//            Intent i = new Intent(context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            context.startActivity(i);
        }else{
            Intent intent = new Intent(context, FashionHomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f;

            if (position == 0) {
                f = new FragmentOne();
            } else if (position == 1) {
                f = new FragmentTwo();
            } else if (position == 2) {
                f = new FragmentThree();
            } else if (position == 3) {
                f = new FragmentFour();
            }else{
                f = new FragmentOne();
                Log.i("Invalid","Index");
            }

            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }


    }
}
