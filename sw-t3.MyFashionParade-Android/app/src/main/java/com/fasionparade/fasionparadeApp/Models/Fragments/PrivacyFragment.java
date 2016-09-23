package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import org.json.JSONObject;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PrivacyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static Fragment myFrag;
    int position;
    private TextView back;
    Context context;
    ConnectionCheck cd;
    String encrypted="",decrypted="";
    AlertDialogManager alert = new AlertDialogManager();

    WebView webView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivacyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivacyFragment newInstance(String param1, String param2) {
        PrivacyFragment fragment = new PrivacyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        myFrag=fragment;
        return fragment;
    }

    public PrivacyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);


        context =getActivity();


        webView =(WebView)view.findViewById(R.id.webview_privacy);
        back=(TextView)view.findViewById(R.id.backButton);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        getPrivacyDetails();
        return view;
    }





    public void getPrivacyDetails()
    {

        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet())
        {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {


            String url = ResourceManager.Privacyterms();
            Log.i("Privacy Response", url);
            new Privacy().execute(url);
        }
    }

    private class Privacy extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
                String encryption_data;

                //Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[64]);

                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {
                    encrypted = jsonObject.getString("privacyPolicy");
                    encryption_data = java.net.URLDecoder.decode(encrypted, "UTF-8");



                    webView.loadData(encryption_data,"text/html",null);

//                    decrypted = encryption.decryptOrNull(encrypted);
//
//
//                    Toast.makeText(getActivity()," Decryption "+ decrypted, Toast.LENGTH_SHORT).show();
//
//                    privacytxt_view.setText(decrypted);


                }else
                {
                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pDialog.cancel();

        }

    }






    @Override
    public void onDetach() {
        super.onDetach();

    }


}
