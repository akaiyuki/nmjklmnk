package com.fasionparade.fasionparadeApp.Functions.Core;

/**
 * Created by Vishnu on 8/4/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.fasionparade.fasionparadeApp.BuildConfig;
import com.fasionparade.fasionparadeApp.Models.Fragments.InboxFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.MyParadesFragment;
import io.fabric.sdk.android.Fabric;


public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    private Activity mCurrentActivity = null;

    private InboxFragment inboxFragment =null;
    private MyParadesFragment myParadesFragment =null;

    @Override
    public void onCreate()
    {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());

        if (!BuildConfig.DEBUG){
            Fabric.with(this, new Crashlytics());
        }


        mInstance = this;
        MSharedPreferences.init(mInstance);

        FacebookSdk.sdkInitialize(getApplicationContext());

    }
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(AppController.this);
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }


    public void setInboxFragment(InboxFragment inboxFragment){
        this.inboxFragment = inboxFragment;
    }
    public InboxFragment getInboxFragment(){

        return inboxFragment;
    }
    public void setMyParadesFragment(MyParadesFragment myParadesFragment){
        this.myParadesFragment = myParadesFragment;
    }
    public MyParadesFragment getMyParadesFragment(){

        return myParadesFragment;
    }

}