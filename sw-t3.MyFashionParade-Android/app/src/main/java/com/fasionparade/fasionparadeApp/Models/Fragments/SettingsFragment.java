package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.Functions.Core.MSharedPreferences;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.HelpActivity;
import com.fasionparade.fasionparadeApp.Models.Activity.MainActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView backImageView,allow_chart_on_off,contact_only_onoff;
    private LinearLayout shareFBLayout, addContactLayout, upgradePremiumLayout, promoCodeLayout;
    private RelativeLayout editProfileLayout, changePasswordLayout, pushSettingLayout, cellularDataLayout, allowChatLayout, chatWithLayout,
    historyLayout, logoutLayout, deleteAccountLayout, reportLayout, feedbackLayout, privacyLayout, termsLayout,helpLayout;
    private OnFragmentInteractionListener mListener;
    private View mView;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editorLogin;

    private static final String IS_LOGIN = "IsLoggedIn";
    SharedPreferences settingPreferences;

    SharedPreferences.Editor editor;




    ConnectionCheck cd;
    AlertDialogManager alert = new AlertDialogManager();


    User user;




    Boolean is_enable =true;

    Boolean is_enable_chart =true;
    boolean status1;
    boolean status2;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        mView=inflater.inflate(R.layout.fragment_settings, container, false);
        context=container.getContext();

        settingPreferences = getActivity().getSharedPreferences("SETTING_PREF", Context.MODE_PRIVATE);


        status1=settingPreferences.getBoolean("Allowchart", true);

        status2=settingPreferences.getBoolean("Mfpcontactonly", false);




        getUiInitialization();

        return mView;
    }

    public void getUiInitialization(){
        shareFBLayout=(LinearLayout)mView.findViewById(R.id.shareFBLayout);
        addContactLayout= (LinearLayout)mView.findViewById(R.id.addContatctLayout);
        upgradePremiumLayout= (LinearLayout)mView.findViewById(R.id.upgradePremiumLayout);
        promoCodeLayout= (LinearLayout)mView.findViewById(R.id.promoCodeLayout);
        editProfileLayout= (RelativeLayout)mView.findViewById(R.id.editProfileLayout);
        changePasswordLayout= (RelativeLayout)mView.findViewById(R.id.changePasswordLayout);
       // linkedAccountLayout= (RelativeLayout)mView.findViewById(R.id.linkedAccountLayout);
        pushSettingLayout= (RelativeLayout)mView.findViewById(R.id.pushSettingLayout);
        cellularDataLayout= (RelativeLayout)mView.findViewById(R.id.cellularDataLayout);
        allowChatLayout= (RelativeLayout)mView.findViewById(R.id.allowChatLayout);
        chatWithLayout= (RelativeLayout)mView.findViewById(R.id.chatWithLayout);
        historyLayout= (RelativeLayout)mView.findViewById(R.id.historyLayout);
        logoutLayout= (RelativeLayout)mView.findViewById(R.id.logoutLayout);
        deleteAccountLayout= (RelativeLayout)mView.findViewById(R.id.deleteAccountLayout);
        reportLayout= (RelativeLayout)mView.findViewById(R.id.reportLayout);
        feedbackLayout= (RelativeLayout)mView.findViewById(R.id.feedbackLayout);
        privacyLayout= (RelativeLayout)mView.findViewById(R.id.privacyLayout);
        termsLayout= (RelativeLayout)mView.findViewById(R.id.termsLayout);
        helpLayout=(RelativeLayout)mView.findViewById(R.id.helpLayout);
        backImageView= (ImageView)mView.findViewById(R.id.backImageView_addgroup);

        allow_chart_on_off =(ImageView)mView.findViewById(R.id.toggleAllowChatImageView_setting);

        contact_only_onoff =(ImageView)mView.findViewById(R.id.ChatOn_setting);


        shareFBLayout.setOnClickListener(this);
        addContactLayout.setOnClickListener(this);
        upgradePremiumLayout.setOnClickListener(this);
        promoCodeLayout.setOnClickListener(this);

        editProfileLayout.setOnClickListener(this);
        changePasswordLayout.setOnClickListener(this);
      //  linkedAccountLayout.setOnClickListener(this);
        pushSettingLayout.setOnClickListener(this);
        cellularDataLayout.setOnClickListener(this);
        allowChatLayout.setOnClickListener(this);
        chatWithLayout.setOnClickListener(this);
        historyLayout.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
        deleteAccountLayout.setOnClickListener(this);
        reportLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
        privacyLayout.setOnClickListener(this);
        termsLayout.setOnClickListener(this);
        helpLayout.setOnClickListener(this);

        allow_chart_on_off.setOnClickListener(this);
        contact_only_onoff.setOnClickListener(this);




        if(status1)
        {
            is_enable = true;
            allow_chart_on_off.setImageResource(R.drawable.on_icon);
            contact_only_onoff.setImageResource(R.drawable.off_icon);
        }
        else
        {
            is_enable = false;
            allow_chart_on_off.setImageResource(R.drawable.off_icon);
            contact_only_onoff.setImageResource(R.drawable.on_icon);
        }

        if(status2)
        {

            is_enable_chart = true;
            allow_chart_on_off.setImageResource(R.drawable.off_icon);
            contact_only_onoff.setImageResource(R.drawable.on_icon);

        }
        else
        {
            is_enable_chart = false;
            allow_chart_on_off.setImageResource(R.drawable.on_icon);
            contact_only_onoff.setImageResource(R.drawable.off_icon);

        }


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FashionHomeActivity.class));
                getActivity().finish();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {

            case R.id.shareFBLayout:


                user = Utils.getUserFromPreference(getActivity());
                String username = user.contactName;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, " Hey, Join me on MFP, the world's first fashion-based social voting App (download link), my MFP username is " +username);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.addContatctLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new AddContactFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.upgradePremiumLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new UpgradePremium(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.promoCodeLayout:

                break;
            case R.id.editProfileLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new EditProfileFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.changePasswordLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new ChangePasswordFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.pushSettingLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new PushSettings(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.cellularDataLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new CellularDataFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.allowChatLayout:

                break;
            case R.id.chatWithLayout:

                break;
            case R.id.historyLayout:

                break;
            case R.id.logoutLayout:
                new AlertDialog.Builder(v.getContext())

                        .setTitle("Logout")
                        .setMessage("Are you sure you want logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)

                            {


                                cd = new ConnectionCheck(getActivity());
                                if (!cd.isConnectingToInternet()) {
                                    alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
                                } else {
                                    String url = ResourceManager.Logout();
                                    user = Utils.getUserFromPreference(getActivity());
                                    String newURL = url + "userId=" + user.id;
                                    Log.i("Log out :", newURL);
                                    new Logout().execute(newURL);

//                                    startActivity(new Intent(getActivity(), MainActivity.class));
//                                    getActivity().finish();

                                }

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case R.id.deleteAccountLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new DeleteAccountFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.reportLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new ReportFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.feedbackLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new FeedbackFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.privacyLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), new PrivacyFragment(),((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.termsLayout:

                MEngine.switchFragment((BaseActivity) getActivity(), TermsFragment.newInstance("setting", "setting"), ((BaseActivity) getActivity()).getFrameLayout());
                break;
            case R.id.helpLayout:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;

            case R.id.toggleAllowChatImageView_setting:


                if(is_enable)
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = settingPreferences.edit();
                    prefsEditor.putBoolean("Allowchart",false);
                    prefsEditor.commit();
                    is_enable = false;
                    allow_chart_on_off.setImageResource(R.drawable.off_icon);
                    contact_only_onoff.setImageResource(R.drawable.on_icon);
                    pass_on_allow_chart();

                }
                else
                {
                    SharedPreferences.Editor prefsEditor; prefsEditor = settingPreferences.edit();
                    prefsEditor.putBoolean("Allowchart",true);
                    prefsEditor.commit();
                    is_enable = true;
                    allow_chart_on_off.setImageResource(R.drawable.on_icon);
                    contact_only_onoff.setImageResource(R.drawable.off_icon);
                    pass_off_allow_chart();


                }

                break;

            case R.id.ChatOn_setting:

                if(is_enable_chart)

                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = settingPreferences.edit();
                    prefsEditor.putBoolean("Mfpcontactonly",false);
                    prefsEditor.commit();
                    is_enable_chart = false;
                    contact_only_onoff.setImageResource(R.drawable.off_icon);
                    allow_chart_on_off.setImageResource(R.drawable.on_icon);
//                    pass_on_allow_chart();

                }
                else
                {

                    SharedPreferences.Editor prefsEditor; prefsEditor = settingPreferences.edit();
                    prefsEditor.putBoolean("Mfpcontactonly",true);
                    prefsEditor.commit();
                    is_enable_chart = true;
                    contact_only_onoff.setImageResource(R.drawable.on_icon);
                    allow_chart_on_off.setImageResource(R.drawable.off_icon);
//                    pass_off_allow_chart();

                    //                    pass_on_allow_chart();

                }

                break;

            default:
                break;
        }
    }




    public void pass_on_allow_chart()
    {

        cd = new ConnectionCheck(getActivity());
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.AllowChart();
            user = Utils.getUserFromPreference(getActivity());
            String newURL = url + "userId=" + user.id + "&allowFlag=3";
            Log.i("Pass ON :", newURL);
            new AllowChart().execute(newURL);
        }


    }


    public void pass_off_allow_chart()
    {

        cd = new ConnectionCheck(getActivity());
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet connection", false);
        } else {
            String url = ResourceManager.AllowChart();
            user = Utils.getUserFromPreference(getActivity());
            String newURL = url + "userId=" + user.id + "&allowFlag=1";
            Log.i("Pass OFF :", newURL);
            new AllowChart().execute(newURL);
        }


    }


    private class AllowChart extends AsyncTask<String, Void, String>
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


                }
                else
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


    private class Logout extends AsyncTask<String, Void, String>
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
                    pref =  getActivity().getSharedPreferences(Flag.USER_DETAILS,
                            Context.MODE_PRIVATE);
                    editorLogin = pref.edit();
                    editorLogin.putString(Flag.USER_DATA,"");
                    editorLogin.putBoolean(IS_LOGIN, false);
                    editorLogin.commit();

//                    MSharedPreferences.setSomeStringValue(AppController.getInstance(),Flag.USER_DATA,"");

                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(myIntent);
                    getActivity().finish();


                }
                else
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


}
