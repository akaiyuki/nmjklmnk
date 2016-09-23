package com.fasionparade.fasionparadeApp.Functions.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksuresh on 7/19/2016.
 */
public class Group implements Parcelable {


    public String userId;
    public String groupName;

    public String groupId;
    public String totalMembers;
    public String isAdmin;

    protected Group(Parcel in) {
        userId = in.readString();
        groupName = in.readString();
        groupId = in.readString();
        totalMembers = in.readString();
        isAdmin = in.readString();
    }

    public Group(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(groupName);
        dest.writeString(groupId);
        dest.writeString(totalMembers);
        dest.writeString(isAdmin);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
