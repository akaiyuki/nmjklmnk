package com.fasionparade.fasionparadeApp.Models.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;

public class NotificationCongratulationActivity extends AppCompatActivity {

    TextView okTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_congratulation);
        okTextView=(TextView)findViewById(R.id.okTextView);


        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FashionHomeActivity.newparade_added =true;
                Intent intent = new Intent(NotificationCongratulationActivity.this, FashionHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
