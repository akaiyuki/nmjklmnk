package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Core.MSharedPreferences;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Service.JsonParser;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.Iso2Phone;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int INTENT_REQUEST_GET_IMAGES = 13;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText mailTxt, webTxt, bioTxt, nameTxt, phoneNumberTxt, phoneTxt;
    private OnFragmentInteractionListener mListener;

    ImageView backImageView, toggleImageView, circleImageView;
    Context context;
    boolean toggle = false;
    TextView startParadeTextView;
    ConnectionCheck connectionCheck;
    AlertDialogManager alert = new AlertDialogManager();
    String name, web, bio, mail, code, phoneNum, profile = "", userId = "", indicative;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = container.getContext();
        sharedPreferences = getActivity().getSharedPreferences(Flag.USER_DETAILS, Context.MODE_PRIVATE);
        backImageView = (ImageView) view.findViewById(R.id.backImageView_addgroup);
        circleImageView = (ImageView) view.findViewById(R.id.circleImagView);
        toggleImageView = (ImageView) view.findViewById(R.id.toggleImageView);
        mailTxt = (EditText) view.findViewById(R.id.mailTxt);
        webTxt = (EditText) view.findViewById(R.id.webTxt);
        bioTxt = (EditText) view.findViewById(R.id.bioTxt);
        nameTxt = (EditText) view.findViewById(R.id.nameTxt);
        phoneTxt = (EditText) view.findViewById(R.id.phoneTxt);
        phoneNumberTxt = (EditText) view.findViewById(R.id.phoneNumberTxt);
        startParadeTextView = (TextView) view.findViewById(R.id.startParadeTextView);

        ImageView mImageProfile = (ImageView) view.findViewById(R.id.profileImageView_addgroup);
        mImageProfile.setVisibility(View.GONE);

        getCountryCode();


        phoneTxt.setText(indicative);
        phoneTxt.setOnClickListener(new View.OnClickListener() {
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

                                                            String lastWord = text.toString().substring(text.toString().lastIndexOf(" ") + 1);
                                                            phoneTxt.setText(lastWord);
                                                            return true;
                                                        }
                                                    })
                                                    .positiveText(R.string.choose)
                                                    .show();

                                        }
                                    }

        );
        startParadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();

            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        toggleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toggle) {
                    toggleImageView.setImageResource(R.drawable.on_icon);
                    toggle = false;

                } else {
                    toggleImageView.setImageResource(R.drawable.off_icon);
                    toggle = true;
                }

            }
        });


        return view;
    }

    public void getCountryCode() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();

        indicative = Iso2Phone.getPhone(countryCodeValue);

    }


    private void updateProfile() {
        name = nameTxt.getText().toString();
        web = webTxt.getText().toString();
        bio = bioTxt.getText().toString();
        mail = mailTxt.getText().toString();
        code = phoneTxt.getText().toString();
        phoneNum = phoneNumberTxt.getText().toString();

        connectionCheck = new ConnectionCheck(context);
        if (!connectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.myUpdateProfile();


            new userInfoSend().execute(url);
        }
    }

    private class userInfoSend extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.postProfile(params[0], name, web, bio, mail, profile, userId, code, phoneNum);


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

                if (errorCode.equals("200")) {


                    User user = JsonParser.getUser(jsonObject);
                    if (user.message.equals("success")) {
                        String loginUser = Utils.getGson().toJson(user,
                                User.class);
                        editor = sharedPreferences.edit();
                        editor.putString(Flag.USER_DATA, loginUser);
                        editor.commit();

//                        MSharedPreferences.setSomeStringValue(AppController.getInstance(),Flag.USER_DATA, loginUser);

                        Toast.makeText(getActivity(),
                                "Successfully update", Toast.LENGTH_SHORT)
                                .show();
                        getActivity().onBackPressed();
                    }


                } else {

                    Toast.makeText(getActivity(),
                            "Something Wrong", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        User user = Utils.getUserFromPreference(context);
        mailTxt.setText(user.mail);
        webTxt.setText(user.website);
        bioTxt.setText(user.bio);
        nameTxt.setText(user.contactName);
        phoneTxt.setText(user.countryCode);
        phoneNumberTxt.setText(user.mobile);
        userId = user.id;
        if (user.profilePic != null && !user.profilePic.isEmpty())
            Picasso.with(context)
                    .load(user.profilePic)
                    .placeholder(R.drawable.no_image)
                    .into(circleImageView);

        Config config = new Config();
        //  config.setCameraHeight(android.R.dimen.app_camera_height);
        //     config.setToolbarTitleRes(R.string.custom_title);
        config.setSelectionMin(1);
        config.setSelectionLimit(1);
        //     config.setSelectedBottomHeight(R.dimen.bottom_height);
        ImagePickerActivity.setConfig(config);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ImagePickerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {

            ArrayList<Uri> image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);


            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inSampleSize = 2;
            Bitmap bitmap;

            bitmap = BitmapFactory.decodeFile(image_uris.get(0).toString(), options);


            circleImageView.setImageURI(image_uris.get(0));

            //  ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //  bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            //  byte[] data = bos.toByteArray();
            //  profile=data.toString();
            profile = image_uris.get(0).toString();

            //do something
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
