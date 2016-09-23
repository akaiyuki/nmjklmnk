package com.fasionparade.fasionparadeApp.Views.SignupView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.Models.Activity.FashionSignUpActivity;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupOneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupOneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText nameTxt, birthdayTxt, mfpUserNameTxt;
    private TextView backButton, loginButton;
    View mView;
    Context context;
    private String name, bithday, mName;
    Calendar myCalendar = Calendar.getInstance();
    int year=0;
    AlertDialogManager alert = new AlertDialogManager();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupOneFragment newInstance(String param1, String param2) {
        SignupOneFragment fragment = new SignupOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignupOneFragment() {
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
        mView = inflater.inflate(R.layout.fragment_signup_one, container, false);
        context = container.getContext();
        getUiInitialization();
        return mView;
    }

    public void getUiInitialization() {

        nameTxt = (EditText) mView.findViewById(R.id.nameTxt);
        birthdayTxt = (EditText) mView.findViewById(R.id.birthdayTxt);
        mfpUserNameTxt = (EditText) mView.findViewById(R.id.mfpUserNameTxt);
        backButton = (TextView) mView.findViewById(R.id.backButton);
        loginButton = (TextView) mView.findViewById(R.id.loginButton);
        backButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              getActivity().onBackPressed();
                                          }
                                      }
        );
        birthdayTxt
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(context, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               name = nameTxt.getText().toString();
                                               bithday = birthdayTxt.getText().toString();
                                               mName = mfpUserNameTxt.getText().toString();

                                               if ((name == "" || name.isEmpty())
                                                       || (bithday == "" || bithday.isEmpty())
                                                       || (mName == "" || mName.isEmpty())) {
                                                   Toast.makeText(context,
                                                           "Please enter all the fields", Toast.LENGTH_SHORT)


                                                           .show();


                                               } else if (year < 13) {

                                                   alert.showAlertDialog(context, "Step 1",
                                                           "User must be at least 13 years of age", false);


                                               } else {
                                                   Flag.signName = name;
                                                   Flag.signBirthday = bithday;
                                                   Flag.signmName = mName;
                                                   FashionSignUpActivity.viewPager.setCurrentItem(1);

                                               }

                                           }
                                       }
        );
    }
    public String getEmijoByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {
        String FinalDate;
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthdayTxt.setText(sdf.format(myCalendar.getTime()));

        FinalDate=sdf.format(myCalendar.getTime());

        try {


            Calendar c = Calendar.getInstance();


            SimpleDateFormat df = new SimpleDateFormat(myFormat);
            String CurrentDate = df.format(c.getTime());

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat(myFormat);

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            year = Integer.valueOf(dayDifference )/ 365;
            Log.e("DIDN'T WORK", "exception " + year);
        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }


    }
    @Override
    public void onDetach() {
        super.onDetach();

    }


}
