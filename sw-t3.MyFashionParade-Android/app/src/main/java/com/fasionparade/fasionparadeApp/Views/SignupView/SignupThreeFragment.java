package com.fasionparade.fasionparadeApp.Views.SignupView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionSignUpActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.LoginActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Fragments.TermsFragment;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;

import org.json.JSONObject;


public class SignupThreeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText choosePasswordTxt, confirmPasswordTxt;
    CheckBox check_agree;
    private TextView backButton, loginButton;
    View mView;
    Context context;
    private String cPassword, cmPassword;
    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupThreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupThreeFragment newInstance(String param1, String param2) {
        SignupThreeFragment fragment = new SignupThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignupThreeFragment() {
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_signup_three, container, false);
        context = container.getContext();
        getUiInitialization();
        return mView;
    }

    public void getUiInitialization() {

        choosePasswordTxt = (EditText) mView.findViewById(R.id.choosePasswordTxt);
        confirmPasswordTxt = (EditText) mView.findViewById(R.id.confirmPasswordTxt);
        check_agree = (CheckBox) mView.findViewById(R.id.check_agree);
        //check_agree.setOnClickListener(this);
        //  phoneTxt = (EditText) mView.findViewById(R.id.phoneTxt);
        backButton = (TextView) mView.findViewById(R.id.backButton);
        loginButton = (TextView) mView.findViewById(R.id.loginButton);
        backButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              FashionSignUpActivity.viewPager.setCurrentItem(1);
                                          }
                                      }
        );
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               cPassword = choosePasswordTxt.getText().toString();
                                               cmPassword = confirmPasswordTxt.getText().toString();


                                               if ((cPassword == "" || cPassword.isEmpty())
                                                       || (cmPassword == "" || cmPassword.isEmpty())
                                                       ) {
                                                   Toast.makeText(context,
                                                           "Please enter all the fields", Toast.LENGTH_SHORT)
                                                           .show();
                                               } else if (!cPassword.equals(cmPassword)) {
                                                   Toast.makeText(context,
                                                           "Password does not match", Toast.LENGTH_SHORT)
                                                           .show();
                                               } else {
                                                   Flag.signPassword = cPassword;

                                                   Flag.signCPassword = cmPassword;
                                                   if (Flag.privacyStatus) {
                                                       postSignUser();
                                                   } else {

                                                       alert.showAlertDialog(context, "MFP",
                                                               "Please agree with terms and conditions", false);
                                                   }
                                               }

                                           }
                                       }
        );
        check_agree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    if (!Flag.privacyStatus) {
                        Flag.privacyStatus = true;
                        FragmentManager privacyfragmentManager = getActivity().getSupportFragmentManager();

                        FragmentTransaction privacyfragmentTransaction = privacyfragmentManager.beginTransaction();
                        privacyfragmentTransaction.replace(R.id.signContentLayout, TermsFragment.newInstance("", ""));
                        privacyfragmentTransaction.addToBackStack(null);
                        privacyfragmentTransaction.commit();

//                        MEngine.switchFragment((BaseActivity) getActivity(), new TermsFragment(),  ((BaseActivity) getActivity()).getFrameLayout());
                    }
                }

            }
        });
    }

    //    public void itemClicked(View v) {
//        CheckBox checkBox = (CheckBox)v;
//        if(checkBox.isChecked()){
//            FragmentManager privacyfragmentManager=getActivity().getSupportFragmentManager();
//
//            FragmentTransaction privacyfragmentTransaction=privacyfragmentManager.beginTransaction();
//            privacyfragmentTransaction.replace(R.id.signContentLayout, PrivacyFragment.newInstance("", ""));
//            privacyfragmentTransaction.addToBackStack(null);
//            privacyfragmentTransaction.commit();
//        }
//    }
    private void postSignUser() {
        cd = new ConnectionCheck(context);
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.signUp();
            url = url + "userName=" + Flag.signmName + "&name=" + Flag.signName + "&dob=" + Flag.signBirthday + "&emailId=" + Flag.signMail + "&password=" + Flag.signPassword + "&phoneNumber=" + Flag.signPhoneNumber + "&deviceId=" + Flag.deviceId + "&countryCode=" + Flag.signCode + "&loginType=1&deviceType=2&bio=&website=&userType=0";
            new userSignUp().execute(url);

        }
    }


    private class userSignUp extends AsyncTask<String, Void, String> {
        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {//deviceId
            //   String json="{'userName':"+userName+","
            //          + "'emailId':"+email+","+"'password':"+password+","+"'phoneNumber':"+phone+","+"'deviceType':"+0+","+"'loginType':"+0+","+"'deviceId':"+"1234"+","+"'bio':"+"test"+","+"'website':"+"test.com"+"}";
            System.out.println(params[0]);


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
                if (errorCode.equals("200")) {


                    showAlertDialogSignUp(context, "MFP",
                            message, false);
                    clearFlagDetails();

                } else {
                    Toast.makeText(context,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (Exception e) {
                Toast.makeText(context,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    public void clearFlagDetails(){
        Flag.signmName="";
        Flag.signName ="";
        Flag.signBirthday ="";
        Flag.signMail="";
        Flag.signPassword ="";
        Flag.signPhoneNumber ="";
        Flag.signCode="";
        Flag.signCPassword="";
        Flag.privacyStatus = false;
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void showAlertDialogSignUp(final Context context, String title, String message,
                                      Boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle("You have joined MYFASHIONPARADE");
        int unicode = 0x1F4E9;
        // String message = "We've sent an email to" + getEmijoByUnicode(unicode) + Flag.signMail + ". Open it up to activate your account";
        message = "We've sent an email to " + getEmijoByUnicode(unicode) + Flag.signMail + ". Open it up to activate your account";
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);


                }
            });

        // Showing Alert Message
        alertDialog.show();
    }

    public String getEmijoByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
