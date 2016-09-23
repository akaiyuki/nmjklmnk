package com.fasionparade.fasionparadeApp.Models.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Models.Adapters.ChatDetailListAdapter;
import com.fasionparade.fasionparadeApp.Functions.Database.DatabaseHandler;
import com.fasionparade.fasionparadeApp.Functions.Service.ResourceManager;
import com.fasionparade.fasionparadeApp.Functions.Service.WebserviceAssessor;
import com.fasionparade.fasionparadeApp.Functions.Core.AlertDialogManager;
import com.fasionparade.fasionparadeApp.Functions.Core.AppController;
import com.fasionparade.fasionparadeApp.Functions.Object.ChatDetail;
import com.fasionparade.fasionparadeApp.Functions.Service.ConnectionCheck;
import com.fasionparade.fasionparadeApp.Functions.Object.Flag;
import com.fasionparade.fasionparadeApp.Functions.Object.User;
import com.fasionparade.fasionparadeApp.Functions.Core.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ChatDetailActivity extends ActionBarActivity {

    Context context;
    RecyclerView chatDetailRecyclerView;
    EditText messageEditText;
    TextView sentTextView,nameTextView;
    ImageView attachmentIcon;
    ChatDetailListAdapter chatDeatilListAdapter;
    ArrayList<ChatDetail> list;
    Uri imageUrl = null;
    public static final int INTENT_REQUEST_GET_IMAGES = 13;

    ImageView backImageView;

    ConnectionCheck mConnectionCheck;
    AlertDialogManager alert = new AlertDialogManager();

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;



    String senderId = "";
    String receiverId = "";

    String userName="";
    String name ="";

    private String[] value,character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);


        User user = Utils.getUserFromPreference(ChatDetailActivity.this);
        senderId = user.id;

        Log.i("Sender Id", senderId);

        if (getIntent() != null) {

            if (getIntent().hasExtra("RECEIVER_ID"))
                receiverId = getIntent().getStringExtra("RECEIVER_ID");

            if (getIntent().hasExtra("USER_NAME"))
                userName = getIntent().getStringExtra("USER_NAME");


            if (getIntent().hasExtra("NAME"))
                name = getIntent().getStringExtra("NAME");


            Log.i("Receiver Id", receiverId);

        }


        context = this;
        chatDetailRecyclerView = (RecyclerView) findViewById(R.id.chatDetailRecyclerView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sentTextView = (TextView) findViewById(R.id.sentTextView);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(name);

        attachmentIcon = (ImageView) findViewById(R.id.attachmentIcon);

        backImageView=(ImageView)findViewById(R.id.backImageView);

        value = getResources().getStringArray(R.array.values);
        character = getResources().getStringArray(R.array.character);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatDetailActivity.this);
        chatDetailRecyclerView.setLayoutManager(linearLayoutManager);


        DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
        list = databaseHandler.getChatList(senderId, receiverId);
        Log.i("list size", list.size() + "");
