package com.fasionparade.fasionparadeApp.Views.SignupView;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasionparade.fasionparadeApp.Models.Activity.FashionSignUpActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.Iso2Phone;


public class SignupTwoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String indicative;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String email, code, phone;
    private EditText emailTxt, codeTxt, phoneTxt;
    private TextView backButton, loginButton;
    View mView;
    Context context;
    AlertDialogManager alert = new AlertDialogManager();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupTwoFragment newInstance(String param1, String param2) {
        SignupTwoFragment fragment = new SignupTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignupTwoFragment() {
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
        mView = inflater.inflate(R.layout.fragment_signup_two, container, false);
        context = container.getContext();
        getCountrycode();
        getUiInitialization();

        return mView;
    }
    public void getCountrycode(){
        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();

        indicative= Iso2Phone.getPhone(countryCodeValue);

    }


    public void getUiInitialization() {

        emailTxt = (EditText) mView.findViewById(R.id.emailTxt);
        codeTxt = (EditText) mView.findViewById(R.id.codeTxt);
        phoneTxt = (EditText) mView.findViewById(R.id.phoneTxt);
        backButton = (TextView) mView.findViewById(R.id.backButton);
        loginButton = (TextView) mView.findViewById(R.id.loginButton);
        codeTxt.setText(indicative);
        backButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              FashionSignUpActivity.viewPager.setCurrentItem(0);
                                          }
                                      }
        );

        codeTxt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           new MaterialDialog.Builder(context)
                                                   .title("Set Code")
                                                   .items(R.array.preference_values)
                                                   .positiveColorRes(R.color.colorPrimary)
                                                   .buttonRippleColorRes(R.color.colorPrimary)
                                                   .widgetColorRes(R.color.colorPrimary)
                                                   .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                                       @Override
                                                       public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                                        //   String split[] =text.toString().split("'+'");
                                                           String lastWord = text.toString().substring(text.toString().lastIndexOf(" ") + 1);
                                                           codeTxt.setText(lastWord);
                                                           return true;
                                                       }
                                                   })
                                                   .positiveText(R.string.choose)
                                                   .show();

                                       }
                                   }

        );
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               email = emailTxt.getText().toString();
                                               code = codeTxt.getText().toString();
                                               phone = phoneTxt.getText().toString();

                                               if ((email == "" || email.isEmpty())

                                                       || (phone == "" || phone.isEmpty())) {

                                                   alert.showAlertDialog(context, "Step 1",
                                                           "Please enter all the fields", false);
                                               } else if (!isEmailValid(email)) {
                                                   alert.showAlertDialog(context, "Step 1",
                                                           "Invalid email address", false);
                                               } else {
                                                   Flag.signMail = email;
                                                   Flag.signCode = code;
                                                   Flag.signPhoneNumber = phone;
                                                   FashionSignUpActivity.viewPager.setCurrentItem(2);

                                               }

                                           }
                                       }
        );
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }


}
