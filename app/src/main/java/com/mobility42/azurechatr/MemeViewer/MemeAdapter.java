package com.mobility42.azurechatr.MemeViewer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobility42.azurechatr.FeedChat;
import com.mobility42.azurechatr.R;

import java.util.ArrayList;

/**
 * Created by Francesca on 11/10/2015.
 */
public class MemeAdapter extends BaseAdapter {

    private ArrayList<FeedMeme> items;
    private final Context context;

    // the context is needed to inflate views in getView()
    public MemeAdapter(Context context,ArrayList<FeedMeme> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public FeedMeme getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final FeedMeme currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.feed_meme, parent, false);
        }

        final ImageView meme1 = (ImageView) row.findViewById(R.id.feedmeme_leftmeme);
        final ImageView meme2 = (ImageView) row.findViewById(R.id.feedmeme_rightmeme);

        if(currentItem.getMeme1() != null) {
            meme1.setImageDrawable(currentItem.getMeme1());
            meme1.setTag(currentItem.getMeme1());
        }
        if(currentItem.getMeme2() != null) {
            meme2.setImageDrawable(currentItem.getMeme2());
            meme2.setTag(currentItem.getMeme2());
        }
        return row;
    }

    public ArrayList<FeedMeme> getItems(){
        return items;
    }
}
