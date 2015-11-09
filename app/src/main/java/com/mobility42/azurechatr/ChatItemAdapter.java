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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class ChatItemAdapter extends ArrayAdapter<FeedChat> {

	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;

	public ChatItemAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
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
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
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
			if (currentItem.getStatus() == "waiting") {
				status.setImageResource(R.drawable.clock);
			} else if (currentItem.getStatus()== "sending") {
				status.setImageResource(R.drawable.checkmark);
			} else if (currentItem.getStatus() == "sent") {
				status.setImageResource(R.drawable.checkmark);
				status.setColorFilter(Color.parseColor("0x18F466"),PorterDuff.Mode.SRC_IN);

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

}
