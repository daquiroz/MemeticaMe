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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class MemeChannelAdapter extends BaseAdapter {

	private ArrayList<Meme> items;

	private final Context context;

	// the context is needed to inflate views in getView()
	public MemeChannelAdapter(Context context, ArrayList<FeedChat> items) {
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
	public FeedChat getItem(int position) {
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

		final FeedChat currentItem = getItem(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.bubble, parent, false);
		}

		row.setTag(currentItem);
		final TextView userName = (TextView) row.findViewById(R.id.textUsername);

		final TextView text = (TextView) row.findViewById(R.id.textChatItem);

		final TextView time = (TextView) row.findViewById(R.id.time);

		time.setText(currentItem.getTimeStamp());

		userName.setText(currentItem.getUserName());

		text.setText(currentItem.getText());
		text.setEnabled(true);
		//time.setText(currentItem.getTimeStamp().toString());
		userName.setVisibility(userName.GONE);
		final RelativeLayout bubble = (RelativeLayout) row.findViewById(R.id.bubble);

		if(!currentItem.isTheDeviceUser())
		{
			ImageView status = (ImageView)row.findViewById(R.id.status);
			status.setVisibility(status.GONE);
			((LinearLayout)row).setGravity(Gravity.LEFT);
			//username.Visibility = ViewStates.Visible;
			bubble.setBackgroundResource(R.drawable.bubble_yellow);
			if(position>1)

				if(!getItem (position- 1).isTheDeviceUser()){

					//username.Visibility = ViewStates.Gone;
					bubble.setBackgroundResource(R.drawable.bubble_orange);

				}
		}
		else
		{
			ImageView status = (ImageView)row.findViewById(R.id.status);
			status.setVisibility(status.VISIBLE);
			if (currentItem.getStatus().equals("waiting")) {
				status.setImageResource(R.drawable.clock);
				status.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
			} else if (currentItem.getStatus().equals("sending")){
				status.setImageResource(R.drawable.checkmark);
				status.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
			} else if (currentItem.getStatus().equals ("sent")) {
				status.setImageResource(R.drawable.checkmark);
				status.setColorFilter(Color.GREEN,PorterDuff.Mode.SRC_ATOP);

			}
			((LinearLayout)row).setGravity(Gravity.RIGHT);

			//username.Visibility = ViewStates.Visible;

			bubble.setBackgroundResource(R.drawable.bubble_green);

			if(position>1)

				if(getItem (position- 1).isTheDeviceUser()){

					//username.Visibility = ViewStates.Gone;
					bubble.setBackgroundResource(R.drawable.bubble_blue);

				}
		}

		return row;
	}

	public void updateStatus() {

		for (int i = 0; i < getCount(); i++) {
			if (items.get(i).getStatus().equals("sending")) {
				items.get(i).setStatus("sent");
			}
		}





	}

	public void updateToSending() {

		for (int i = 0; i < getCount(); i++) {
			if (items.get(i).getStatus().equals("waiting")) {
				items.get(i).setStatus("sending");
			}
		}




	}

}