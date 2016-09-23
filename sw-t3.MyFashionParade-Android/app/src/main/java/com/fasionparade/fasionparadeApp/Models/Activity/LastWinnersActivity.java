package com.fasionparade.fasionparadeApp.Models.Activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.fasionparade.fasionparadeApp.Models.Adapters.HorizontalScrollListAdapter;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.ActiveParade;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LastWinnersActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener

{

    private SliderLayout slider;
    ImageView leftArrow, rightArrow, backImageView;
    TextView backTextView,dateTextView;
    ArrayList<String> productList;
    ArrayList<String> imageList;

    String jsonData, index, jsonArray;
    List<ActiveParade> dataList;
    RecyclerView horizontalScrollRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)


    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastwinners_activity);
        horizontalScrollRecyclerView = (RecyclerView) findViewById(R.id.horizonralScrollRecyclerView);

        slider = (SliderLayout) findViewById(R.id.slider);
        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        backTextView=(TextView)findViewById(R.id.backTextView);
        dateTextView=(TextView)findViewById(R.id.date_time_view);


        jsonData = getIntent().getStringExtra("data");
        index = getIntent().getStringExtra("index");
        jsonArray = getIntent().getStringExtra("jsonArray");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ActiveParade>>() {
        }.getType();
        dataList = gson.fromJson(jsonData, type);



        try
        {
            dateTextView.setText(dataList.get(Integer.parseInt(index)).startTime);
            JSONArray imageArray = new JSONArray(jsonArray);
            System.out.println("imageArray" + imageArray.length());
            productList = new ArrayList<>();
            JSONObject imageObject;
            String image;
            for (int i = 0; i < imageArray.length(); i++)
            {
                imageObject = (JSONObject) imageArray.get(i);
                image = imageObject.getString("fileName");
                System.out.println("image" + image);
                productList.add(image);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        // initialize a SliderLayout
        for (int i = 0; i < productList.size(); i++)
        {

            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.image(productList.get(i)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
            slider.addSlider(textSliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.stopAutoCycle();





        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = slider.getCurrentPosition();

                if (pos > 0) {

                    slider.setCurrentPosition(pos - 1);

                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = slider.getCurrentPosition();

                if (pos < productList.size() - 1) {

                    slider.setCurrentPosition(pos + 1);
                }

            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalScrollRecyclerView.setLayoutManager(layoutManager);

        HorizontalScrollListAdapter horizontalScrollListAdapter = new HorizontalScrollListAdapter(LastWinnersActivity.this, productList);
        horizontalScrollRecyclerView.setAdapter(horizontalScrollListAdapter);

        int spacing = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.spacing);
        VotingItemDecoration votingHorzGirdSpacingItemDecoration = new VotingItemDecoration(1, spacing, false, productList.size());
        horizontalScrollRecyclerView.addItemDecoration(votingHorzGirdSpacingItemDecoration);


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        horizontalScrollListAdapter.SetOnItemClickListener(new HorizontalScrollListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                slider.setCurrentPosition(position);
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
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }

    public class VotingItemDecoration extends RecyclerView.ItemDecoration
    {

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
