package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONObject;


public class InformationActivity extends ActionBarActivity {

    Context context;
    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

    TextView titleTxt,backButton,sendButton;
    TextInputLayout emailTextInputLayout;
    EditText commentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);

        context = this;

        titleTxt =(TextView) findViewById(R.id.TitleTxt);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        commentsLayout = (EditText) findViewById(R.id.choosePasswordTextInputLayout);
        backButton = (TextView) findViewById(R.id.backButton);
        sendButton = (TextView) findViewById(R.id.sendButton);

        titleTxt.setText("Report Abuse");
        emailTextInputLayout.setVisibility(View.GONE);
        commentsLayout.setHint("Comments");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            commentsLayout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        } else {
            commentsLayout.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentsLayout.getText().toString().equals(""))
                    makeReportAbuseRequest(commentsLayout.getText().toString());
                else
                    Toast.makeText(InformationActivity.this, "Please enter your comments", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void makeReportAbuseRequest(String comments) {
        mConnectionCheck = new ConnectionCheck(context);
        User user = Utils.getUserFromPreference(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String  url = ResourceManager.ReportAbuse();
            url = url + "userId=" + user.id + "&paradeId=" +getIntent().getStringExtra("paradeId") +"&imageId="+ getIntent().getStringExtra("productId")+"&description="+comments;
            new reportAbuse().execute(url);
        }
    }

    private class reportAbuse extends AsyncTask<String, Void, String> {
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
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if(errorCode.equals("200")) {
                    Toast.makeText(InformationActivity.this, message, Toast.LENGTH_SHORT).show();
                    commentsLayout.getText().clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(InformationActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

}

