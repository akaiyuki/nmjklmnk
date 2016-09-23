package com.fasionparade.fasionparadeApp.Functions.Object;

import android.net.Uri;

import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

/**
 * Created by ksuresh on 4/4/2016.
 */
public class Flag {
    public static String fbName;
    public static String fbId;
    public static String fbMail;
    public static final String USER_DETAILS = "userDetailsPref";
    public static final String USER_DATA = "userDetailsData";
    public static final String IS_CAMERA= "IsLoggedIn";
    public static final String CAMERA_DETAILS = "cameraDetailsPref";

    public static  String deviceId = "";
    public static Boolean profilePage=false;

    public static String signName;
    public static String signBirthday;
    public static String signmName;
    public static String signMail;
    public static String signCode;
    public static String signPhoneNumber;

    public static boolean privacyStatus=false;
    public static String signPassword;
    public static String signCPassword;
    public static ArrayList<Uri> totalImageUris=new ArrayList<>();
    public static ArrayList<String> totalImage=new ArrayList<>();
    public static int imageLimit=2;
}
