package com.mobility42.azurechatr;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class ContactSelectAdapter extends BaseAdapter {

    public ArrayList<Contact> items;

    public final Context context;


    public ContactSelectAdapter(Context context, ArrayList<Contact> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Contact getItem(int position) {return items.get(position);}

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

        final Contact currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.contact_select_item, parent, false);
        }

        row.setTag(currentItem);
        final CheckBox nombreContacto = (CheckBox) row.findViewById(R.id.checkcontacto);

        //final TextView primeraLetra = (TextView) row.findViewById(R.id.foto);


        nombreContacto.setText(currentItem.getName());



        return row;
    }

}
