package com.fasionparade.fasionparadeApp.Models.Activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Functions.Core.MSharedPreferences;
import com.fasionparade.fasionparadeApp.Models.Fragments.MyProfileFragment;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Database.DatabaseHandler;
import com.fasionparade.fasionparadeApp.Models.Fragments.BrowseFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.ContactGroupDetailFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.ContactsFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.HomeFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.InboxFragment;
import com.fasionparade.fasionparadeApp.Models.Fragments.MyParadesFragment;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import android.Manifest;


public class FashionHomeActivity extends BaseActivity implements ContactGroupDetailFragment.TextClicked {

    RelativeLayout homeLayout, myParadeLayout, invitesLayout, browseLayout, contactLayout;//chatLayout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    User user;
    public static FashionHomeActivity INSTANCE = null;

    Fragment mFragOnDisplay = null;

    TextView inboxBadgeTextView,ParadeBadgeTextView;

    public static boolean group_clicked,newparade_added,_areLecturesLoaded = false,winner_clicked,parade_clicked,repost_clicked;
    SharedPreferences mSharedPreferences;

    private static final String[] CONTENT = new String[]{"Home", "My Parades", "Inbox", "Public", "Contacts"};
    public static String userType="";

    private RelativeLayout relHomeLayout;
    private RelativeLayout relParadesLayout;
    private RelativeLayout relInboxLayout;
    private RelativeLayout relPublicLayout;
    private RelativeLayout relContactsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion_home);

        INSTANCE = this;

        homeLayout = (RelativeLayout) findViewById(R.id.homeLayout);
        myParadeLayout = (RelativeLayout) findViewById(R.id.myParadeLayout);
        invitesLayout = (RelativeLayout) findViewById(R.id.inboxLayout);
        browseLayout = (RelativeLayout) findViewById(R.id.browseLayout);
        contactLayout = (RelativeLayout) findViewById(R.id.contactLayout);
        //  chatLayout=(RelativeLayout)findViewById(R.id.chatLayout);

        inboxBadgeTextView = (TextView) findViewById(R.id.inboxBadgeTextView);
        ParadeBadgeTextView = (TextView) findViewById(R.id.ParadeBadgeTextView);

        fragmentManager = getSupportFragmentManager();


        setFrameLayout(R.id.mainContentLayout);


        relHomeLayout = (RelativeLayout) findViewById(R.id.homeLayout);
        relParadesLayout = (RelativeLayout) findViewById(R.id.myParadeLayout);
        relInboxLayout = (RelativeLayout) findViewById(R.id.inboxLayout);
        relPublicLayout = (RelativeLayout) findViewById(R.id.browseLayout);
        relContactsLayout = (RelativeLayout) findViewById(R.id.contactLayout);

        homeSelected();

        relHomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeSelected();
            }
        });

        relParadesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myParadeSelected();
            }
        });

        relInboxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitesSelected();
            }
        });

        relPublicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseSelected();
            }
        });

        relContactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSelected();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("FROM")) {

                String from = intent.getStringExtra("FROM");

                if (from.equals("INBOX_CHAT_NOTIFICATION")) {
                    Log.i("FROM", "INBOX_CHAT_NOTIFICATION");

                    invitesSelected();
                } else if (from.equals("INBOX_INVITE_NOTIFICATION")) {
                    Log.i("FROM", "INBOX_INVITE_NOTIFICATION");

                    invitesSelected();
                } else if (from.equals("MAIN_PAGE_NOTIFICATION")) {

                    Log.i("FROM", "MAIN_PAGE_NOTIFICATION");
                    homeSelected();
                } else if (from.equals("MY_PARADE_NOTIFICATION")) {
                    Log.i("FROM", "MY_PARADE_NOTIFICATION");

                    myParadeSelected();
                } else {
                    Log.i("FROM", "last else");
                }

            } else {
                Log.i("No FROM", "No FROM");

            }

        } else {

            Log.i("Intent ", "NULL");
        }


        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeSelected();
//               MEngine.switchFragment(INSTANCE, new HomeFragment(), getFrameLayout());

            }
        });

        myParadeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 myParadeSelected();

//                MEngine.switchFragment(INSTANCE, new MyParadesFragment(), getFrameLayout());

            }
        });

        invitesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 invitesSelected();

//                MEngine.switchFragment(INSTANCE, new InboxFragment(), getFrameLayout());
            }
        });

        browseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  browseSelected();

