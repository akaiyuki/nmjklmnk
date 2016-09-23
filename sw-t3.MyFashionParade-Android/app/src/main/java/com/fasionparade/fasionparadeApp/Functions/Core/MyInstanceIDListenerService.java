package com.fasionparade.fasionparadeApp.Functions.Core;

import android.content.Intent;
import android.util.Log;

import com.fasionparade.fasionparadeApp.Functions.Service.RegistrationIntentService;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by ksuresh on 4/5/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override

    public void onTokenRefresh() {

        // Fetch updated Instance ID token and notify of changes
        Log.d("TAG----", "GCM Registration Token: ");
        Intent intent = new Intent(this, RegistrationIntentService.class);

        startService(intent);

    }
}
