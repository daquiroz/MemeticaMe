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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class ContactAdapter extends BaseAdapter {

    public ArrayList<Contact> items;

    public final Context context;


    public ContactAdapter(Context context, ArrayList<Contact> items) {
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
            row = inflater.inflate(R.layout.contacts_list_item, parent, false);
        }

        row.setTag(currentItem);
        final TextView nombreContacto = (TextView) row.findViewById(R.id.nombrecontacto);

        final TextView primeraLetra = (TextView) row.findViewById(R.id.foto);


        nombreContacto.setText(currentItem.getName());

        try {
            String aux = currentItem.getName();
            String[] primera = aux.split("");
            primeraLetra.setText(primera[1]+primera[2]);
        }catch (Exception e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        return row;
    }

}
