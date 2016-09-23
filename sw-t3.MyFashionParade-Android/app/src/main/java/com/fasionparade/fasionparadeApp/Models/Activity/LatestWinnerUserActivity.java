package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Models.Adapters.ViewPagerAdapter;
import com.fasionparade.fasionparadeApp.Models.Adapters.WinnerGridAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.FileType;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LatestWinnerUserActivity extends ActionBarActivity implements OnPageChangeListener {

    ImageView leftArrow, rightArrow, backImageView, profileImageView, badgeImageView, paradeProfileImage;
    TextView backTextView, DateTextView, paradeProfileName, followTextView;
    ArrayList<String> productList, productIdList, productTypeList, voteList;
    ArrayList<FileType> productListInbox;

    String jsonData, index, jsonArray;
    List<ActiveParade> dataList;
    RecyclerView horizonralScrollRecyclerView;

    protected View view;
    private ViewPager sliderImages;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;


    public static int vote_count;
    List<String> rankList;
    boolean follow_clicked;
    String userId = "";

    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latestwinnerparader_anotheruser);

        vote_count = 0;
        rankList = new ArrayList<>();
        getRankStatus();

        horizonralScrollRecyclerView = (RecyclerView) findViewById(R.id.horizonralScrollRecyclerView_anotheruser);

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        backImageView = (ImageView) findViewById(R.id.backImageView_anotheruser);
        profileImageView = (ImageView) findViewById(R.id.profileImageView_anotheruser);
        backTextView = (TextView) findViewById(R.id.backTextView_anotheruser);
        DateTextView = (TextView) findViewById(R.id.date_time_view_anotheruser);
        paradeProfileName = (TextView) findViewById(R.id.paradeprofileName);
        followTextView = (TextView) findViewById(R.id.followTextView_anotheruser);
        paradeProfileImage = (ImageView) findViewById(R.id.paradeprofileImage_anotheruser);
        badgeImageView = (ImageView) findViewById(R.id.badgeImageView);


        jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        jsonArray = getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>() {
        }.getType();
        dataList = gson.fromJson(jsonData, type);


        try {
            DateTextView.setText(dataList.get(Integer.parseInt(index)).startTime);
            paradeProfileName.setText(getIntent().getStringExtra("userName"));
            userId = dataList.get(Integer.parseInt(index)).userId;

            JSONArray imageArray = new JSONArray(jsonArray);
            System.out.println("imageArray" + imageArray.length());
            productList = new ArrayList<>();
            productIdList = new ArrayList<>();
            productTypeList = new ArrayList<>();
            productListInbox = new ArrayList<>();
            voteList = new ArrayList<>();
            JSONObject imageObject;
            String image, imageid;
            for (int i = 0; i < imageArray.length(); i++) {
                imageObject = (JSONObject) imageArray.get(i);

                image = imageObject.getString("fileName");
                imageid = imageObject.getString("imageId");
                productList.add(image);
                productIdList.add(imageid);
                productTypeList.add(imageObject.getString("fileType"));
                FileType fileType = new FileType();

                //  imageObject = (JSONObject) imageArray.get(i);
                fileType.fileName = imageObject.getString("fileName");
                fileType.type = imageObject.getString("fileType");
                fileType.imageId = imageObject.getString("imageId");
                productListInbox.add(fileType);

                if (!imageObject.getString("votes").equals("0"))
                    vote_count++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (!getIntent().getStringExtra("profilePic").equals("") && getIntent().getStringExtra("profilePic") != null) {
            Picasso.with(LatestWinnerUserActivity.this)
                    .load(getIntent().getStringExtra("profilePic"))
                    .placeholder(R.drawable.actionbar_profileicon)
                    .into(paradeProfileImage);
        }

        if (getIntent().getStringExtra("followingStatus").equals("1")) {
            followTextView.setText("Following");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
            } else {
                followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
            }

            follow_clicked = true;
        } else if(getIntent().getStringExtra("followingStatus").equals("0")) {
            followTextView.setText("+Follow");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
            } else {
                followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
            }

            follow_clicked = false;
        }


        sliderImages = (ViewPager) findViewById(R.id.pager_introduction);


        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        mAdapter = new ViewPagerAdapter(LatestWinnerUserActivity.this, productList, productTypeList);
        sliderImages.setAdapter(mAdapter);
        sliderImages.setOnPageChangeListener(this);
        setUiPageViewController();

        sliderImages.setCurrentItem(getIntent().getIntExtra("position", 0));

        if (vote_count == 0) {
            badgeImageView.setVisibility(View.GONE);
        } else {
            badgeImageView.setVisibility(View.VISIBLE);
            badgeImageView.setImageResource(R.drawable.winnerbadge);
        }


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = sliderImages.getCurrentItem();

                if (pos > 0) {

                    sliderImages.setCurrentItem(pos - 1);
                    setBadgeImage(pos - 1);

                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = sliderImages.getCurrentItem();

                if (pos < productList.size() - 1) {

                    sliderImages.setCurrentItem(pos + 1);
                    setBadgeImage(pos + 1);
                }

            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flag.profilePage = true;
                Intent intent = new Intent(LatestWinnerUserActivity.this, FashionHomeActivity.class);
                startActivity(intent);
                finish();


            }
        });
        followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowUnfollowParadeRequest();
            }
        });


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        horizonralScrollRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true));

        //winner padare grid view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LatestWinnerUserActivity.this, 3);
        horizonralScrollRecyclerView.setLayoutManager(gridLayoutManager);


        WinnerGridAdapter horizontalScrollListAdapter = new WinnerGridAdapter(LatestWinnerUserActivity.this, productList, rankList);
        horizonralScrollRecyclerView.setAdapter(horizontalScrollListAdapter);


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        horizontalScrollListAdapter.SetOnItemClickListener(new WinnerGridAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                sliderImages.setCurrentItem(position);
                setBadgeImage(position);
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        setBadgeImage(sliderImages.getCurrentItem());
    }

    public class VotingHorzGirdSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private int totalSize;

        public VotingHorzGirdSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int totalSize) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
            this.totalSize = totalSize;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {

                if (position == 0) {

                    Log.i("itemdec", "0");

                    outRect.left = spacing;
                    outRect.right = spacing / 4;

                   /* outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing; // item top
                    }*/

                } else {

                    if (position == totalSize - 1) {
                        Log.i("itemdec", "last position");
                        outRect.left = spacing / 4;
                        outRect.right = spacing;


                    } else {
                        Log.i("itemdec", "else");

                        outRect.left = spacing / 4; // column * ((1f / spanCount) * spacing)
                        outRect.right = spacing / 4; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    }

                }

               /* outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }*/
            }
        }
    }

    private void FollowUnfollowParadeRequest() {
        cd = new ConnectionCheck(LatestWinnerUserActivity.this);
        User user = Utils.getUserFromPreference(LatestWinnerUserActivity.this);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(LatestWinnerUserActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = "";
            if (!follow_clicked)
                url = ResourceManager.Followmtpuser();
            else
                url = ResourceManager.Unfollowmtpuser();

            url = url + "userId=" + user.id + "&followingId=" + userId;
            new followUnfollowParade().execute(url);
        }
    }

    private class followUnfollowParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LatestWinnerUserActivity.this);
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
                    Toast.makeText(LatestWinnerUserActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (!follow_clicked) {
                        followTextView.setText("Following");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFollowBackground));
                        } else {
                            followTextView.setBackgroundColor(getResources().getColor(R.color.colorFollowBackground));
                        }

                        follow_clicked = true;
                    } else {
                        followTextView.setText("+Follow");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            followTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
                        } else {
                            followTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
                        }

                        follow_clicked = false;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LatestWinnerUserActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    public void getRankStatus() {
        if (vote_count > 1) {
            if (vote_count == 2) {
                rankList = new ArrayList<>();
                rankList.add("1");
                rankList.add("2");
                rankList.add("");
                rankList.add("");
                rankList.add("");
                rankList.add("");
            } else if (vote_count == 3) {
                if (voteList.get(1).equals(voteList.get(2))) {
                    rankList = new ArrayList<>();
                    rankList.add("1");
                    rankList.add("2");
                    rankList.add("=2");
                    rankList.add("");
                    rankList.add("");
                    rankList.add("");
                } else {
                    rankList = new ArrayList<>();
                    rankList.add("1");
                    rankList.add("2");
                    rankList.add("3");
                    rankList.add("");
                    rankList.add("");
                    rankList.add("");
                }
            } else if (vote_count == 4) {
                if (voteList.get(1).equals(voteList.get(2))) {
                    rankList = new ArrayList<>();
                    rankList.add("1");
                    rankList.add("2");
                    rankList.add("=2");
                    rankList.add("3");
                    rankList.add("");
                    rankList.add("");
                } else if (voteList.get(2).equals(voteList.get(3))) {
                    rankList = new ArrayList<>();
                    rankList.add("1");
                    rankList.add("2");
                    rankList.add("3");
                    rankList.add("=3");
                    rankList.add("");
                    rankList.add("");
                } else {
                    rankList = new ArrayList<>();
                    rankList.add("1");
                    rankList.add("2");
                    rankList.add("3");
                    rankList.add("");
                    rankList.add("");
                    rankList.add("");
                }

            } else if (vote_count == 5) {
                if (voteList.get(1).equals(voteList.get(2))) {
                    if (voteList.get(3).equals(voteList.get(4))) {
                        rankList = new ArrayList<>();
                        rankList.add("1");
                        rankList.add("2");
                        rankList.add("=2");
                        rankList.add("3");
                        rankList.add("=3");
                        rankList.add("");
                    } else {
                        rankList = new ArrayList<>();
                        rankList.add("1");
                        rankList.add("2");
                        rankList.add("=2");
                        rankList.add("3");
                        rankList.add("");
                        rankList.add("");
                    }
                } else {
                    if (voteList.get(3).equals(voteList.get(4))) {
                        rankList = new ArrayList<>();
                        rankList.add("1");
                        rankList.add("2");
                        rankList.add("3");
                        rankList.add("=3");
                        rankList.add("");
                        rankList.add("");
                    } else {
                        rankList = new ArrayList<>();
                        rankList.add("1");
                        rankList.add("2");
                        rankList.add("3");
                        rankList.add("");
                        rankList.add("");
                        rankList.add("");
                    }
                }

            }
        } else {
            rankList = new ArrayList<>();
            rankList.add("1");
            rankList.add("");
            rankList.add("");
            rankList.add("");
            rankList.add("");
            rankList.add("");
        }

    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    public void setBadgeImage(int position) {
        if (!rankList.get(position).equals(""))
            badgeImageView.setVisibility(View.VISIBLE);
        else
            badgeImageView.setVisibility(View.GONE);

        if (rankList.get(position).equals("1"))
            badgeImageView.setImageResource(R.drawable.winnerbadge);
        else if (rankList.get(position).equals("2"))
            badgeImageView.setImageResource(R.drawable.winnerbadge_second);
        else if (rankList.get(position).equals("=2"))
            badgeImageView.setImageResource(R.drawable.winnerbadge_second_equal);
        else if (rankList.get(position).equals("3"))
            badgeImageView.setImageResource(R.drawable.winnerbadge_third);
        else if (rankList.get(position).equals("=3"))
            badgeImageView.setImageResource(R.drawable.winnerbadge_third);
    }

}
