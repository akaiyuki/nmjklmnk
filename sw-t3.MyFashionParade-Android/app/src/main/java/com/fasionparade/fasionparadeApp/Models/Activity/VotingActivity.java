package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.fasionparade.fasionparadeApp.Models.Adapters.HorizontalScrollListAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.PublicParade;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotingActivity extends AppCompatActivity implements
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    ImageView leftArrow, rightArrow, backImageView;
    CircleImageView circleImage;
    HashMap<String, String> file_maps;

    RecyclerView horizonralScrollRecyclerView;
    // android.support.v17.leanback.widget.HorizontalGridView hg;
    TextView exitParadeTextView, startParadeTextView,followeName;
    ArrayList<String> ImagePath;
    ArrayList<String> ImageId,ParadeId;
    public String userId;
    public String paradeId;
    public String paradeName;
    public String sharedWith;
    public String duration;
    public String aboutParade;
    public String tag;
    public String startTime;
    public String endTime;
    public String groupId;
    RelativeLayout followLayout;
    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    Context context;
    User user;
    ScrollView mainScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        context=this;
        Bundle bundle = getIntent().getExtras();
        mainScrollView = (ScrollView)findViewById(R.id.groupsScrollView);
        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        circleImage=(CircleImageView) findViewById(R.id.circleImage);
        exitParadeTextView = (TextView) findViewById(R.id.exitParadeTextView);
        startParadeTextView = (TextView) findViewById(R.id.startParadeTextView);
        followeName=(TextView) findViewById(R.id.followeName);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        followLayout=(RelativeLayout) findViewById(R.id.followLayout);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        user = Utils.getUserFromPreference(context);
        horizonralScrollRecyclerView = (RecyclerView) findViewById(R.id.horizonralScrollRecyclerView);
        if (bundle != null) {
            try {
                userId=bundle.getString("userId");
                paradeId=bundle.getString("paradeId");
                paradeName=bundle.getString("paradeName");
                sharedWith=bundle.getString("sharedWith");
                duration=bundle.getString("duration");
                aboutParade=bundle.getString("aboutParade");
                tag=bundle.getString("tag");
                startTime=bundle.getString("startTime");
                endTime=bundle.getString("endTime");
                groupId=bundle.getString("groupId");
                JSONArray imageArray = new JSONArray(bundle.getString("imagePathJson"));
                JSONObject jObject;
                file_maps = new HashMap<String, String>();
                ImagePath = new ArrayList<String>();
                ParadeId=new ArrayList<String>();
                ImageId=new ArrayList<String>();
                for (int j = 0; j < imageArray.length(); j++) {
                    jObject = imageArray.getJSONObject(j);
                    ImagePath.add(jObject.getString("fileName"));
                    ImageId.add(jObject.getString("imageId"));
                    ParadeId.add(jObject.getString("paradeId"));
                  //  file_maps.put("", jObject.getString("fileName"));
                    DefaultSliderView textSliderView = new DefaultSliderView(this);
                    // initialize a SliderLayout
                    textSliderView

                            .image(jObject.getString("fileName"))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);


                    mDemoSlider.addSlider(textSliderView);
                }
            } catch (Exception e) {

            }
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.stopAutoCycle();
      //  mDemoSlider.setCustomAnimation(new DescriptionAnimation());
       // mDemoSlider.setDuration(4000);


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mDemoSlider.getCurrentPosition();

                if (pos > 0) {

                    mDemoSlider.setCurrentPosition(pos - 1);

                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = mDemoSlider.getCurrentPosition();

                if (pos < ImagePath.size() - 1) {

                    mDemoSlider.setCurrentPosition(pos + 1);
                }

            }
        });

        exitParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        startParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(ImageId.get(mDemoSlider.getCurrentPosition()));
                System.out.println(ParadeId.get(mDemoSlider.getCurrentPosition()));
                voteParade(ImageId.get(mDemoSlider.getCurrentPosition()),ParadeId.get(mDemoSlider.getCurrentPosition()));
            }
        });
        followLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followingParade();
            }
        });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizonralScrollRecyclerView.setLayoutManager(layoutManager);

        HorizontalScrollListAdapter horizontalScrollListAdapter = new HorizontalScrollListAdapter(VotingActivity.this, ImagePath);
        horizonralScrollRecyclerView.setAdapter(horizontalScrollListAdapter);

        int spacing = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.spacing);
        VotingItemDecoration votingHorzGirdSpacingItemDecoration = new VotingItemDecoration(1, spacing, false, ImagePath.size());
        horizonralScrollRecyclerView.addItemDecoration(votingHorzGirdSpacingItemDecoration);


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        horizontalScrollListAdapter.SetOnItemClickListener(new HorizontalScrollListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                mDemoSlider.setCurrentPosition(position);
            }
        });



        mainScrollView.setFocusableInTouchMode(true);
        mainScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        getProfile();
    }

    private void voteParade(String imageId, String paradeId) {
        cd = new ConnectionCheck(context);

        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.voteParade();
            url = url + "userId=" + user.id+"&imageId="+ imageId +"&paradeId="+ paradeId ;

            new voteParadeService().execute(url);

        }
    }

    private void getProfile() {
        cd = new ConnectionCheck(context);

        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.getMyProfile();
            url = url + "userId=" + userId;
            System.out.println("url--->"+url);
            new getProfile().execute(url);

        }
    }

    private void followingParade() {
        cd = new ConnectionCheck(context);

        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.getfollow();
            url = url + "userId=" + user.id +"&followingId=" +userId;
           new getFollowUser().execute(url);
        }
    }
    private class voteParadeService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
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

                    Toast.makeText(context,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(context,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(context,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }
    private class getFollowUser extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
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

                    Toast.makeText(context,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(context,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(context,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }
    private class getProfile extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        List<PublicParade> pParade;
        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
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
                    User userFollow = JsonParser.getUser(jsonObject);
                    if (user.message.equals("success")) {
                        followeName.setText(userFollow.contactName);
                        Picasso.with(context)
                                .load(userFollow.profilePic)
                                .placeholder(R.drawable.no_image)
                                .into(circleImage);
                    }

                }else{
                    Toast.makeText(context,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(context,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }
//item decoration for voting activity

    public class VotingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private int totalSize;

        public VotingItemDecoration(int spanCount, int spacing, boolean includeEdge, int totalSize) {
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
}
