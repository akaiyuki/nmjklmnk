package com.fasionparade.fasionparadeApp.Models.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Fragments.ContactsHelpFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.HomeHelpFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.InboxHelpFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.ParadeHelpFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.ProfileHelpFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.PublicHelpFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.SettingHelpFragment;


public class HelpActivity extends FragmentActivity {
    public static ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        //  viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(6);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f;

            if (position == 0) {
                f = new HomeHelpFragment();
            } else if (position == 1) {
                f = new ParadeHelpFragment();
            } else if (position == 2) {
                f = new InboxHelpFragment();
            } else if (position == 3) {
                f = new PublicHelpFragment();
            }else if (position == 4) {
                f = new ContactsHelpFragment();
            }else if (position == 5) {
                f = new ProfileHelpFragment();
            }else if (position == 6) {
                f = new SettingHelpFragment();
            }
            else{
                f = new HomeHelpFragment();
                Log.i("Invalid", "Index");
            }

            return f;
        }

        @Override
        public int getCount() {
            return 7;
        }


    }

}
