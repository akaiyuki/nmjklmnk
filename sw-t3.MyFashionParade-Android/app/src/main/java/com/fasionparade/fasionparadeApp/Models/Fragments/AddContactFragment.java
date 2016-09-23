package com.fasionparade.fasionparadeApp.Models.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.AddContactAdapter;

import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.FindFriend;
import com.fasionparade.fasionparadeApp.Models.Adapters.GridSpacingItemDecoration;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddContactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    private TextView Empty_textview;
    private RecyclerView recyclerView;
    private EditText Search_text;
    ArrayList<FindFriend> findfriends;

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
     * @return A new instance of fragment AddContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddContactFragment newInstance(String param1, String param2) {
        AddContactFragment fragment = new AddContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddContactFragment() {
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_add_contact, container, false);

        context =getActivity();

        findfriends = new ArrayList<>();

        Empty_textview =(TextView)view.findViewById(R.id.empty_textview_addcontact);
        Search_text =(EditText)view.findViewById(R.id.searchEditText_addcontact);
        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView_addcontact);


        /**
         * Enabling Search Filter
         * */
        Search_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable editable) {


                cd = new ConnectionCheck(context);
                if (!cd.isConnectingToInternet())
                {
                    alert.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", false);
                } else {


                    String url = ResourceManager.searchfriend();
                    user = Utils.getUserFromPreference(context);
                    String newURL = url + "userId=" + user.id + "&searchText=" + editable;
                    Log.i("Search friend", newURL);
                    new Searchfriend().execute(newURL);

                }


            }
        });




        return  view;



    }

    //Searchfriend
    private class Searchfriend extends AsyncTask<String, Void, String>
    {
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

            String result = WebserviceAssessor.getData(params[0]);

            return result;



        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                Log.i("Result",result);

                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200"))
                {
                    Empty_textview.setVisibility(View.GONE);
                    findfriends = new ArrayList<>();

//                    //JSONOBJECT Commonjsonobject
                    JSONArray userdetails_itemsJsonarry = jsonObject.getJSONArray("userDetails");
                    for (int i = 0; i < userdetails_itemsJsonarry.length(); i++)
                    {

                        JSONObject userdetails_JSONObject = userdetails_itemsJsonarry.getJSONObject(i);
                        FindFriend findfriend =new FindFriend();
                        findfriend.setFriendid(userdetails_JSONObject.getString("friendId"));
                        findfriend.setFriendName(userdetails_JSONObject.getString("name"));
                        findfriend.setFriendProfilePic(userdetails_JSONObject.getString("profilePic"));
                        findfriend.setFriendUserName(userdetails_JSONObject.getString("userName"));
                        findfriend.setFriendfollowingStatus(userdetails_JSONObject.getString("friendStatus"));
                        findfriends.add(findfriend);
                    }

                    recyclerView.setVisibility(View.VISIBLE);
                    AddContactAdapter followMtpAdapter=new AddContactAdapter(getActivity(),findfriends);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    //adding item decoration
                    int spacing= getActivity().getResources().getDimensionPixelSize(R.dimen.spacing);
                    GridSpacingItemDecoration gridSpacingItemDecoration=new GridSpacingItemDecoration(1,spacing,true);
                    recyclerView.addItemDecoration(gridSpacingItemDecoration);
                    recyclerView.setAdapter(followMtpAdapter);

                }

                else
                {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    Empty_textview.setVisibility(View.VISIBLE);
                    Empty_textview.setGravity(Gravity.CENTER);
                    recyclerView.setVisibility(View.GONE);
                    Empty_textview.setText("Enter the username of the person you want to follow in the above search field");
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
