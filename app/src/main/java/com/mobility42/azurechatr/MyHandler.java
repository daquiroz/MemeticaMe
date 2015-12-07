package com.mobility42.azurechatr;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyHandler extends NotificationsHandler {

	public static final String DISPLAY_MESSAGE_ACTION = "displaymessage";
	public static final String EXTRA_MESSAGE = "message";
	public static final String EXTRA_USERNAME = "username";
	public static final String EXTRA_ID = "id";
	public static final String STATUS = "status";
	public static final String TIME_STAMP = "time";

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	Context ctx;

	@Override
	public void onReceive(Context context, Bundle bundle) {
		ctx = context;
		String nhMessage = bundle.getString("message");
		String nhUsername = bundle.getString("username");
		String nhid = bundle.getString("id");
		sendNotification(nhUsername + " - " + nhMessage);
		displayMessage(context, nhMessage, nhUsername, nhid);
	}

	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager)
				ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
				new Intent(ctx, ChatActivity.class), 0);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(ctx)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle("MemeticaMe")
						.setStyle(new NotificationCompat.BigTextStyle()
								.bigText(msg))
						.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	static void displayMessage(Context context, String message, String username, String id) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		intent.putExtra(EXTRA_USERNAME, username);
		intent.putExtra(EXTRA_ID,id);
		SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
		Date currentDate = new Date(System.currentTimeMillis());
		String time = formatter.format(currentDate);
		intent.putExtra(TIME_STAMP, time);
		intent.putExtra(STATUS, "sent");
		context.sendBroadcast(intent);
	}
}
