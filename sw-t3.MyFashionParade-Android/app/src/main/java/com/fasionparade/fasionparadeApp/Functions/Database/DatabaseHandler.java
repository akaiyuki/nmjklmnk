package com.fasionparade.fasionparadeApp.Functions.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fasionparade.fasionparadeApp.Functions.Object.ActiveChatUser;
import com.fasionparade.fasionparadeApp.Functions.Object.ChatDetail;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MFP";

    // Contacts table name
    private static final String TABLE_CHAT = "ChatDetail";

    // Contacts Table Columns names
    public static final String KEY_COLUMN_ID = "id";

    private static final String KEY_SENDERID = "senderid";       // Sender user id
    private static final String KEY_RECEIVERID = "receiverid";   // Receiver user id

    private static final String KEY_MY_ID = "my_id";             // Logged in user id
    private static final String KEY_FRIEND_ID = "friend_id";     // Friend's user id
    private static final String KEY_FRIEND_USERNAME = "friend_user_name";             // Friend's user name
    private static final String KEY_FRIEND_NAME = "friend_name";     // Friend's name


    private static final String KEY_TEXT_OR_URI = "message";     // text or image uri or image url
    private static final String KEY_TYPE_ISIMAGE = "type";   //1 : Image   0: Text
    private static final String KEY_Time = "time";              //Time
    private static final String KEY_DATE = "date";              //Date
    private static final String KEY_TIME_IN_MILLIS = "time_in_milli_sec";
    private static final String KEY_TYPE_TRANSFER = "transfer";   //   "SEND"   or "RECEIVE"
    private static final String KEY_READ_STATUS = "read_status";   //   "UNREAD"   or "READ"


    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_CHAT + "(" + KEY_COLUMN_ID + " integer primary key," + KEY_SENDERID + " TEXT," + KEY_RECEIVERID + " TEXT,"
                + KEY_MY_ID + " TEXT," + KEY_FRIEND_ID + " TEXT," + KEY_FRIEND_USERNAME + " TEXT," + KEY_FRIEND_NAME + " TEXT,"
                + KEY_TEXT_OR_URI + " TEXT," + KEY_TYPE_ISIMAGE + " TEXT," + KEY_Time + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME_IN_MILLIS + " TEXT,"
                + KEY_TYPE_TRANSFER + " TEXT," + KEY_READ_STATUS + " TEXT)";
        System.out.println("Table----" + CREATE_CHAT_TABLE);
        db.execSQL(CREATE_CHAT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);

        // Create tables again
        onCreate(db);
    }

    // Adding new chat
    public void addChat(ChatDetail chatetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SENDERID, chatetail.getSenderId());
        values.put(KEY_RECEIVERID, chatetail.getReceiverId()); // Contact Name

        values.put(KEY_MY_ID, chatetail.getMyId()); // my Id
        values.put(KEY_FRIEND_ID, chatetail.getFriendId()); // Friend id
        values.put(KEY_FRIEND_USERNAME, chatetail.getFriendUserName()); // Friend User Name
        values.put(KEY_FRIEND_NAME, chatetail.getFriendName()); // Friend Name

        values.put(KEY_TEXT_OR_URI, chatetail.getMessage());
        values.put(KEY_TYPE_ISIMAGE, chatetail.getTypeIsImage());// Contact
        values.put(KEY_Time, chatetail.getTime());
        values.put(KEY_DATE, chatetail.getDate());
        values.put(KEY_TIME_IN_MILLIS, chatetail.getTimeInMillis());
        values.put(KEY_TYPE_TRANSFER, chatetail.getTransferType());
        values.put(KEY_READ_STATUS, chatetail.getReadStatus());


        // Inserting Row
        db.insert(TABLE_CHAT, null, values);
        db.close(); // Closing database connection
    }


    public ArrayList<ChatDetail> getChatList(String myUserId, String friendId) {
        //ArrayList<HashMap<String, String>> savedReminders = new ArrayList<>();
        ArrayList<ChatDetail> chatArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // String[] idArray = {fromSenderId, fromReceiverId};

        //    Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT, null);
        /*Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT + " WHERE (" + KEY_SENDERID + "=" + fromSenderId + " OR "
                + KEY_SENDERID + "=" + fromReceiverId + ") AND (" + KEY_RECEIVERID + "=" + fromSenderId + " OR " + KEY_RECEIVERID
                + "=" + fromReceiverId + ")", null);*/

        String query = "select * from " + TABLE_CHAT + " WHERE " + KEY_MY_ID + "=" + myUserId + " AND " + KEY_FRIEND_ID + "=" + friendId;
        Log.i("getChatList query", query);


        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Log.i("Cursor", cursor.toString());

        while (cursor.isAfterLast() == false) {

            long id = cursor.getLong(cursor.getColumnIndex(KEY_COLUMN_ID));
            String senderId = cursor.getString(cursor.getColumnIndex(KEY_SENDERID));
            String receiverId = cursor.getString(cursor.getColumnIndex(KEY_RECEIVERID));

            String myId = cursor.getString(cursor.getColumnIndex(KEY_MY_ID));
            String friedndId = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_ID));
            String friendUsername = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_USERNAME));
            String friendName = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_NAME));

            String textOrURI = cursor.getString(cursor.getColumnIndex(KEY_TEXT_OR_URI));
            String isImage = cursor.getString(cursor.getColumnIndex(KEY_TYPE_ISIMAGE));
            String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
            String time = cursor.getString(cursor.getColumnIndex(KEY_Time));
            String timeInMillis = cursor.getString(cursor.getColumnIndex(KEY_TIME_IN_MILLIS));
            String typrTransfer = cursor.getString(cursor.getColumnIndex(KEY_TYPE_TRANSFER));
            String readStatus = cursor.getString(cursor.getColumnIndex(KEY_READ_STATUS));

            chatArrayList.add(new ChatDetail(senderId, receiverId, myId, friedndId, friendUsername, friendName, textOrURI, isImage, time, date, timeInMillis, typrTransfer, readStatus));

            cursor.moveToNext();

        }

        db.close();

        return chatArrayList;
    }


    public ArrayList<ActiveChatUser> getUserList(String myId) {


        ArrayList<ActiveChatUser> activeUserArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // String[] idArray = {fromSenderId, fromReceiverId};

        String query ="select * from " + TABLE_CHAT + " where " + KEY_MY_ID + "=" + myId
                + " GROUP BY " + KEY_FRIEND_ID + " ORDER BY " + KEY_FRIEND_ID;

        Log.i("getUserList query",query);


        Cursor cursor = db.rawQuery(query
                , null);
         /*   Cursor cursor = db.rawQuery("select * from " + TABLE_CHAT + " WHERE (" + KEY_SENDERID + "=" + fromSenderId + " OR "
                    + KEY_SENDERID + "=" + fromReceiverId + ") AND (" + KEY_RECEIVERID + "=" + fromSenderId + " OR " + KEY_RECEIVERID
                    + "=" + fromReceiverId + ")", null);*/


        cursor.moveToFirst();

        Log.i("Cursor", cursor.toString());

        while (cursor.isAfterLast() == false) {

            ActiveChatUser activeChatUser = new ActiveChatUser();


            long id = cursor.getLong(cursor.getColumnIndex(KEY_COLUMN_ID));

            String friedndId = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_ID));
            String friendUsername = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_USERNAME));
            String friendName = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_NAME));

            activeChatUser.setId(friedndId);
            activeChatUser.setUserName(friendUsername);
            activeChatUser.setName(friendName);

            activeUserArrayList.add(activeChatUser);

            cursor.moveToNext();

        }

        db.close();

        return activeUserArrayList;


    }


    public String getTotalReadStatus(String myId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select count(" + KEY_READ_STATUS + ") from " + TABLE_CHAT + " where " + KEY_MY_ID + "=" + myId + " AND " + KEY_READ_STATUS + "=\"UNREAD\""
                + " GROUP BY " + KEY_READ_STATUS
                , null);

        String count = "";

        if (cursor.moveToFirst()) {

            int countVal = cursor.getInt(0);
            cursor.close();

            count = countVal + "";
        }

        return count + "";
    }

    public String getReadStatusForUser(String myId, String friendId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select count(" + KEY_READ_STATUS + ") from " + TABLE_CHAT + " where " + KEY_MY_ID + "=" + myId + " AND " + KEY_READ_STATUS + "=\"UNREAD\"" + " AND " + KEY_FRIEND_ID + "=" + friendId
                + " GROUP BY " + KEY_READ_STATUS
                , null);

        String count = "";

        if (cursor.moveToFirst()) {

            int countVal = cursor.getInt(0);
            cursor.close();

            count = countVal + "";
        }

        return count + "";
    }


    public void updateReadStatus(String myId, String friendId) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("UPDATE " + TABLE_CHAT + " SET " + KEY_READ_STATUS + "=\"READ\" WHERE " + KEY_FRIEND_ID + "=" + friendId
                + " AND " + KEY_MY_ID + "=" + myId, null);

        cursor.moveToFirst();
        cursor.close();

    }


    public void deleteChatHistory(String myId, String friendId) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_CHAT, KEY_MY_ID + "=? AND " + KEY_FRIEND_ID + "=?", new String[]{myId, friendId});
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     ****************** Offer ***********************

     // Adding new offer
     public void addoffer(OfferList offerdetail) {
     SQLiteDatabase db = this.getWritableDatabase();

     ContentValues values = new ContentValues();
     values.put(KEY_UNIQUEID, offerdetail.get_uniqueid());
     values.put(KEY_ID, offerdetail.get_offersid()); // Contact Name
     values.put(KEY_TITLE, offerdetail.get_offerstitle());
     values.put(KEY_MESSAGE, offerdetail.get_offersmessage());// Contact
     // Phone
     values.put(KEY_FROMDATE, offerdetail.get_offersfromdate());
     values.put(KEY_TODATE, offerdetail.get_offerstodate());
     values.put(KEY_IMAGE, offerdetail.get_rewardimage());

     // Inserting Row
     db.insert(TABLE_OFFER, null, values);
     db.close(); // Closing database connection
     }

     // Getting offer Count
     public int getofferCount() {
     String countQuery = "SELECT  * FROM " + TABLE_OFFER;
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery(countQuery, null);
     // cursor.close();

     // return count
     return cursor.getCount();
     }

     // Getting offer Count
     public int getuniqueidofferCount(String uniqueid) {
     String countQuery = "SELECT  * FROM " + TABLE_OFFER + " WHERE " + KEY_UNIQUEID + " ='" + uniqueid + "'";
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery(countQuery, null);
     // cursor.close();

     // return count
     return cursor.getCount();
     }

     // Getting All Offers
     public List<OfferList> getAllOffers() {
     List<OfferList> OfferList = new ArrayList<OfferList>();
     // Select All Query
     String selectQuery = "SELECT  * FROM " + TABLE_OFFER;

     SQLiteDatabase db = this.getWritableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);

     // looping through all rows and adding to list
     if (cursor.moveToFirst()) {
     do {
     OfferList offerdetail = new OfferList(cursor.getString(0), cursor.getString(1), cursor.getString(2),
     cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
     // Adding contact to list
     OfferList.add(offerdetail);
     } while (cursor.moveToNext());
     }

     // return contact list
     return OfferList;
     }

     // Getting All Offers
     public List<OfferList> getuniqueidOffers(String unigeid) {
     List<OfferList> OfferList = new ArrayList<OfferList>();
     // Select All Query
     // "select * from barzz_table where bar_id='" + barid
     // + "' and current_date= Date('" + current_date + "')";
     String selectQuery = "SELECT  * FROM " + TABLE_OFFER + " WHERE " + KEY_UNIQUEID + " ='" + unigeid + "'";
     // System.out.println("SELECT * FROM " + TABLE_OFFER + " WHERE
     // KEY_UNIQUEID='" + unigeid + "'");
     SQLiteDatabase db = this.getWritableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);

     // looping through all rows and adding to list
     if (cursor.moveToFirst()) {
     do {
     OfferList offerdetail = new OfferList(cursor.getString(0), cursor.getString(1), cursor.getString(2),
     cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
     // Adding contact to list
     OfferList.add(offerdetail);
     } while (cursor.moveToNext());
     }

     // return contact list
     return OfferList;
     }

     ************************** Beacon ***************

     // Adding new beacon
     public void addBeacon(String beaconid) {
     SQLiteDatabase db = this.getWritableDatabase();

     ContentValues values = new ContentValues();
     values.put(KEY_BEACONID, beaconid); // Contact Name

     // Inserting Row
     db.insert(TABLE_BEACON, null, values);
     db.close(); // Closing database connection
     }

     // Getting beacon Count
     public int getbeaconCount() {
     String countQuery = "SELECT  * FROM " + TABLE_BEACON;
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery(countQuery, null);
     // cursor.close();

     // return count
     return cursor.getCount();
     }

     // Getting All beacons
     public List<String> getAllBeacons() {
     List<String> beaconList = new ArrayList<>();
     // Select All Query
     String selectQuery = "SELECT  * FROM " + TABLE_BEACON;

     SQLiteDatabase db = this.getWritableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);

     // looping through all rows and adding to list
     if (cursor.moveToFirst()) {
     do {
     // Adding contact to list
     beaconList.add(cursor.getString(0));
     } while (cursor.moveToNext());
     }

     // return contact list
     return beaconList;
     }

     // Getting single offer
     public OfferList getbeaconoffer(String uniqueid) {
     SQLiteDatabase db = this.getReadableDatabase();

     Cursor cursor = db.query(TABLE_OFFER,
     new String[] { KEY_UNIQUEID, KEY_ID, KEY_TITLE, KEY_MESSAGE, KEY_FROMDATE, KEY_TODATE, KEY_IMAGE },
     KEY_ID + "=?", new String[] { String.valueOf(uniqueid) }, null, null, null, null);
     if (cursor != null)
     cursor.moveToFirst();

     OfferList offerlist = new OfferList(cursor.getString(0), cursor.getString(1), cursor.getString(2),
     cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
     System.out.println(
     "unique id:" + uniqueid + "id" + offerlist.get_offersid() + "Title" + offerlist.get_offerstitle()
     + "message" + offerlist.get_offersmessage() + "date" + offerlist.get_offersfromdate()
     + offerlist.get_offerstodate() + "image" + offerlist.get_rewardimage());
     // cursor.close();
     return offerlist;
     }

     // Getting single offer
     public OfferList getoffer(String uniqueid, String offerid) {
     SQLiteDatabase db = this.getReadableDatabase();

     Cursor cursor = db
     .query(TABLE_OFFER,
     new String[] { KEY_UNIQUEID, KEY_ID, KEY_TITLE, KEY_MESSAGE, KEY_FROMDATE, KEY_TODATE,
     KEY_IMAGE },
     KEY_UNIQUEID + "= ? AND " + KEY_ID + " = ?",
     new String[] { String.valueOf(uniqueid), String.valueOf(offerid) }, null, null, null, null);
     if (cursor != null)
     cursor.moveToFirst();

     OfferList offerlist = new OfferList(cursor.getString(0), cursor.getString(1), cursor.getString(2),
     cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
     System.out.println("unique id" + offerlist.get_uniqueid() + "id" + offerlist.get_offersid() + "Title"
     + offerlist.get_offerstitle() + "message" + offerlist.get_offersmessage() + "date"
     + offerlist.get_offersfromdate() + offerlist.get_offerstodate() + "image"
     + offerlist.get_rewardimage());
     // cursor.close();
     return offerlist;
     }

     ************************** Delete all table contents ***************
     public void deletetablecontents() {

     SQLiteDatabase db = this.getWritableDatabase();
     db.delete(TABLE_BEACON, null, null);
     db.delete(TABLE_OFFER, null, null);

     }

     * // Updating single contact public int updateContact(Contact contact) {
     * SQLiteDatabase db = this.getWritableDatabase();
     *
     * ContentValues values = new ContentValues(); values.put(KEY_NAME,
     * contact.getName()); values.put(KEY_PH_NO, contact.getPhoneNumber());
     *
     * // updating row return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
     * new String[] { String.valueOf(contact.getID()) }); }
     *
     * // Deleting single contact public void deleteContact(Contact contact) {
     * SQLiteDatabase db = this.getWritableDatabase(); db.delete(TABLE_CONTACTS,
     * KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
     * db.close(); }
     *
     *
     * // Getting contacts Count public int getContactsCount() { String
     * countQuery = "SELECT  * FROM " + TABLE_CONTACTS; SQLiteDatabase db =
     * this.getReadableDatabase(); Cursor cursor = db.rawQuery(countQuery,
     * null); cursor.close();
     *
     * // return count return cursor.getCount(); }
     *
     * }
     */
}