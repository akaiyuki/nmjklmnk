package com.fasionparade.fasionparadeApp.Models.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;

public class FashionCongratulations extends AppCompatActivity {

    TextView getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion_congratulations);
        getStarted=(TextView)findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FashionCongratulations.this, HelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
