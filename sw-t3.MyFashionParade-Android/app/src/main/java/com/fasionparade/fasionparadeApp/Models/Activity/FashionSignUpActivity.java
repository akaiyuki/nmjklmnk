package com.fasionparade.fasionparadeApp.Models.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Views.SignupView.SignupOneFragment;
import com.fasionparade.fasionparadeApp.Views.SignupView.SignupThreeFragment;
import com.fasionparade.fasionparadeApp.Views.SignupView.SignupTwoFragment;

public class FashionSignUpActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion_sign_up);


        viewPager=(ViewPager)findViewById(R.id.viewPager);
        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        //  viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(2);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f;

            if (position == 0) {
                f = new SignupOneFragment();
            } else if (position == 1) {
                f = new SignupTwoFragment();
            } else if (position == 2) {
                f = new SignupThreeFragment();
            }
            else{
                f = new SignupOneFragment();

            }

            return f;
        }

        @Override
        public int getCount() {
            return 3;
        }


    }
//
//    private void initScreen() {
//        submitButton=(TextView)findViewById(R.id.submitButton);
//        facebookLayout=(RelativeLayout)findViewById(R.id.facebookLayout);
//        emailTxt=(EditText)findViewById(R.id.emailTxt);
//        phoneNubmerTxt=(EditText)findViewById(R.id.phoneNubmerTxt);
//        userNameTxt=(EditText)findViewById(R.id.userNameTxt);
//        passwordTxt=(EditText)findViewById(R.id.passwordTxt);
//        cPasswordTxt=(EditText)findViewById(R.id.CpasswordTxt);
//
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 email = emailTxt.getText().toString();
//                 phone = phoneNubmerTxt.getText().toString();
//                 userName = userNameTxt.getText().toString();
//                 password = passwordTxt.getText().toString();
//                 cpassword = cPasswordTxt.getText().toString();
//                if ((email == "" || email.isEmpty())
//                        || (phone == "" || phone.isEmpty())
//                        || (userName == "" || userName.isEmpty()) || (password == "" || password.isEmpty()) || (cpassword == "" || cpassword.isEmpty())) {
//                    Toast.makeText(FashionSignUpActivity.this,
//                            "Please enter all the fields", Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//
//                    postSignUser(email, phone, userName, password, cpassword);
//
//                }
//
//            }
//        });
//
//        facebookLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(FashionSignUpActivity.this, FashionCongratulations.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void postSignUser(String email, String phone, String userName, String password, String cpassword) {
//
//        cd = new ConnectionCheck(context);
//        if (!cd.isConnectingToInternet()) {
//            alert.showAlertDialog(context, "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
//        } else {
//
//            String url = ResourceManager.signUp();
//
//            url=url+"userName="+userName+"&emailId="+email+"&password="+password+"&phoneNumber="+phone+"&deviceId="+ Flag.deviceId+"&loginType=0&deviceType=0&bio=testbio&website=test.com&countryCode=91";
//            new userSignUp().execute(url);
//
//        }
//    }
//    private class userSignUp extends AsyncTask<String, Void, String> {
//        @SuppressWarnings("static-access")
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            startAnim();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {//deviceId
//         //   String json="{'userName':"+userName+","
//         //          + "'emailId':"+email+","+"'password':"+password+","+"'phoneNumber':"+phone+","+"'deviceType':"+0+","+"'loginType':"+0+","+"'deviceId':"+"1234"+","+"'bio':"+"test"+","+"'website':"+"test.com"+"}";
//            System.out.println(params[0]);
//
//
//            String result = WebserviceAssessor.getData(params[0] );
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String errorCode;
//                String message;
//                errorCode = jsonObject.getString("errorCode");
//                message = jsonObject.getString("message");
//                if (errorCode.equals("200")) {
//                    Toast.makeText(FashionSignUpActivity.this,
//                            "Successfully Register", Toast.LENGTH_SHORT)
//                            .show();
//                     Intent intent=new Intent(FashionSignUpActivity.this,FashonLoginActivity.class);
//                     startActivity(intent);
//                }else{
//                    Toast.makeText(FashionSignUpActivity.this,
//                            message, Toast.LENGTH_SHORT)
//                            .show();
//                }
//                stopAnim();
//            }catch(Exception e){
//                Toast.makeText(FashionSignUpActivity.this,
//                        "Something Wrong", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
//    void startAnim() {
//        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
//    }
//
//    void stopAnim() {
//        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
//    }
}
