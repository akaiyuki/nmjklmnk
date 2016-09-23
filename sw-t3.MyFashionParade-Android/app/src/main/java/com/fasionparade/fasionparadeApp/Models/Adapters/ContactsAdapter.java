package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 22/8/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{





    static Activity activity;

    // static OnItemClickListener mItemClickListener;

    ArrayList<Contacts> groupList;

    ArrayList<Boolean> selectedArrayList;

    public ContactsAdapter(Activity activity
            ,ArrayList<Contacts> groupList,ArrayList<Boolean> selectedArrayList

    ) {
        this.groupList = groupList;
        this.activity=activity;

        this.selectedArrayList=selectedArrayList;

        //   imageLoader= VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_contact_add_member_layout, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            final VHItem vhItemHolder=(VHItem)holder;

            vhItemHolder.contactName.setText(groupList.get(position).name);
            vhItemHolder.phoneNumber.setText(groupList.get(position).name);

            vhItemHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectedArrayList.set(position, vhItemHolder.checkBox.isChecked());

                }
            });


            vhItemHolder.mainRelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    vhItemHolder.checkBox.toggle();

                    selectedArrayList.set(position, vhItemHolder.checkBox.isChecked());
                   /* if(vhItemHolder.checkBox.isChecked()){



                    }else{


                    }*/

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder{//} implements View.OnClickListener {

        CircleImageView circleImage;
        CheckBox checkBox;

        TextView contactName,phoneNumber;

        RelativeLayout mainRelLayout;
        //  TextView contactName;

        public VHItem(View itemView) {
            super(itemView);

            circleImage=(CircleImageView) itemView.findViewById(R.id.circleImage);
            contactName=(TextView)itemView.findViewById(R.id.contactName);
            phoneNumber=(TextView)itemView.findViewById(R.id.phoneNumber);

            mainRelLayout=(RelativeLayout)itemView.findViewById(R.id.mainRelLayout);

            checkBox=(CheckBox)itemView.findViewById(R.id.checkBox);


            //  itemView.setOnClickListener(this);

        }


      /*  @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }*/
    }

  /*  public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }*/
}




