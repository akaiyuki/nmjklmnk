package com.fasionparade.fasionparadeApp.Functions.Object;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by ksuresh on 4/6/2016.
 */
public class Contacts implements Parcelable ,ContactItemInterface {

    public String id;
    public String name;
    public String number;
    public String countryCode;
    public String photo;
    public String followingStatus;


    public String getFollowingstatus() {
        return followingStatus;
    }

    public void setFollowingstatus(String followingStatus) {
        this.followingStatus = followingStatus;
    }

    public String getCountrycode() {
        return countryCode;
    }

    public void setCountrycode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Contacts() {

    }

    @Override
    public String getItemForIndex() {
        return name;
    }

    protected Contacts(Parcel in) {
        id = in.readString();
        name = in.readString();
        number = in.readString();
        photo = in.readString();
        countryCode = in.readString();
        followingStatus = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(photo);
        dest.writeString(followingStatus);
        dest.writeString(countryCode);
    }

    @SuppressWarnings("unused")
    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };


    public String getPhone() {
        return "+" + countryCode + "" + number;
    }


    public String getNames() {

        return "" + name;
    }

}