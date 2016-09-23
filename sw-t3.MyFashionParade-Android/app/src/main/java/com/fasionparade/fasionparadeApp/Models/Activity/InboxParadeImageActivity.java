package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.fasionparade.fasionparadeApp.Models.Adapters.ViewPagerAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class InboxParadeImageActivity extends ActionBarActivity implements
        ViewPager.OnPageChangeListener {
    ImageView leftArrow, rightArrow;
    ImageView facebookIcon, instagramIcon, favIcon, informationIcon;
    TextView backTextView, voteTextView, closeTextview;

    ArrayList<String> imageList, imageIdList, imageTypeList;

    Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

    protected View view;
    private ViewPager sliderImages;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;


    //facebook
    private boolean canPresentShareDialog;
    private PendingAction pendingAction = PendingAction.NONE;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.example.hellofacebook:PendingAction";
    private CallbackManager callbackManager;
    // private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(InboxParadeImageActivity.this).setTitle(title).setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null).show();
        }
    };

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    //facebook


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        context = this;
        setContentView(R.layout.activity_inbox_parade_image);


        //facebbok
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlePendingAction();
                // updateUI();
            }

            @Override
            public void onCancel() {
                if (pendingAction != PendingAction.NONE) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                // updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                if (pendingAction != PendingAction.NONE && exception instanceof FacebookAuthorizationException) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                // updateUI();
            }

            private void showAlert() {
                new AlertDialog.Builder(InboxParadeImageActivity.this).setTitle(R.string.cancelled)
                        .setMessage(R.string.permission_not_granted).setPositiveButton(R.string.ok, null).show();
            }
        });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        // FACEBOOK

        // slider = (SliderLayout) findViewById(R.id.slider);
        imageList = getIntent().getStringArrayListExtra("products");
        imageIdList = getIntent().getStringArrayListExtra("productsId");
        imageTypeList = getIntent().getStringArrayListExtra("productsType");
        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        backTextView = (TextView) findViewById(R.id.backTextView);
        voteTextView = (TextView) findViewById(R.id.voteTextView);
        closeTextview = (TextView) findViewById(R.id.closeTextview);

        facebookIcon = (ImageView) findViewById(R.id.facebookIcon);
        instagramIcon = (ImageView) findViewById(R.id.instagramIcon);
        favIcon = (ImageView) findViewById(R.id.favIcon);
        informationIcon = (ImageView) findViewById(R.id.informationIcon);

        sliderImages = (ViewPager) findViewById(R.id.pager_introduction);


        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        mAdapter = new ViewPagerAdapter(InboxParadeImageActivity.this, imageList, imageTypeList);
        sliderImages.setAdapter(mAdapter);
        sliderImages.setOnPageChangeListener(this);
        setUiPageViewController();

        sliderImages.setCurrentItem(getIntent().getIntExtra("position", 0));

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = sliderImages.getCurrentItem();

                if (pos > 0) {

                    sliderImages.setCurrentItem(pos - 1);

                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = sliderImages.getCurrentItem();

                if (pos < imageList.size() - 1) {

                    sliderImages.setCurrentItem(pos + 1);
                }

            }
        });
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        closeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        voteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVoteRequest();
            }
        });
        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPostStatusUpdate();
            }
        });

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "image/*";
                String filename = imageList.get(sliderImages.getCurrentItem());
                createInstagramIntent(type, filename);
            }
        });

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFavouriteRequest();
            }
        });

        informationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboxParadeImageActivity.this, InformationActivity.class);
                intent.putExtra("productId", imageIdList.get(sliderImages.getCurrentItem()));
                intent.putExtra("aboutParade", getIntent().getStringExtra("aboutParade"));
                intent.putExtra("paradeId", getIntent().getStringExtra("paradeId"));
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                startActivity(intent);
            }
        });


    }

    private void makeFavouriteRequest() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.favouriteParade();
            url = url + "userId=" + user.id + "&imageId=" + imageIdList.get(sliderImages.getCurrentItem());
            new favouriteParade().execute(url);
        }
    }

    private void makeVoteRequest() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.voteParade();
            url = url + "userId=" + user.id + "&imageId=" + imageIdList.get(sliderImages.getCurrentItem()) + "&paradeId=" + getIntent().getStringExtra("paradeId");
            new favouriteParade().execute(url);
        }
    }

    private class favouriteParade extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

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
                String message;
                message = jsonObject.getString("message");
                Toast.makeText(InboxParadeImageActivity.this, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(InboxParadeImageActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

    //Facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but
        // we assume they
        // will succeed.

        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                // postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent;
        linkContent = new ShareLinkContent.Builder()
                .setContentTitle("MYFASHIONPARADE").setContentDescription(getIntent().getStringExtra("aboutParade"))
                .setContentUrl(Uri.parse(imageList.get(sliderImages.getCurrentItem()))).build();

        if (canPresentShareDialog) {
            shareDialog.show(linkContent);

        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        System.out.println("accessToken" + accessToken);
        if (accessToken != null || allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    //facebook

    private void createInstagramIntent(String type, String mediaPath) {

        // Create the new Intent using the 'Send' action.
        PackageManager pm = getPackageManager();
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        Bitmap imagebitmap = null;
        URL url = ConvertToUrl(mediaPath);
        try {
            Intent intent1 = getPackageManager()
                    .getLaunchIntentForPackage("com.instagram.android");
            Intent openInChooser = Intent.createChooser(share, "Share to");

            List<ResolveInfo> resInfo = pm.queryIntentActivities(share, 0);
            List<LabeledIntent> intentList = new ArrayList<>();
            for (int i = 0; i < resInfo.size(); i++) {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                share.setComponent(new ComponentName(packageName, ri.activityInfo.name));

                if (intent1 != null) {

                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        if (packageName.contains("instagram")) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            imagebitmap = BitmapFactory.decodeStream(url.openConnection()
                                    .getInputStream());
                            // Broadcast the Intent.
                            share.putExtra(Intent.EXTRA_STREAM,
                                    getImageUri(InboxParadeImageActivity.this, imagebitmap));
                            intentList.add(new LabeledIntent(share, packageName, ri.loadLabel(pm), ri.icon));
                        }
                    }


                } else {
                    share = new Intent(Intent.ACTION_VIEW);
                    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    share.setData(Uri.parse("market://details?id="
                            + "com.instagram.android"));
                    startActivity(share);
                }


            }

            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents[0]);
            startActivity(openInChooser);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private URL ConvertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    }
}
