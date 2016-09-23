package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Views.ImageViews.CircleTransform;
import com.fasionparade.fasionparadeApp.Functions.Object.Contacts;
import com.fasionparade.fasionparadeApp.Functions.Object.ContactItemInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactHelperAdapter extends ContactListHelperAdapter
{

	 static List<Contacts>  myFriendArrayList;
	 static List<Contacts>  contactArrayList;
	TextView phoneView;
	TextView nameView;
	TextView inviteTextView;
	ImageView userImage,inviteImageView;


	String userName ="";

	public ContactHelperAdapter(Context _context, int _resource, List<Contacts> _items, List<Contacts> _myfriendarraylist)
	{
		super(_context, _resource, _items ,_myfriendarraylist);

		myFriendArrayList = _myfriendarraylist;
		contactArrayList = _items;




	}
	
	// override this for custom drawing
	public void populateDataForRow(View parentView, ContactItemInterface item , int position)
	{
		// default just draw the item only
		View infoView = parentView.findViewById(R.id.infoRowContainer);
		phoneView = (TextView)infoView.findViewById(R.id.fullNameView);
		nameView = (TextView)infoView.findViewById(R.id.nickNameView);
		userImage =(ImageView)infoView.findViewById(R.id.userImg_contact);


		inviteTextView =(TextView)infoView.findViewById(R.id.inviteTextView_new);
		inviteImageView = (ImageView)infoView.findViewById(R.id.inviteImageView_new);
		nameView.setText(item.getItemForIndex());

		if(item instanceof Contacts)
		{
			Contacts contactItem = (Contacts)item;
			phoneView.setText("" + contactItem.getNumber());
			Boolean found = false;
			userName = contactArrayList.get(position).name;
			if(myFriendArrayList != null)
			{
				for (int i = 0; i < myFriendArrayList.size(); i++)
				 {

                /*String phoneNo=myFriendsArraylist.get(i).get*/
					if (myFriendArrayList.get(i).getPhone().equals(contactArrayList.get(position).number))
					{
						found = true;
						System.out.println(myFriendArrayList.get(i).getPhone());

						if(contactArrayList.get(position).getPhoto()!= null && !contactArrayList.get(position).getPhoto().isEmpty())
						{
							if(myFriendArrayList.get(i).getPhoto()!= null && !myFriendArrayList.get(i).getPhoto().isEmpty()) {
								Picasso.with(getContext())
										.load(myFriendArrayList.get(i).getPhoto())
										.placeholder(R.drawable.no_image)
										.transform(new CircleTransform())
										.error(R.drawable.actionbar_profileicon)
										.into(userImage);
							}
						}
						else
						{
							userImage.setImageResource(R.drawable.contact_icon);
						}


						Log.i("Position list Adapter ", "True");

					}
					else
					{
						Log.i("Position list Adapter ","Flase");
						System.out.println(myFriendArrayList.get(i).getPhone());
						System.out.println(contactArrayList.get(position).number);
					}
				}
			}
			else
			{
				Log.i("List Adapter no value ","myFriendsArraylist");
			}

			if(found == true)
			{
				inviteImageView.setVisibility(View.VISIBLE);
				inviteTextView.setVisibility(View.GONE);

			}else
			{
				inviteTextView.setVisibility(View.VISIBLE);
				inviteImageView.setVisibility(View.GONE);
				inviteTextView.setBackgroundResource(R.drawable.login_button_bg);
				inviteTextView.setPadding(15, 10, 15, 10);
				inviteTextView.setText("Invite");
				userImage.setImageResource(R.drawable.contact_icon);

			}
		}
	}

}
