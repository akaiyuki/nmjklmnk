package com.fasionparade.fasionparadeApp.Models.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Fragments.SettingsFragment;
import com.fasionparade.fasionparadeApp.R;

public class SettingsActivity extends BaseActivity {

    public static SettingsActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        INSTANCE = this;

        setFrameLayout(R.id.mainContentLayout);

        MEngine.switchFragment(INSTANCE, new SettingsFragment(), getFrameLayout());

    }

    @Override
    public void onBackPressed()
    {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            startActivity(new Intent(SettingsActivity.this, FashionHomeActivity.class));
            finish();
        }

    }

}