//                MEngine.switchFragment(INSTANCE, new BrowseFragment(), getFrameLayout());

            }
        });

        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  contactSelected();
                FashionHomeActivity.group_clicked=false;
//                MEngine.switchFragment(INSTANCE, new ContactsFragment(), getFrameLayout());
            }
        });

      /*  chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  chatSelected();
                viewPager.setCurrentItem(5);
            }
        });*/

        //testing
//        user = Utils.getUserFromPreference(this);
//        System.out.println("----->" + user.contactName);

        user = MSharedPreferences.getUserFromPreference(AppController.getInstance());
        System.out.println("----->" + user.contactName);

        askPermission();
    }


    class FashionHomeAdapter extends FragmentStatePagerAdapter {

        public FashionHomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            System.out.println("CHECK FRAGMENT POSITION---------" + position);
            Fragment f = new Fragment();
            switch (position) {
                case 0:
                    f = new HomeFragment();
                    break;
                case 1:
                    f = new MyParadesFragment();
                    break;
                case 2:
//                    f = InboxFragment.newInstance("", "");
                    f = new InboxFragment();
                    break;
                case 3:
                    f = new BrowseFragment();
                    break;
                case 4:
                    f = new MyProfileFragment();
                    break;
                default:
                    return new HomeFragment();

            }

            mFragOnDisplay = f;

            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toString();
        }

        @Override
        public int getCount() {

            return 5;
            //return CONTENT.length;
        }

    }


    public void homeSelected() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
        } else {
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
        }

        MEngine.switchFooterSelection(INSTANCE, new HomeFragment(), getFrameLayout());

        otherSelected();

    }

    public void myParadeSelected() {

        MEngine.switchFooterSelection(INSTANCE, new MyParadesFragment(), getFrameLayout());

        ParadeBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);
        inboxBadgeTextView.setBackgroundResource(R.drawable.bg_circle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            ParadeBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlackText));
            inboxBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhiteText));

        } else {
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            ParadeBadgeTextView.setTextColor(getResources().getColor(R.color.colorBlackText));
            inboxBadgeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
        }

    }

    public void invitesSelected() {

        MEngine.switchFooterSelection(INSTANCE, new InboxFragment(), getFrameLayout());

        inboxBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            inboxBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlackText));
        } else {
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            inboxBadgeTextView.setTextColor(getResources().getColor(R.color.colorBlackText));
        }

    }

    public void browseSelected() {

        MEngine.switchFooterSelection(INSTANCE, new BrowseFragment(), getFrameLayout());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
        }else {
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
        }

        otherSelected();

    }

    public void contactSelected() {

        MEngine.switchFooterSelection(INSTANCE, new MyProfileFragment(), getFrameLayout());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
        } else {
            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
        }

        otherSelected();

    }

    public void deselectAll() {
        homeLayout.setBackgroundColor(Color.parseColor("#000000"));
        myParadeLayout.setBackgroundColor(Color.parseColor("#000000"));
        invitesLayout.setBackgroundColor(Color.parseColor("#000000"));
        browseLayout.setBackgroundColor(Color.parseColor("#000000"));
        contactLayout.setBackgroundColor(Color.parseColor("#000000"));


    }
    public void otherSelected() {

        inboxBadgeTextView.setBackgroundResource(R.drawable.bg_circle);
        ParadeBadgeTextView.setBackgroundResource(R.drawable.bg_circle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            inboxBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhiteText));
            ParadeBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhiteText));
        } else {
            inboxBadgeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
            ParadeBadgeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
        }

    }



  /*  public void chatSelected(){
        homeLayout.setBackgroundColor(Color.parseColor("#000000"));
        myParadeLayout.setBackgroundColor(Color.parseColor("#000000"));
        invitesLayout.setBackgroundColor(Color.parseColor("#000000"));
        browseLayout.setBackgroundColor(Color.parseColor("#000000"));
        contactLayout.setBackgroundColor(Color.parseColor("#000000"));
    }*/


    @Override
    public void sendText(String text) {
        // Get Fragment B
        //     ContactsFragment frag = (ContactsFragment)adapter.getItem(4);
        //     frag.updateList();

        Log.i("SendText", "Called");

//        RecyclerView recyclerView = (RecyclerView) viewPager.findViewWithTag("GROUP_RECYCLER_VIEW");

//        if (recyclerView != null) {
//            Log.i("RecyclerView", "Not NULL");
//            recyclerView.getAdapter().notifyDataSetChanged();
//        } else {
//            Log.i("RecyclerView", "NULL");
//        }

        Log.i("SendText", "end");


    }


    protected void onResume() {

//        sample for fb
        AppEventsLogger.activateApp(this);


        super.onResume();
        ((AppController) getApplicationContext()).setCurrentActivity(this);
        int inboxCount = 0;
        User user = Utils.getUserFromPreference(FashionHomeActivity.this);
        DatabaseHandler databaseHandler = new DatabaseHandler(FashionHomeActivity.this);
        String count = databaseHandler.getTotalReadStatus(user.id);

        Log.i("ChatCount", count);
        mSharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        if (count.equals("") || count.equals("0"))
            inboxCount = mSharedPreferences.getInt("InvitesCount", 0);
        else
            inboxCount = Integer.parseInt(count) + mSharedPreferences.getInt("InvitesCount", 0);

        Log.i("InviteCount", mSharedPreferences.getInt("InvitesCount", 0) + "");


        count = String.valueOf(inboxCount);

        if (!count.equals("") && !count.equals("0")) {
            Log.i("Count", "not empty");

            inboxBadgeTextView.setVisibility(View.VISIBLE);
            inboxBadgeTextView.setText(count);

        } else {

            Log.i("Count", "empty");
            inboxBadgeTextView.setVisibility(View.GONE);

        }

    }

    protected void onPause() {
        clearReferences();

        AppEventsLogger.deactivateApp(this);

        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();

    }

    private void clearReferences() {
        Activity currActivity = ((AppController) getApplicationContext()).getCurrentActivity();
        if (this.equals(currActivity))
            ((AppController) getApplicationContext()).setCurrentActivity(null);
    }


    public void updateData() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                User user = Utils.getUserFromPreference(FashionHomeActivity.this);
                DatabaseHandler databaseHandler = new DatabaseHandler(FashionHomeActivity.this);
                String count = databaseHandler.getTotalReadStatus(user.id);
                int inboxCount;
                Log.i("Count", count);

                mSharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
                if (count.equals("") || count.equals("0"))
                    inboxCount = mSharedPreferences.getInt("InvitesCount", 0);
                else
                    inboxCount = Integer.parseInt(count) + mSharedPreferences.getInt("InvitesCount", 0);

                Log.i("InviteCount", mSharedPreferences.getInt("InvitesCount", 0) + "");


                count = String.valueOf(inboxCount);

                if (!count.equals("") && !count.equals("0")) {
                    Log.i("Count", "not empty");

                    inboxBadgeTextView.setVisibility(View.VISIBLE);
                    inboxBadgeTextView.setText(count);

                } else {

                    Log.i("Count", "empty");
                    inboxBadgeTextView.setVisibility(View.GONE);

                }


            }
        });


    }

    public void updateParadeData() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int myParadeCount;
                String count="";

                mSharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);

                myParadeCount = mSharedPreferences.getInt("WinnerCount", 0) + mSharedPreferences.getInt("VoteCount", 0);

                Log.i("inboxCount", myParadeCount + "");


                count = String.valueOf(myParadeCount);

                if (!count.equals("") && !count.equals("0")) {
                    Log.i("Count", "not empty");

                    ParadeBadgeTextView.setVisibility(View.VISIBLE);
                    ParadeBadgeTextView.setText(count);

                } else {

                    Log.i("Count", "empty");
                    ParadeBadgeTextView.setVisibility(View.GONE);

                }


            }
        });


    }

    //Permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @TargetApi(Build.VERSION_CODES.M)
    private void askPermission() {

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            Log.i("Cam Permission",hasCameraPermission+"");
            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);

            }

            int hasContactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            Log.i("Contacts Permission",hasContactPermission+"");
            if (hasContactPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }else{

            }


            int hasStorPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.i("rd Str Permission",hasStorPermission+"");
            if (hasStorPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);

            }

            int hasWriteStorPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.i("wr Str Permission",hasStorPermission+"");
            if (hasWriteStorPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);

            }


            int hasMicPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasMicPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);

            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted

                    Log.i("CAMERA PERMISSION", "Granted");


                } else {
                    // Permission Denied
                  /*  Toast.makeText(FashionHomeActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT)
                            .show();*/
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed()
    {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }

    }


}
