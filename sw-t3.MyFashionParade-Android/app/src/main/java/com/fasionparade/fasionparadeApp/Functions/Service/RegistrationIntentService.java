package com.fasionparade.fasionparadeApp.Functions.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by ksuresh on 4/5/2016.
 */
public class RegistrationIntentService extends IntentService {
    // abbreviated tag name

    private static final String TAG = "RegIntentService";



    public RegistrationIntentService() {

        super(TAG);

    }



    @Override

    protected void onHandleIntent(Intent intent) {

        // Make a call to Instance API

        InstanceID instanceID = InstanceID.getInstance(this);

        String senderId = getResources().getString(R.string.sender_id);

        try {

            // request token that will be used by the server to send push notifications

            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);

            Log.d(TAG, "GCM Registration Token: " + token);

            Flag.deviceId=token;

            // pass along this data

            sendRegistrationToServer(token);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    private void sendRegistrationToServer(String token) {

        // Add custom implementation, as needed.

    }
}
