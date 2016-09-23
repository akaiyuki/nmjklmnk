package com.fasionparade.fasionparadeApp.Functions.Object;

/**
 * Created by root on 10/8/16.
 */
public class ChatDetail {


    String senderId;
    String receiverId;

    String myId;
    String friendId;
    String friendUserName;
    String friendName;

    String message;
    String typeIsImage;            //1= image  0= text
    String time;
    String date;
    String timeInMillis;
    String transferType;   // "SEND"  "RECEIVE"
    String readStatus;   // "UNREAD"   or "READ"


    public ChatDetail(String senderId, String receiverId, String myId, String friendId, String friendUserName, String friendName,
                      String message, String typeIsImage, String time, String date,
                      String timeInMillis, String transferType, String readStatus) {

        this.senderId = senderId;
        this.receiverId = receiverId;

        this.myId=myId;
        this.friendId=friendId;
        this.friendUserName=friendUserName;
        this.friendName=friendName;

        this.message = message;
        this.typeIsImage = typeIsImage;
        this.time = time;
        this.date = date;
        this.timeInMillis = timeInMillis;
        this.transferType = transferType;
        this.readStatus = readStatus;
    }


    public String getMyId() {
        return myId;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getFriendUserName() {
        return friendUserName;
    }

    public String getFriendName() {
        return friendName;
    }


    public String getReadStatus() {
        return readStatus;
    }

    public String getTimeInMillis() {
        return timeInMillis;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getSenderId() {
        return senderId;
    }


    public String getReceiverId() {
        return receiverId;
    }


    public String getMessage() {
        return message;
    }


    public String getTypeIsImage() {
        return typeIsImage;
    }


    public String getTime() {
        return time;
    }


    public String getDate() {
        return date;
    }


}