//        list = new ArrayList<>();

        chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list,value,character);
        chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);
        try {
            chatDetailRecyclerView.scrollToPosition(chatDeatilListAdapter.getItemCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!messageEditText.getText().toString().equals("")) {

                   /* DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
                    databaseHandler.addChat(new ChatDetail("1", "2", messageEditText.getText().toString(), "0", "10:33", "10-08-2016", System.currentTimeMillis() + "", "SEND"));

                    // list.add(Uri.parse(messageEditText.getText().toString()));
                    list = databaseHandler.getChatList("", "");
                    Log.i("List Size", list.size() + "");


                    chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list);
                    chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);
                    // chatDeatilListAdapter.notifyDataSetChanged();
                    messageEditText.getText().clear();

                    try {
                        chatDetailRecyclerView.scrollToPosition(chatDeatilListAdapter.getItemCount() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                   String message= checkSpecialCharacter(messageEditText.getText().toString());

                    sendChatMessage(message);


                }


            }
        });

        messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(messageEditText.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        attachmentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        });


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        Log.i("Request Code", requestCode + "");
        Log.i("resuleCode Code", resuleCode + "");

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            Log.i("If", "Entered");


            Uri selectedImage = intent.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
           /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] prodata = byteArrayOutputStream.toByteArray();*/
            imageUrl = selectedImage;
            //     list.add(imageUrl);

            Log.i("Pic Path", picturePath);
            Log.i("Image uri", imageUrl.toString());

  /*          DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
            databaseHandler.addChat(new ChatDetail("1", "2", imageUrl.toString(), "1", "10:33", "10-08-2016", System.currentTimeMillis() + "", "SEND"));

            list = databaseHandler.getChatList("", "");

            chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list);
            chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);*/

//           chatDeatilListAdapter.notifyDataSetChanged();


            String url = ResourceManager.imageUpload();
            Log.i("Upload URL", url);
            new UploadChatImage().execute(url, picturePath, "0");


        } else {
            Log.i("Else", "Entered");
        }

    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    private void sendChatMessage(String message) {
        mConnectionCheck = new ConnectionCheck(context);
        if (!mConnectionCheck.isConnectingToInternet()) {
            alert.showAlertDialog(context, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        } else {

            String url = ResourceManager.sendChat();
            Log.e("deviceId-->", Flag.deviceId);

            // emailId=test@gmail.com&password=123
            url = url + "senderId=" + senderId + "&receiverId=" + receiverId + "&message=" + message + "&isImage=0";

            new SendChatMessage().execute(url);

        }
    }


    private class SendChatMessage extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Sending...");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.getData(params[0]);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                Log.i("Resoinse", result);

                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {

                    //Time
                    long milliSec = System.currentTimeMillis();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSec);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh.mm a");



                    DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
                    databaseHandler.addChat(new ChatDetail(senderId, receiverId,senderId,receiverId,userName,name, messageEditText.getText().toString(), "0", timeFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "SEND" ,"READ"));

                    // list.add(Uri.parse(messageEditText.getText().toString()));
                    list = databaseHandler.getChatList(senderId, receiverId);
                    Log.i("List Size", list.size() + "");


                    chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list,value,character);
                    chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);
                    // chatDeatilListAdapter.notifyDataSetChanged();
                    messageEditText.getText().clear();

                    try {
                        chatDetailRecyclerView.scrollToPosition(chatDeatilListAdapter.getItemCount() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(ChatDetailActivity.this,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (Exception e) {
                Toast.makeText(ChatDetailActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    private class UploadChatImage extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Sending...");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = WebserviceAssessor.postImageData(context, params[0], senderId, params[1], params[2]);

            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                if (result == null)
                    Log.i("Response", "NULL");

                Log.i("Response", result);

                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {

                    JSONArray imageDetailsJSONArray = jsonObject.getJSONArray("imageDetails");
                    JSONObject imageJSONObject = imageDetailsJSONArray.getJSONObject(0);

                    String imagesURL = imageJSONObject.getString("imageUrl");


                    String url = ResourceManager.sendChat();

                    // emailId=test@gmail.com&password=123
                    url = url + "senderId=" + senderId + "&receiverId=" + receiverId + "&message=" + imagesURL + "&isImage=1";

                    new SendChatImage().execute(url,imagesURL);


                 /*   DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
                    databaseHandler.addChat(new ChatDetail("1", "2", imageUrl.toString(), "1", "10:33", "10-08-2016", System.currentTimeMillis() + "", "SEND"));

                    list = databaseHandler.getChatList("", "");

                    chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list);
                    chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);*/



               /*     DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
                    databaseHandler.addChat(new ChatDetail(senderId, receiverId, messageEditText.getText().toString(), "0", "10:33", "10-08-2016", System.currentTimeMillis() + "", "SEND"));

                    // list.add(Uri.parse(messageEditText.getText().toString()));
                    list = databaseHandler.getChatList(senderId, receiverId);
                    Log.i("List Size", list.size() + "");


                    chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list);
                    chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);
                    // chatDeatilListAdapter.notifyDataSetChanged();
                    messageEditText.getText().clear();

                    try {
                        chatDetailRecyclerView.scrollToPosition(chatDeatilListAdapter.getItemCount() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/


                } else {
                    Toast.makeText(ChatDetailActivity.this,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (Exception e) {
                Toast.makeText(ChatDetailActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    private class SendChatImage extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;
   //     String imageId;


        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Sending...");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

     //       imageId=params[1];

            String result = WebserviceAssessor.getData(params[0]);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                Log.i("Resoinse", result);

                JSONObject jsonObject = new JSONObject(result);
                String errorCode;
                String message;
                errorCode = jsonObject.getString("errorCode");
                message = jsonObject.getString("message");
                if (errorCode.equals("200")) {

                    //Time
                    long milliSec = System.currentTimeMillis();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSec);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh.mm a");



                    DatabaseHandler databaseHandler = new DatabaseHandler(ChatDetailActivity.this);
                    databaseHandler.addChat(new ChatDetail(senderId, receiverId,senderId,receiverId,userName,name, imageUrl.toString(), "1", timeFormat.format(calendar.getTime()),dateFormat.format(calendar.getTime()), System.currentTimeMillis() + "", "SEND","READ"));

                    // list.add(Uri.parse(messageEditText.getText().toString()));
                    list = databaseHandler.getChatList(senderId, receiverId);
                    Log.i("List Size", list.size() + "");


                    chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, list,value,character);
                    chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);
                    // chatDeatilListAdapter.notifyDataSetChanged();
                    messageEditText.getText().clear();

                    try {
                        chatDetailRecyclerView.scrollToPosition(chatDeatilListAdapter.getItemCount() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(ChatDetailActivity.this,
                            message, Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (Exception e) {
                Toast.makeText(ChatDetailActivity.this,
                        "Something Wrong", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.cancel();
        }
    }


    public String checkSpecialCharacter(String message)
    {
       for(int i=0;i<value.length;i++)
       {
           if(message.contains(value[i]))
               message.replaceAll(value[i],character[i]);
       }

        return message;
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((AppController)getApplicationContext()).setCurrentActivity(this);

        DatabaseHandler databaseHandler=new DatabaseHandler(ChatDetailActivity.this);
        databaseHandler.updateReadStatus(senderId,receiverId);

//        updateReadStatus


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {
        clearReferences();
        receiverId="";
        super.onDestroy();

    }

    private void clearReferences(){
        Activity currActivity = ((AppController)getApplicationContext()).getCurrentActivity();
        if (this.equals(currActivity))
            ((AppController)getApplicationContext()).setCurrentActivity(null);
    }



    public void updateData(final String friendId){

        Log.i("ChatDet UpdateData","Called");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                ArrayList<ChatDetail> chatDetailArrayList = databaseHandler.getChatList(senderId, friendId);
                String[] value = getResources().getStringArray(R.array.values);
                String[] character = getResources().getStringArray(R.array.character);

                chatDeatilListAdapter = new ChatDetailListAdapter(ChatDetailActivity.this, chatDetailArrayList,value,character);
                chatDetailRecyclerView.setAdapter(chatDeatilListAdapter);

                try {
                    chatDetailRecyclerView.scrollToPosition(chatDeatilListAdapter.getItemCount() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }



}
