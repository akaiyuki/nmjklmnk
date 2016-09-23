//package com.fasionparade.fasionparadeApp.Models.Activity;
//
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
//import com.fasionparade.fasionparadeApp.R;
//import com.fasionparade.fasionparadeApp.Functions.Database.DatabaseHandler;
//import com.fasionparade.fasionparadeApp.Models.Fragments.BrowseFragment;
//import com.fasionparade.fasionparadeApp.Models.Fragments.ContactGroupDetailFragment;
//import com.fasionparade.fasionparadeApp.Models.Fragments.ContactsFragment;
//import com.fasionparade.fasionparadeApp.Models.Fragments.HomeFragment;
//import com.fasionparade.fasionparadeApp.Models.Fragments.InboxFragment;
//import com.fasionparade.fasionparadeApp.Models.Fragments.MyParadesFragment;
//import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
//import com.fasionparade.fasionparadeApp.Functions.Object.User;
//import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
//import android.Manifest;
//
//
//public class FashionHomeActivity extends BaseActivity implements ContactGroupDetailFragment.TextClicked {
//
//    public static ViewPager viewPager;
//
//    RelativeLayout homeLayout, myParadeLayout, invitesLayout, browseLayout, contactLayout;//chatLayout;
//
//    FragmentManager fragmentManager;
//    FragmentTransaction fragmentTransaction;
//    User user;
//    FashionHomeActivity INSTANCE = null;
//
//    Fragment mFragOnDisplay = null;
//
//    TextView inboxBadgeTextView,ParadeBadgeTextView;
//
//    public static boolean group_clicked,newparade_added,_areLecturesLoaded = false,winner_clicked,parade_clicked,repost_clicked;
//    SharedPreferences mSharedPreferences;
//
//    private static final String[] CONTENT = new String[]{"Home", "My Parades", "Inbox", "Public", "Contacts"};
//    public static String userType="";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fashion_home);
//
//        INSTANCE = this;
//
//        homeLayout = (RelativeLayout) findViewById(R.id.homeLayout);
//        myParadeLayout = (RelativeLayout) findViewById(R.id.myParadeLayout);
//        invitesLayout = (RelativeLayout) findViewById(R.id.inboxLayout);
//        browseLayout = (RelativeLayout) findViewById(R.id.browseLayout);
//        contactLayout = (RelativeLayout) findViewById(R.id.contactLayout);
//        //  chatLayout=(RelativeLayout)findViewById(R.id.chatLayout);
//
//        inboxBadgeTextView = (TextView) findViewById(R.id.inboxBadgeTextView);
//        ParadeBadgeTextView = (TextView) findViewById(R.id.ParadeBadgeTextView);
//
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//
//        fragmentManager = getSupportFragmentManager();
//
//
//        setFrameLayout(R.id.mainContentLayout);
//
//
//        FragmentStatePagerAdapter adapter = new FashionHomeAdapter(
//                getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//
//        if(newparade_added)
//        {
//            viewPager.setCurrentItem(1);
//            newparade_added=false;
//        }
//
//        viewPager.addOnPageChangeListener((new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                if (position == 0) {
//                    homeSelected();
//                } else if (position == 1) {
//                    myParadeSelected();
//                } else if (position == 2) {
//                    invitesSelected();
//                } else if (position == 3) {
//                    browseSelected();
//                } else if (position == 4) {
//                    contactSelected();
//                }/* else if (position == 5) {
//                    chatSelected();
//                }*/
//
//                if (mFragOnDisplay != null){
//                    try {
//                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
////                        FragmentTransaction transaction = fragmentManager.beginTransaction();
////                        transaction.replace(R.id.mainContentLayout, mFragOnDisplay);
////
////                        assert mFragOnDisplay != null;
////                        transaction.addToBackStack(mFragOnDisplay.getClass().toString());
////
////                        transaction.commit();
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        }));
//
//        Intent intent = getIntent();
//        if (intent != null) {
//
//            if (intent.hasExtra("FROM")) {
//
//                String from = intent.getStringExtra("FROM");
//
//                if (from.equals("INBOX_CHAT_NOTIFICATION")) {
//                    Log.i("FROM", "INBOX_CHAT_NOTIFICATION");
//
//                    viewPager.setCurrentItem(2);
//                } else if (from.equals("INBOX_INVITE_NOTIFICATION")) {
//                    Log.i("FROM", "INBOX_INVITE_NOTIFICATION");
//
//                    viewPager.setCurrentItem(2);
//                } else if (from.equals("MAIN_PAGE_NOTIFICATION")) {
//
//                    Log.i("FROM", "MAIN_PAGE_NOTIFICATION");
//                    viewPager.setCurrentItem(0);
//                } else if (from.equals("MY_PARADE_NOTIFICATION")) {
//                    Log.i("FROM", "MY_PARADE_NOTIFICATION");
//
//                    viewPager.setCurrentItem(1);
//                } else {
//                    Log.i("FROM", "last else");
//                }
//
//            } else {
//                Log.i("No FROM", "No FROM");
//
//            }
//
//        } else {
//
//            Log.i("Intent ", "NULL");
//        }
//
//
//        homeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //homeSelected();
//                viewPager.setCurrentItem(0);
//
//            }
//        });
//
//        myParadeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // myParadeSelected();
//                viewPager.setCurrentItem(1);
//
//            }
//        });
//
//        invitesLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // invitesSelected();
//                viewPager.setCurrentItem(2);
//
//            }
//        });
//
//        browseLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //  browseSelected();
//                viewPager.setCurrentItem(3);
//            }
//        });
//
//        contactLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //  contactSelected();
//                FashionHomeActivity.group_clicked=false;
//                viewPager.setCurrentItem(4);
//            }
//        });
//
//      /*  chatLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  chatSelected();
//                viewPager.setCurrentItem(5);
//            }
//        });*/
//        user = Utils.getUserFromPreference(this);
//        System.out.println("----->" + user.contactName);
//
//        askPermission();
//    }
//
//
//    class FashionHomeAdapter extends FragmentStatePagerAdapter {
//
//        public FashionHomeAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            System.out.println("CHECK FRAGMENT POSITION---------" + position);
//            Fragment f = new Fragment();
//            switch (position) {
//                case 0:
//                    f = new HomeFragment();
//                    break;
//                case 1:
//                    f = new MyParadesFragment();
//                    break;
//                case 2:
////                    f = InboxFragment.newInstance("", "");
//                    f = new InboxFragment();
//                    break;
//                case 3:
//                    f = new BrowseFragment();
//                    break;
//                case 4:
//                    f = new ContactsFragment();
//                    break;
//                default:
//                    return new HomeFragment();
//
//            }
//
//            mFragOnDisplay = f;
//
//            return f;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return CONTENT[position % CONTENT.length].toString();
//        }
//
//        @Override
//        public int getCount() {
//
//            return 5;
//            //return CONTENT.length;
//        }
//
//    }
//
//
//    public void homeSelected() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//        } else {
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//        }
//
//        otherSelected();
//
//    }
//
//    public void myParadeSelected() {
//        ParadeBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);
//        inboxBadgeTextView.setBackgroundResource(R.drawable.bg_circle);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            ParadeBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlackText));
//            inboxBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhiteText));
//
//        } else {
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            ParadeBadgeTextView.setTextColor(getResources().getColor(R.color.colorBlackText));
//            inboxBadgeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
//        }
//
//    }
//
//    public void invitesSelected() {
//        inboxBadgeTextView.setBackgroundResource(R.drawable.bg_white_circle);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            inboxBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlackText));
//        } else {
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            inboxBadgeTextView.setTextColor(getResources().getColor(R.color.colorBlackText));
//        }
//
//    }
//
//    public void browseSelected() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//        }else {
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//        }
//
//        otherSelected();
//
//    }
//
//    public void contactSelected() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(),R.color.colorHighlighted))));
//        } else {
//            homeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            myParadeLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            invitesLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            browseLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorBackgroundColor))));
//            contactLayout.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.colorHighlighted))));
//        }
//
//        otherSelected();
//
//    }
//
//    public void deselectAll() {
//        homeLayout.setBackgroundColor(Color.parseColor("#000000"));
//        myParadeLayout.setBackgroundColor(Color.parseColor("#000000"));
//        invitesLayout.setBackgroundColor(Color.parseColor("#000000"));
//        browseLayout.setBackgroundColor(Color.parseColor("#000000"));
//        contactLayout.setBackgroundColor(Color.parseColor("#000000"));
//
//
//    }
//    public void otherSelected() {
//
//        inboxBadgeTextView.setBackgroundResource(R.drawable.bg_circle);
//        ParadeBadgeTextView.setBackgroundResource(R.drawable.bg_circle);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            inboxBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhiteText));
//            ParadeBadgeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhiteText));
//        } else {
//            inboxBadgeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
//            ParadeBadgeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
//        }
//
//    }
//
//
//
//  /*  public void chatSelected(){
//        homeLayout.setBackgroundColor(Color.parseColor("#000000"));
//        myParadeLayout.setBackgroundColor(Color.parseColor("#000000"));
//        invitesLayout.setBackgroundColor(Color.parseColor("#000000"));
//        browseLayout.setBackgroundColor(Color.parseColor("#000000"));
//        contactLayout.setBackgroundColor(Color.parseColor("#000000"));
//    }*/
//
//
//    @Override
//    public void sendText(String text) {
//        // Get Fragment B
//        //     ContactsFragment frag = (ContactsFragment)adapter.getItem(4);
//        //     frag.updateList();
//
//        Log.i("SendText", "Called");
//
//        RecyclerView recyclerView = (RecyclerView) viewPager.findViewWithTag("GROUP_RECYCLER_VIEW");
//
//        if (recyclerView != null) {
//            Log.i("RecyclerView", "Not NULL");
//            recyclerView.getAdapter().notifyDataSetChanged();
//        } else {
//            Log.i("RecyclerView", "NULL");
//        }
//
//        Log.i("SendText", "end");
//
//
//    }
//
//
//    protected void onResume() {
//        super.onResume();
//        ((AppController) getApplicationContext()).setCurrentActivity(this);
//        int inboxCount = 0;
//        User user = Utils.getUserFromPreference(FashionHomeActivity.this);
//        DatabaseHandler databaseHandler = new DatabaseHandler(FashionHomeActivity.this);
//        String count = databaseHandler.getTotalReadStatus(user.id);
//
//        Log.i("ChatCount", count);
//        mSharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
//        if (count.equals("") || count.equals("0"))
//            inboxCount = mSharedPreferences.getInt("InvitesCount", 0);
//        else
//            inboxCount = Integer.parseInt(count) + mSharedPreferences.getInt("InvitesCount", 0);
//
//        Log.i("InviteCount", mSharedPreferences.getInt("InvitesCount", 0) + "");
//
//
//        count = String.valueOf(inboxCount);
//
//        if (!count.equals("") && !count.equals("0")) {
//            Log.i("Count", "not empty");
//
//            inboxBadgeTextView.setVisibility(View.VISIBLE);
//            inboxBadgeTextView.setText(count);
//
//        } else {
//
//            Log.i("Count", "empty");
//            inboxBadgeTextView.setVisibility(View.GONE);
//
//        }
//
//    }
//
//    protected void onPause() {
//        clearReferences();
//        super.onPause();
//    }
//
//    protected void onDestroy() {
//        clearReferences();
//        super.onDestroy();
//
//    }
//
//    private void clearReferences() {
//        Activity currActivity = ((AppController) getApplicationContext()).getCurrentActivity();
//        if (this.equals(currActivity))
//            ((AppController) getApplicationContext()).setCurrentActivity(null);
//    }
//
//
//    public void updateData() {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                User user = Utils.getUserFromPreference(FashionHomeActivity.this);
//                DatabaseHandler databaseHandler = new DatabaseHandler(FashionHomeActivity.this);
//                String count = databaseHandler.getTotalReadStatus(user.id);
//                int inboxCount;
//                Log.i("Count", count);
//
//                mSharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
//                if (count.equals("") || count.equals("0"))
//                    inboxCount = mSharedPreferences.getInt("InvitesCount", 0);
//                else
//                    inboxCount = Integer.parseInt(count) + mSharedPreferences.getInt("InvitesCount", 0);
//
//                Log.i("InviteCount", mSharedPreferences.getInt("InvitesCount", 0) + "");
//
//
//                count = String.valueOf(inboxCount);
//
//                if (!count.equals("") && !count.equals("0")) {
//                    Log.i("Count", "not empty");
//
//                    inboxBadgeTextView.setVisibility(View.VISIBLE);
//                    inboxBadgeTextView.setText(count);
//
//                } else {
//
//                    Log.i("Count", "empty");
//                    inboxBadgeTextView.setVisibility(View.GONE);
//
//                }
//
//
//            }
//        });
//
//
//    }
//
//    public void updateParadeData() {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int myParadeCount;
//                String count="";
//
//                mSharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
//
//                myParadeCount = mSharedPreferences.getInt("WinnerCount", 0) + mSharedPreferences.getInt("VoteCount", 0);
//
//                Log.i("inboxCount", myParadeCount + "");
//
//
//                count = String.valueOf(myParadeCount);
//
//                if (!count.equals("") && !count.equals("0")) {
//                    Log.i("Count", "not empty");
//
//                    ParadeBadgeTextView.setVisibility(View.VISIBLE);
//                    ParadeBadgeTextView.setText(count);
//
//                } else {
//
//                    Log.i("Count", "empty");
//                    ParadeBadgeTextView.setVisibility(View.GONE);
//
//                }
//
//
//            }
//        });
//
//
//    }
//
//    //Permission
//    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
//    @TargetApi(Build.VERSION_CODES.M)
//    private void askPermission() {
//
//        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentApiVersion >= android.os.Build.VERSION_CODES.M) {
//
//            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
//            Log.i("Cam Permission",hasCameraPermission+"");
//            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//
//            }
//
//            int hasContactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
//            Log.i("Contacts Permission",hasContactPermission+"");
//            if (hasContactPermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//            }else{
//
//            }
//
//
//            int hasStorPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//            Log.i("rd Str Permission",hasStorPermission+"");
//            if (hasStorPermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//
//            }
//
//            int hasWriteStorPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            Log.i("wr Str Permission",hasStorPermission+"");
//            if (hasWriteStorPermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//
//            }
//
//
//            int hasMicPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
//            if (hasMicPermission != PackageManager.PERMISSION_GRANTED) {
//
//                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//
//            }
//
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_PERMISSIONS:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//
//                    Log.i("CAMERA PERMISSION", "Granted");
//
//
//                } else {
//                    // Permission Denied
//                  /*  Toast.makeText(FashionHomeActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT)
//                            .show();*/
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//}
