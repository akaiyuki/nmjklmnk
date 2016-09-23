package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    private EditText title_editText,description_editText;

    private TextView button_submit,button_back;
    static String title="",description="";


    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    User user;






    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_report, container, false);


        title_editText = (EditText)view.findViewById(R.id.userName_report);
        description_editText =(EditText)view.findViewById(R.id.choosePasswordTextInputLayout);

        button_submit = (TextView)view.findViewById(R.id.loginButton_report);
        button_back =(TextView)view.findViewById(R.id.backButton_report);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                reportProblem();

            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();

            }
        });

        return  view;
    }

    public void reportProblem()
    {

        title = title_editText.getText().toString().trim();
        description = description_editText.getText().toString().trim();

        cd = new ConnectionCheck(getActivity());
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.Reportproblem();
            user = Utils.getUserFromPreference(getActivity());
            String newURL = url + "userId=" + user.id + "&title=" + title + "&description=" + description + "&problemType=1";
            Log.i("Report problem :", newURL);
            new ReportProblemAccess().execute(newURL);
        }
    }


    private class ReportProblemAccess extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute()
        {
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
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    title_editText.setText("");
                    description_editText.setText("");
                    title_editText.requestFocus();

                }
                else
                {
                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
                    title_editText.setText("");
                    description_editText.setText("");
                    title_editText.requestFocus();
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
