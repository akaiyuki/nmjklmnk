package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.facebook.FacebookSdk;

import org.json.JSONObject;


public class DeleteAccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView back,delete_user;

    private String user_emailId="",user_password="";
    private EditText user_emailId_editText,user_password_editText;


    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();
    User user;
    Context context;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteAccountFragment newInstance(String param1, String param2) {
        DeleteAccountFragment fragment = new DeleteAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DeleteAccountFragment() {
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
        context = getActivity();
        FacebookSdk.sdkInitialize(context);
        View view =inflater.inflate(R.layout.fragment_delete_account, container, false);



        user = Utils.getUserFromPreference(context);

        back=(TextView)view.findViewById(R.id.backButton_delete_setting);
        delete_user=(TextView)view.findViewById(R.id.loginButton_deleteuser);
        user_emailId_editText =(EditText)view.findViewById(R.id.userName_deleteuser);
        user_password_editText=(EditText)view.findViewById(R.id.password_deleteuser);


        user_emailId = user.mail;
        user_password =user.password;


        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if(user_emailId_editText.getText().toString().isEmpty() || user_password_editText.getText().toString().isEmpty())
                {

                    Log.i("user","Field is Empty");


                }
                else if(user_emailId_editText.getText().toString().equalsIgnoreCase(user_emailId) || user_password_editText.getText().toString().equalsIgnoreCase(user_password))
                {


                Log.i("user","Vaild");

                }
                else
                {

                    Log.i("user","Invaild");


                }
            }
        });






        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();

            }
        });









//        deleteUseraccount();


        return  view;
    }



    public void deleteUserAccount()
    {

        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.DeleteUser();
            user = Utils.getUserFromPreference(context);
            String newURL = url + "userId=" + user.id;
            Log.i("UnFollows", newURL);
            new DeleteUserDetails().execute(newURL);

        }














    }


    private class DeleteUserDetails extends AsyncTask<String, Void, String>
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
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
