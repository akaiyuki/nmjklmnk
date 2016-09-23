package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Core.MSharedPreferences;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.fasionparade.fasionparadeApp.R;

import org.json.JSONObject;


public class UpgradeActivity extends AppCompatActivity {
     TextView upgradeTextView, stayTextView,promoTextView;
    ConnectionCheck connectionCheck;

    AlertDialogManager alert = new AlertDialogManager();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        sharedPreferences = getApplicationContext().getSharedPreferences(Flag.USER_DETAILS,
                Context.MODE_PRIVATE);

        upgradeTextView = (TextView) findViewById(R.id.upgradeTextView);
        stayTextView = (TextView) findViewById(R.id.stayTextView);
        promoTextView = (TextView) findViewById(R.id.promoTextView);

        upgradeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeUpdatePremiumRequest("1");
            }
        });
        stayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }
    private void makeUpdatePremiumRequest(String userType) {
        connectionCheck = new ConnectionCheck(this);
        User user = Utils.getUserFromPreference(this);
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.usertypeupdate();
            url = url + "userId=" + user.id + "&userType=" +userType;
            new UserTypeUpdate().execute(url);
        }
    }

    private class UserTypeUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpgradeActivity.this);
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
                User user = JsonParser.getUser(jsonObject);
                if (user.message.equals("success")) {
                    String loginUser = Utils.getGson().toJson(user,
                            User.class);
                    editor = sharedPreferences.edit();
                    editor.putString(Flag.USER_DATA,loginUser);
                    editor.commit();
//                    MSharedPreferences.setSomeStringValue(AppController.getInstance(),Flag.USER_DATA,loginUser);
                }
                FashionHomeActivity.userType = user.userType;
                Toast.makeText(UpgradeActivity.this, message, Toast.LENGTH_SHORT).show();
                NewParadeActivity.newParadeActivity.finish();
                finish();
                Intent intent = new Intent(UpgradeActivity.this,NewParadeActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UpgradeActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }

}


