//package com.fasionparade.fasionparadeApp.Models.Fragments;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
//import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
//import com.fasionparade.fasionparadeApp.Models.Activity.FashionHomeActivity;
//import com.fasionparade.fasionparadeApp.Models.Activity.NewParadeActivity;
//import com.fasionparade.fasionparadeApp.Models.Activity.ProfileActivity;
//import com.fasionparade.fasionparadeApp.Models.Activity.SettingsActivity;
//import com.fasionparade.fasionparadeApp.R;
//import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
//import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
//import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
//
//import java.util.ArrayList;
//
//
//public class HomeFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    // private OnFragmentInteractionListener mListener;
//    ConnectionCheck cd;
//    AlertDialogManager alert = new AlertDialogManager();
//
//    RecyclerView recyclerView;
//
//    LinearLayout startParadeLInLayout;
//    TextView startNPTxt,inviteCMTxt,CGroupTxt;
//    ImageView profileImageView,settingImage;
//    Context context;
//    ArrayList<String>imageList=new ArrayList<>();
//
//    public HomeFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view=inflater.inflate(R.layout.fragment_home, container, false);
//        profileImageView=(ImageView)view.findViewById(R.id.profileImageView);
//        settingImage=(ImageView)view.findViewById(R.id.settingImage);
//        startNPTxt=(TextView)view.findViewById(R.id.startNPTxt);
//        inviteCMTxt=(TextView)view.findViewById(R.id.inviteCMTxt);
//        CGroupTxt=(TextView)view.findViewById(R.id.CGroupTxt);
//        context=container.getContext();
//        inviteCMTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                FashionHomeActivity.viewPager.setCurrentItem(4);
//                MEngine.switchFragment((BaseActivity) getActivity(), new InboxFragment(), ((BaseActivity)getActivity()).getFrameLayout());
//
//            }
//        });
//        CGroupTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                FashionHomeActivity.viewPager.setCurrentItem(4);
//                MEngine.switchFragment((BaseActivity) getActivity(), new InboxFragment(), ((BaseActivity)getActivity()).getFrameLayout());
//
//
//            }
//        });
//
//        startNPTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getActivity(), NewParadeActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        settingImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                MEngine.switchFragment((BaseActivity) getActivity(),new SettingsFragment(), ((BaseActivity) getActivity()).getFrameLayout());
//
//                startActivity(new Intent(getActivity(), SettingsActivity.class));
//                getActivity().finish();
//
//            }
//        });
//
//        profileImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
//                startActivity(new Intent(getActivity(), ProfileActivity.class));
//                getActivity().finish();
//            }
//        });
//
//        return view;
//
//    }
//
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if( Flag.profilePage)
//        {
//            Flag.profilePage=false;
//            startActivity(new Intent(getActivity(), ProfileActivity.class));
//            getActivity().finish();
//
//        }
//
//
//
//    }
//
////    private void getPublicParadeList() {
////        cd = new ConnectionCheck(context);
////        User user= Utils.getUserFromPreference(context);
////        if (!cd.isConnectingToInternet()) {
////            alert.showAlertDialog(context, "Internet Connection Error",
////                    "Please connect to working Internet connection", false);
////        } else {
////            String url = ResourceManager.getPublicParade();
////            url = url + "userId=" + user.id + "&testTime=2016-03-20";
////            new getPublicParede().execute(url);
////        }
////    }
////    private class getPublicParede extends AsyncTask<String, Void, String> {
////        ProgressDialog pDialog;
////        List<PublicParade> pParade;
////        @SuppressWarnings("static-access")
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            pDialog = new ProgressDialog(context);
////            pDialog.setMessage("Loading...");
////            pDialog.show();
////        }
////
////        @Override
////        protected String doInBackground(String... params) {
////
////            String result = WebserviceAssessor.getData(params[0]);
////
////
////            return result;
////         }
////
////        @Override
////        protected void onPostExecute(String result) {
////
////            super.onPostExecute(result);
////
////            try {
////                JSONObject jsonObject = new JSONObject(result);
////                String errorCode;
////                String message;
////                errorCode = jsonObject.getString("errorCode");
////                message = jsonObject.getString("message");
////                if (errorCode.equals("200")) {
////
////                    pParade = JsonParser.getPPResult(jsonObject);
////                    int length=pParade.size();
////                    if(length>0){
////
////                        JSONObject imageObject;
////                        for (int j = 0; j < length; j++) {
////                            JSONArray imageArray=pParade.get(j).imagePathJson;
////                            imageObject = imageArray.getJSONObject(0);
////
////                            String image=imageObject.getString("fileName");
////
////                            imageList.add(image);
////                        }
////
////                        FashionHomeGridAdapter adapter = new FashionHomeGridAdapter((FashionHomeActivity)getActivity(), imageList
////                                // ,productCategoryObject
////                        );
////                        recyclerView.setAdapter(adapter);
////
////                        adapter.SetOnItemClickListener(new FashionHomeGridAdapter.OnItemClickListener() {
////
////                            @Override
////                            public void onItemClick(View v, int position) {
////                                // do something with position
////
////                               // System.out.println(pParade.get(position).userId);
////                                Bundle bun = new Bundle();
////                                bun.putString("userId", pParade.get(position).userId);
////                                bun.putString("paradeId", pParade.get(position).paradeId);
////                                bun.putString("paradeName", pParade.get(position).paradeName);
////                                bun.putString("sharedWith", pParade.get(position).sharedWith);
////                                bun.putString("duration", pParade.get(position).duration);
////                                bun.putString("aboutParade", pParade.get(position).aboutParade);
////                                bun.putString("tag", pParade.get(position).tag);
////                                bun.putString("startTime", pParade.get(position).startTime);
////                                bun.putString("endTime", pParade.get(position).endTime);
////                                bun.putString("groupId", pParade.get(position).groupId);
////                                bun.putString("imagePathJson", pParade.get(position).imagePathJson.toString());
////                                Intent intent=new Intent(getActivity(), VotingActivity.class);
////                                intent.putExtras(bun);
////                                startActivity(intent);
////                            }
////                        });
////
////                    }else{
////                        Toast.makeText(getActivity(),
////                                "No Records Found", Toast.LENGTH_SHORT)
////                                .show();
////                    }
////
////
////                }else{
////                    Toast.makeText(getActivity(),
////                            message, Toast.LENGTH_SHORT)
////                            .show();
////                }
////
////            }catch(Exception e){
////                e.printStackTrace();
////                Toast.makeText(getActivity(),
////                        "Something Wrong", Toast.LENGTH_SHORT)
////                        .show();
////            }
////            pDialog.cancel();
////        }
////    }
//
////    // TODO: Rename method, update argument and hook method into UI event
////    public void onButtonPressed(Uri uri) {
////        if (mListener != null) {
////            mListener.onFragmentInteraction(uri);
////        }
////    }
////
////
////    @Override
////    public void onDetach() {
////        super.onDetach();
////        mListener = null;
////    }
//
////    public interface OnFragmentInteractionListener {
////        // TODO: Update argument type and name
////        void onFragmentInteraction(Uri uri);
////    }
//
//
////
////    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
////        private int space;
////
////        public SpacesItemDecoration(int space) {
////            this.space = space;
////        }
////
////        @Override
////        public void getItemOffsets(Rect outRect, View view,
////                                   RecyclerView parent, RecyclerView.State state) {
////            if((parent.getChildPosition(view)%2)==0){
////                //even
////                outRect.left = space;
////                outRect.right = space;
////                outRect.bottom = space;
////            }else{
////                //odd
////                outRect.left = space;
////                outRect.bottom = space;
////
////            }
////            // Add top margin only for the first item to avoid double space between items
////            if (parent.getChildPosition(view) == 0) {
////                outRect.top = 0;
////                outRect.left = 0;
////                outRect.right = 0;
////                outRect.bottom = 0;
////            }
////        }
////    }
//
//
//
//
//
//
//
//}
