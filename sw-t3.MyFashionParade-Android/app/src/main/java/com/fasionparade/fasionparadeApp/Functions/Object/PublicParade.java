package com.fasionparade.fasionparadeApp.Functions.Object;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ksuresh on 4/7/2016.
 */
public class PublicParade {
    public  String userId;
    public  String paradeId;
    public  String paradeName;
    public  String sharedWith;
    public  String duration;
    public  String aboutParade;
    public  String tag;
    public  String startTime;
    public  String endTime;
    public  String groupId;
    public  ArrayList<String> imagePath=new ArrayList<String>();
    public  ArrayList<String> ImageId=new ArrayList<String>();
    public  JSONArray imagePathJson=new JSONArray();
    public  String userName;
    public  String completedStatus;
    public  String followingStatus;
    public  String profilePic;
    public  String type;
    public  String friendId;
}
