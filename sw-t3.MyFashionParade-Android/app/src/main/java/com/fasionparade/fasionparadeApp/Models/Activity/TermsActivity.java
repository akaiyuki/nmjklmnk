package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;

import org.json.JSONObject;


public class TermsActivity extends ActionBarActivity {
    WebView webView;
    private TextView back;
    Context context;
    ConnectionCheck connectionCheck;
    String encrypted="";
    AlertDialogManager alert = new AlertDialogManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        context=this;
        webView =(WebView)findViewById(R.id.webview_privacy);
        back=(TextView)findViewById(R.id.backButton);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //   finish();
            }
        });


        getPrivacyDetails();
    }


    public void getPrivacyDetails()
    {

        connectionCheck = new ConnectionCheck(context);
        if (!connectionCheck.isConnectingToInternet())
        {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {


            String url = ResourceManager.Privacyterms();
            Log.i("Terms Response", url);
            new Terms().execute(url);
        }
    }
    private class Terms extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            String result = WebserviceAssessor.getData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                String decryption_data;


                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {
                    encrypted = jsonObject.getString("terms");
                    decryption_data = java.net.URLDecoder.decode(encrypted, "UTF-8");




                    webView.loadData(decryption_data, "text/html", null);
                }else
                {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pDialog.cancel();

        }

    }
}
