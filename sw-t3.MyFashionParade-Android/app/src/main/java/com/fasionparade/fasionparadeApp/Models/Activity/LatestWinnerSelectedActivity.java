package com.fasionparade.fasionparadeApp.Models.Activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;

public class LatestWinnerSelectedActivity extends AppCompatActivity {

    ImageView backImageView;

    TextView backTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_winner_selected);

        backImageView=(ImageView)findViewById(R.id.backImageView);
        backTextView=(TextView)findViewById(R.id.backTextView);

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
}
