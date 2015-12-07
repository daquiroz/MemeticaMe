package com.mobility42.azurechatr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
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
public class ChatAdapter extends BaseAdapter {

    public ArrayList<Chat> items;

    public final Context context;


    public ChatAdapter(Context context, ArrayList<Chat> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Chat getItem(int position) {return items.get(position);}

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

        final Chat currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.feed_chat, parent, false);
        }

        row.setTag(currentItem);
        final TextView nombreChat = (TextView) row.findViewById(R.id.nombrechat);
        ImageView imagen = (ImageView) row.findViewById(R.id.image);
        TextView creado = (TextView) row.findViewById(R.id.message);


        if (currentItem.getNamechat()!= null) {
            nombreChat.setText(currentItem.getNamechat());
            imagen.setImageResource(R.drawable.users);
            creado.setText("Creado por: " + currentItem.getIdcreador());
        }
        else {
            //String aux = BuscarNombre(currentItem.getIdcontact());
            imagen.setImageResource(R.drawable.user);
            nombreChat.setText(currentItem.getIdcontact());
            creado.setText("");

        }


        return row;
    }

//    private String BuscarNombre(String idcontact) {
//
//        DB db = new DB();
//        for (Contact c : db.listaC)
//        {
//            if (c.getId().equals(idcontact))
//                return c.getName();
//        }
//        return null;
//    }

}
