package com.fasionparade.fasionparadeApp.Models.Adapters;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.fasionparade.fasionparadeApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 8/8/16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    Activity mContext;
    private ArrayList<String> productImage,productType;

    public ViewPagerAdapter(Activity mContext, ArrayList<String> productImage,ArrayList<String> productType) {
        this.mContext = mContext;
        this.productImage = productImage;
        this.productType = productType;
    }

    @Override
    public int getCount() {
        return productImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.badse_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        final VideoView videoView = (VideoView) itemView.findViewById(R.id.videoView);
        if(productType.get(position).equals("0")) {
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Picasso.with(mContext)
                    .load(productImage.get(position))
                    .placeholder(R.drawable.no_image)
                    .into(imageView);
        }
        else if(productType.get(position).equals("1"))
        {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            Display display = mContext.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();

            videoView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

            if (productImage.get(position) != null) {
                videoView.setVideoURI(Uri.parse(productImage.get(position)));
                videoView.requestFocus();
            }

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (productImage.get(position) != null) {
                        Uri video = Uri.parse(productImage.get(position));
                        videoView.setVideoURI(video);
                        videoView.requestFocus();
                        videoView.start();
                    }

                }
            });
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}