package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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
import com.fasionparade.fasionparadeApp.Models.Adapters.WinnerGridAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParadeResultActivity extends AppCompatActivity implements
        BaseSliderView.OnSliderClickListener{

    private SliderLayout slider;
    ImageView leftArrow, rightArrow, badgeImageView;
    ImageView facebookIcon, instagramIcon, favIcon, reportAbuseIcon, infoIcon;
    TextView backTextView, closeTextView, timeTextView;
    ArrayList<String> productList, productIdList, voteList;
    RecyclerView gridTypeRecyclerView;
    LinearLayoutManager gridLayoutManager;
    ArrayList<ActiveParade> dataList;
    String index;
    boolean dialogShown;
    SliderLayout dialogSlider;
    int clicked_slider_pos;
    public static int vote_count;
    List<String> rankList;

    Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

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
            new AlertDialog.Builder(ParadeResultActivity.this).setTitle(title).setMessage(alertMessage)
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

        setContentView(R.layout.activity_parade_result);

        context = this;
        vote_count = 0;
        rankList = new ArrayList<>();
        getRankStatus();

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
                new AlertDialog.Builder(ParadeResultActivity.this).setTitle(R.string.cancelled)
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

        timeTextView = (TextView) findViewById(R.id.timeTextView);

        String jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        String jsonArray = getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>() {
        }.getType();
        dataList = gson.fromJson(jsonData, type);


        try {
            timeTextView.setText(dataList.get(Integer.parseInt(index)).startTime);
            JSONArray imageArray = new JSONArray(jsonArray);

            productList = new ArrayList<>();
            productIdList = new ArrayList<>();
            voteList = new ArrayList<>();
            JSONObject imageObject;
            String image;
            for (int i = 0; i < imageArray.length(); i++) {
                imageObject = (JSONObject) imageArray.get(i);


                image = imageObject.getString("fileName");

                productList.add(image);
                productIdList.add(imageObject.getString("imageId"));
                if (!imageObject.getString("votes").equals("0"))
                    vote_count++;

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        GridLayoutManager gridLayouManager = new GridLayoutManager(ParadeResultActivity.this, 4);

        gridLayouManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                if (position == 0) {
                    return 4;
                } else {
                    return 1;
                }
            }


        });

        slider = (SliderLayout) findViewById(R.id.slider);
        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        badgeImageView = (ImageView) findViewById(R.id.badgeImageView);
        backTextView = (TextView) findViewById(R.id.backTextView);
        closeTextView = (TextView) findViewById(R.id.closeTextview);

        gridTypeRecyclerView = (RecyclerView) findViewById(R.id.gridTypeRecyclerView);

        facebookIcon = (ImageView) findViewById(R.id.facebookIcon);
        instagramIcon = (ImageView) findViewById(R.id.instagramIcon);
        favIcon = (ImageView) findViewById(R.id.favIcon);
        reportAbuseIcon = (ImageView) findViewById(R.id.reportAbuseIcon);
        infoIcon = (ImageView) findViewById(R.id.infoIcon);

        // initialize a SliderLayout
        for (int i = 0; i < productList.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.image(productList.get(i)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(ParadeResultActivity.this);
            slider.addSlider(textSliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.stopAutoCycle();


        slider.setCurrentPosition(getIntent().getIntExtra("position", 0));

        if (vote_count == 0) {
            badgeImageView.setVisibility(View.GONE);
        } else {
            badgeImageView.setVisibility(View.VISIBLE);
            badgeImageView.setImageResource(R.drawable.winnerbadge);
        }

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = slider.getCurrentPosition();

                if (pos > 0) {

                    slider.setCurrentPosition(pos - 1);

                    setBadgeImage(pos - 1);

                }

            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = slider.getCurrentPosition();

                if (pos < productList.size() - 1) {

                    slider.setCurrentPosition(pos + 1);

                    setBadgeImage(pos + 1);
                }

            }
        });

        slider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

                setBadgeImage(slider.getCurrentPosition());

            }
        });
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                instagramShare();
            }
        });

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFavouriteRequest();
            }
        });

        reportAbuseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParadeResultActivity.this, InformationActivity.class);
                intent.putExtra("productId", productIdList.get(slider.getCurrentPosition()));
                intent.putExtra("paradeId", dataList.get(Integer.parseInt(index)).paradeId);
                intent.putExtra("userId", dataList.get(Integer.parseInt(index)).userId);
                startActivity(intent);
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowParadeDetail();
            }
        });
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        gridTypeRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true));

        //winner padare grid view
        gridLayoutManager = new GridLayoutManager(ParadeResultActivity.this, 3);
        gridTypeRecyclerView.setLayoutManager(gridLayoutManager);

        WinnerGridAdapter winnerGridAdapter = new WinnerGridAdapter(ParadeResultActivity.this, productList, rankList);
        gridTypeRecyclerView.setAdapter(winnerGridAdapter);
        winnerGridAdapter.SetOnItemClickListener(new WinnerGridAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

                slider.setCurrentPosition(position);

                setBadgeImage(position);
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

        clicked_slider_pos = slider.getCurrentPosition();
        dialogShowImageDetail();
    }

    private void makeFavouriteRequest() {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.favouriteParade();
            if (!dialogShown)
                url = url + "userId=" + user.id + "&imageId=" + productIdList.get(slider.getCurrentPosition());
            else
                url = url + "userId=" + user.id + "&imageId=" + productIdList.get(dialogSlider.getCurrentPosition());
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
                Toast.makeText(ParadeResultActivity.this, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ParadeResultActivity.this,
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
        if (!dialogShown) {
            linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("MYFASHIONPARADE").setContentDescription(dataList.get(Integer.parseInt(index)).aboutParade)
                    .setContentUrl(Uri.parse(productList.get(slider.getCurrentPosition()))).build();
        } else {
            linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("MYFASHIONPARADE").setContentDescription(dataList.get(Integer.parseInt(index)).aboutParade)
                    .setContentUrl(Uri.parse(productList.get(dialogSlider.getCurrentPosition()))).build();
        }

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
    public void instagramShare() {

        URL url;
        if (!dialogShown)
            url = ConvertToUrl((productList.get(slider.getCurrentPosition())).toString());
        else
            url = ConvertToUrl((productList.get(dialogSlider.getCurrentPosition())).toString());

        Bitmap imagebitmap = null;
        try {
            imagebitmap = BitmapFactory.decodeStream(url.openConnection()
                    .getInputStream());

            Intent intent = getPackageManager()
                    .getLaunchIntentForPackage("com.instagram.android");
            if (intent != null) {
                Intent shareIntent = new Intent(
                        Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM,
                        getImageUri(ParadeResultActivity.this, imagebitmap));

                shareIntent.setPackage("com.instagram.android");
                startActivity(shareIntent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id="
                        + "com.instagram.android"));
                startActivity(intent);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

    public void dialogShowParadeDetail()

    {
        final Dialog dialogParadeDetail = new Dialog(this, R.style.custom_dialog_theme);
        dialogParadeDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogParadeDetail.setContentView(R.layout.layout_popup_inbox);

        LinearLayout paradeInfoLayout = (LinearLayout) dialogParadeDetail.findViewById(R.id.paradeInfoLayout);
        TextView aboutParadeTextView = (TextView) dialogParadeDetail.findViewById(R.id.aboutParadeTextView);
        TextView tagTextView = (TextView) dialogParadeDetail.findViewById(R.id.okTextView);
        TextView closeTextView = (TextView) dialogParadeDetail.findViewById(R.id.exitTextView);

        paradeInfoLayout.setVisibility(View.VISIBLE);
        tagTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            closeTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighlighted));
            closeTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
        } else {
            closeTextView.setBackgroundColor(getResources().getColor(R.color.colorHighlighted));
            closeTextView.setTextColor(getResources().getColor(R.color.colorWhiteText));
        }


        closeTextView.setText("Close");

        aboutParadeTextView.setText(dataList.get(Integer.parseInt(index)).aboutParade);
        if (!dataList.get(Integer.parseInt(index)).tag.equals(""))
            tagTextView.setText("Tags: " + dataList.get(Integer.parseInt(index)).tag);
        else
            tagTextView.setVisibility(View.GONE);

        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogParadeDetail.dismiss();
            }
        });


        dialogParadeDetail.show();
    }

    public void dialogShowImageDetail()

    {
        final Dialog dialogImageDetail = new Dialog(this, android.R.style.Theme_Holo_NoActionBar);
        dialogImageDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogImageDetail.setContentView(R.layout.layout_dialog_paradeimage);

        dialogShown = true;

        dialogSlider = (SliderLayout) dialogImageDetail.findViewById(R.id.slider);
        ImageView leftArrow = (ImageView) dialogImageDetail.findViewById(R.id.leftArrow);
        ImageView rightArrow = (ImageView) dialogImageDetail.findViewById(R.id.rightArrow);
        TextView closeTextview = (TextView) dialogImageDetail.findViewById(R.id.closeTextview);

        ImageView facebookIcon = (ImageView) dialogImageDetail.findViewById(R.id.facebookIcon);
        ImageView instagramIcon = (ImageView) dialogImageDetail.findViewById(R.id.instagramIcon);
        ImageView favIcon = (ImageView) dialogImageDetail.findViewById(R.id.favIcon);
        ImageView reportAbuseIcon = (ImageView) dialogImageDetail.findViewById(R.id.reportAbuseIcon);
        ImageView infoIcon = (ImageView) dialogImageDetail.findViewById(R.id.infoIcon);

        // initialize a SliderLayout
        for (int i = 0; i < productList.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.image(productList.get(i)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(ParadeResultActivity.this);
            dialogSlider.addSlider(textSliderView);
        }

        dialogSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        dialogSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        dialogSlider.stopAutoCycle();

        dialogSlider.setCurrentPosition(clicked_slider_pos);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = dialogSlider.getCurrentPosition();

                if (pos > 0) {

                    dialogSlider.setCurrentPosition(pos - 1);

                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = dialogSlider.getCurrentPosition();

                if (pos < productList.size() - 1) {

                    dialogSlider.setCurrentPosition(pos + 1);
                }

            }
        });

        closeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShown = false;
                dialogImageDetail.dismiss();
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
                instagramShare();
            }
        });

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFavouriteRequest();
            }
        });

        reportAbuseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParadeResultActivity.this, InformationActivity.class);
                intent.putExtra("productId", productIdList.get(dialogSlider.getCurrentPosition()));
                intent.putExtra("paradeId", dataList.get(Integer.parseInt(index)).paradeId);
                intent.putExtra("userId", dataList.get(Integer.parseInt(index)).userId);
                startActivity(intent);
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowParadeDetail();
            }
        });
        dialogImageDetail.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogShown = false;
            }
        });
        dialogImageDetail.show();
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



