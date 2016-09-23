package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fasionparade.fasionparadeApp.Functions.Core.BaseActivity;
import com.fasionparade.fasionparadeApp.Functions.Core.MEngine;
import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Fragments.ContactGroupDetailFragment;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.Group;

import java.util.ArrayList;

/**
 * Created by ksuresh on 7/19/2016.
 */
public class ContactsGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    Context context;
     static BaseActivity baseActivity;

    ImageLoader imageLoader;

    ArrayList<Group> groupList;

    //  public ContactsFragment contactsFragment;

    public ArrayList<Contacts> myFriendsContactsList;

    public ContactsGroupAdapter(BaseActivity baseActivity,ArrayList<Group> groupList,ArrayList<Contacts> myFriendsContactsList) {

        this.groupList = groupList;
        this.baseActivity=baseActivity;

        this.myFriendsContactsList=myFriendsContactsList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_contact_group_item, parent, false);
        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder=(VHItem)holder;
            vhItemHolder.groupName.setText(groupList.get(position).groupName);

            vhItemHolder.setGroup(groupList.get(position));
            vhItemHolder.setPosition(position);
            vhItemHolder.setGroupList(groupList);
            vhItemHolder.setMyFriendsList(myFriendsContactsList);

        }


    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView groupName;
        Group group;
        ArrayList<Group> groupList;

        ArrayList<Contacts> myFriendsContactsList;

        int position;

        public VHItem(View itemView) {
            super(itemView);

            groupName=(TextView)itemView.findViewById(R.id.groupName);

            Log.i("", "Adding Listener");
            itemView.setOnClickListener(this);

        }


        public void setGroup(Group group){

            this.group=group;

        }

        public void setPosition(int position){
            this.position=position;
        }

        public void setGroupList(ArrayList<Group> groupList){

            this.groupList=groupList;

        }

        public void setMyFriendsList(ArrayList<Contacts> myFriendsContactsList){

            this.myFriendsContactsList= myFriendsContactsList;
        }



        @Override
        public void onClick(View v) {

//            android.support.v4.app.FragmentTransaction fragmentTransaction=activity.getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.mainContentLayout, ContactGroupDetailFragment.newInstance(group, groupList, position, myFriendsContactsList));
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();


            MEngine.switchFragment(baseActivity, ContactGroupDetailFragment.newInstance(group, groupList, position, myFriendsContactsList),  baseActivity.getFrameLayout());



        }

    }

}
