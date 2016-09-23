package com.fasionparade.fasionparadeApp.Functions.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksuresh on 7/19/2016.
 */
public class GroupMember implements Parcelable {



    public String memberId;
    public String userName;

    public String loginType;
    public String deviceType;
    public String countryCode;

    public String phoneNumber;
    public String dob;
    public String name;


    public GroupMember(){

    }


    protected GroupMember(Parcel in) {
        memberId = in.readString();
        userName = in.readString();
        loginType = in.readString();
        deviceType = in.readString();
        countryCode = in.readString();
        phoneNumber = in.readString();
        dob = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memberId);
        dest.writeString(userName);
        dest.writeString(loginType);
        dest.writeString(deviceType);
        dest.writeString(countryCode);
        dest.writeString(phoneNumber);
        dest.writeString(dob);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GroupMember> CREATOR = new Parcelable.Creator<GroupMember>() {
        @Override
        public GroupMember createFromParcel(Parcel in) {
            return new GroupMember(in);
        }

        @Override
        public GroupMember[] newArray(int size) {
            return new GroupMember[size];
        }
    };
}
