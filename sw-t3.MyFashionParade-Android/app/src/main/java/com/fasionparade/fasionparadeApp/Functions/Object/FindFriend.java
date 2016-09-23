package com.fasionparade.fasionparadeApp.Functions.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vishnu on 7/21/2016.
 */
public class FindFriend implements Parcelable
{

    public String friendId;
    public String friendName;
    public String friendUserName;
    public String friendProfilePic;
    public String friendFollowingStatus;


    public String getFriendfollowingStatus() {
        return friendFollowingStatus;
    }

    public void setFriendfollowingStatus(String friendFollowingStatus) {
        friendFollowingStatus = friendFollowingStatus;
    }



    public String getFriendid() {
        return friendId;
    }

    public void setFriendid(String friendId) {
        this.friendId = friendId;
    }



    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendUserName() {
        return friendUserName;
    }

    public void setFriendUserName(String friendUserName) {
        this.friendUserName = friendUserName;
    }

    public String getFriendProfilePic() {
        return friendProfilePic;
    }

    public void setFriendProfilePic(String friendProfilePic) {
        this.friendProfilePic = friendProfilePic;
    }

    public FindFriend()
    {

    }



    protected FindFriend(Parcel in) {
        friendId = in.readString();
        friendName = in.readString();
        friendUserName = in.readString();
        friendProfilePic = in.readString();
        friendFollowingStatus=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(friendId);
        dest.writeString(friendName);
        dest.writeString(friendUserName);
        dest.writeString(friendProfilePic);
        dest.writeString(friendFollowingStatus);

    }

    @SuppressWarnings("unused")
    public static final Creator<FindFriend> CREATOR = new Creator<FindFriend>() {
        @Override
        public FindFriend createFromParcel(Parcel in) {
            return new FindFriend(in);
        }

        @Override
        public FindFriend[] newArray(int size) {
            return new FindFriend[size];
        }
    };
}
