package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasionparade.fasionparadeApp.R;
import com.fasionparade.fasionparadeApp.Functions.Object.ChatDetail;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 4/8/16.
 */
public class ChatDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Activity activity;

    static OnItemClickListener mItemClickListener;

    ArrayList<ChatDetail> chatArrayList;
    String[] values,character;

    public ChatDetailListAdapter(Activity activity
            , ArrayList<ChatDetail> chatArrayList,String[] values,String[] character

    ) {
        this.chatArrayList = chatArrayList;
        this.activity = activity;
        this.values=values;
        this.character=character;

        //   imageLoader= VolleySingleton.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_chatdetail_list_itm, parent, false);

        VHItem dataObjectHolder = new VHItem(view);

        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHItem) {

            VHItem vhItemHolder = (VHItem) holder;

            System.out.println("image-------"+chatArrayList.get(position).getMessage());

            if (chatArrayList.get(position).getTransferType().equals("RECEIVE")) {
                vhItemHolder.chatDetailLayout.setGravity(Gravity.RIGHT);
                vhItemHolder.messageLayout.setBackgroundResource(R.drawable.bubble_b);
              /*  if (contactList.get(position).toString().contains("://")) {
                    vhItemHolder.chatMessage.setVisibility(View.GONE);
                    vhItemHolder.imageView.setVisibility(View.VISIBLE);
                    vhItemHolder.imageView.setImageURI(contactList.get(position));
                } else {
                    vhItemHolder.chatMessage.setVisibility(View.VISIBLE);
                    vhItemHolder.imageView.setVisibility(View.GONE);
                    vhItemHolder.chatMessage.setBackgroundResource(R.drawable.bg_blue);
                    vhItemHolder.chatMessage.setTextColor(Color.parseColor("#FFFFFF"));
                    vhItemHolder.chatMessage.setText(contactList.get(position).toString());
                }*/




                if(chatArrayList.get(position).getTypeIsImage().equals("1")){

                    vhItemHolder.imageView.setVisibility(View.VISIBLE);
                    vhItemHolder.chatMessage.setVisibility(View.GONE);

                    Log.i("Rec Img", "Entered");

                /*    Uri imageUri=Uri.parse(chatArrayList.get(position).getMessage());
                    InputStream imageStream = null;
                    try {
                        imageStream = activity.getContentResolver().openInputStream(
                                imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    //          ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //            Bitmap thumb = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(chatArrayList.get(position).getMessage()), 220, 220, false);
                    vhItemHolder.imageView.setImageBitmap(bmp);*/

                    try {
                     //   Picasso.with(activity).load(chatArrayList.get(position).getMessage()).into(vhItemHolder.imageView);

                        Log.i("message",chatArrayList.get(position).getMessage());
                        Log.i("message html", Html.fromHtml(chatArrayList.get(position).getMessage()).toString());
                        Log.i("message url", URLDecoder.decode(chatArrayList.get(position).getMessage(), "UTF-8"));

                        Picasso.with(activity)
                                .load(URLDecoder.decode(chatArrayList.get(position).getMessage(), "UTF-8"))
                              //  .error(R.drawable.product_detail_no_image_org)
                                        //       .placeholder(R.drawable.img_pizza2)
                                .into(vhItemHolder.imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                        Log.i("Image","Success");
                                    }

                                    @Override
                                    public void onError() {
                                        //  imageView.setImageResource(R.drawable.product_detail_no_image_org);

                                        Log.i("Image","Error");
                                    }
                                });

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }else{

                    Log.i("Rec Text", "Entered");


                    vhItemHolder.imageView.setVisibility(View.GONE);
                    vhItemHolder.chatMessage.setVisibility(View.VISIBLE);

                    checkSpecialCharacter(chatArrayList.get(position).getMessage(), vhItemHolder.chatMessage);
                }



            } else {
                vhItemHolder.chatDetailLayout.setGravity(Gravity.LEFT);
                vhItemHolder.messageLayout.setBackgroundResource(R.drawable.bubble_a);
               /* if (contactList.get(position).toString().contains("//")) {
                    vhItemHolder.chatMessage.setVisibility(View.GONE);
                    vhItemHolder.imageView.setVisibility(View.VISIBLE);
                    vhItemHolder.imageView.setImageURI(contactList.get(position));
                } else {
                    vhItemHolder.chatMessage.setVisibility(View.VISIBLE);
                    vhItemHolder.imageView.setVisibility(View.GONE);
                    vhItemHolder.chatMessage.setBackgroundResource(R.drawable.bg_white);
                    vhItemHolder.chatMessage.setTextColor(Color.parseColor("#000000"));
                    vhItemHolder.chatMessage.setText(contactList.get(position).toString());
                }*/


                if(chatArrayList.get(position).getTypeIsImage().equals("1")){

                    vhItemHolder.imageView.setVisibility(View.VISIBLE);
                    vhItemHolder.chatMessage.setVisibility(View.GONE);

                    Uri imageUri=Uri.parse(chatArrayList.get(position).getMessage());
                    InputStream imageStream = null;
                    try {
                        imageStream = activity.getContentResolver().openInputStream(
                                imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    //          ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //            Bitmap thumb = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(chatArrayList.get(position).getMessage()), 220, 220, false);
                    vhItemHolder.imageView.setImageBitmap(bmp);


                }else{

                    vhItemHolder.imageView.setVisibility(View.GONE);
                    vhItemHolder.chatMessage.setVisibility(View.VISIBLE);

                    vhItemHolder.chatMessage.setText(chatArrayList.get(position).getMessage());
                }

            }

            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(chatArrayList.get(position).getTimeInMillis()));

            SimpleDateFormat formatter = new SimpleDateFormat("hh.mm a");
//            vhItemHolder.chatTime.setText(formatter.format(calendar.getTime()));
            vhItemHolder.chatTime.setText(chatArrayList.get(position).getTime());


        /*    if(chatArrayList.get(position).getType().equals("1")){

                vhItemHolder.imageView.setVisibility(View.VISIBLE);
                vhItemHolder.chatMessage.setVisibility(View.GONE);

                Uri imageUri=Uri.parse(chatArrayList.get(position).getMessage());
                InputStream imageStream = null;
                try {
                    imageStream = activity.getContentResolver().openInputStream(
                            imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                Bitmap bmp = BitmapFactory.decodeStream(imageStream);
      //          ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //            Bitmap thumb = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(chatArrayList.get(position).getMessage()), 220, 220, false);
                vhItemHolder.imageView.setImageBitmap(bmp);


            }else{

                vhItemHolder.imageView.setVisibility(View.GONE);
                vhItemHolder.chatMessage.setVisibility(View.VISIBLE);

                vhItemHolder.chatMessage.setText(chatArrayList.get(position).getMessage());
            }*/


        }

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public static class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout chatDetailLayout,messageLayout;
        TextView chatMessage, chatTime;
        ImageView imageView;

        public VHItem(View itemView) {
            super(itemView);

            chatDetailLayout = (RelativeLayout) itemView.findViewById(R.id.chatDetailLayout);
            messageLayout = (RelativeLayout) itemView.findViewById(R.id.messageLayout);
            chatMessage = (TextView) itemView.findViewById(R.id.chatMessage);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            chatTime = (TextView) itemView.findViewById(R.id.chatTime);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public void checkSpecialCharacter(String message,TextView view)
    {
        String msg=message;
        for(int i=0;i<values.length;i++)
        {
            if(message.contains(values[i])) {
                msg = msg.replaceAll(values[i], character[i]);

            }
        }

        view.setText(msg);
    }
}
