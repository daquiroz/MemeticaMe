package com.mobility42.azurechatr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class CanalAdapter extends BaseAdapter {

    private ArrayList<Meme> items;

    private final Context context;

    // the context is needed to inflate views in getView()
    public CanalAdapter(Context context, ArrayList<Meme> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public Meme getItem(int position) {
        return items.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * Returns the view for a specific item on the list
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Meme currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.feed_canal, parent, false);
        }

        row.setTag(currentItem);
        final TextView nombreCanal = (TextView) row.findViewById(R.id.nombreCanal);

        final TextView categoria = (TextView) row.findViewById(R.id.categoriacanal);

        categoria.setText(currentItem.getCategoria());

        nombreCanal.setText(currentItem.getNombrecanal());

        return row;
    }

}