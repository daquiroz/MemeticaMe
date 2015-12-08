package com.mobility42.azurechatr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.mobility42.azurechatr.MemeViewer.MemeViewerActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {

	public static final String DISPLAY_MESSAGE_ACTION = "displaymessage";
	public static final String EXTRA_USERNAME = "username";
	public static final String EXTRA_MESSAGE = "message";
	public static final String TIME_STAMP = "time";
	public static final String ID = "id";
	// Secret IDs, Azure App Keys and Connection Strings NOT to be shared with the public
	private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
	private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
	private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
	private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
	private String GCMPUSH_SENDER_ID = "737194093634";
	public String miId = DB.miId;
	private String idchat;

	private GoogleCloudMessaging gcm;
	private NotificationHub hub;

	private MobileServiceClient mClient;

	private MobileServiceTable<ChatItem> mChatTable;

	/**
	 * Adapter to sync the items list with the view
	 */
	private ChatItemAdapter mAdapter;

	/**
	 * EditText containing the "New Chat" text
	 */
	private EditText mTextNewChat;

	/**
	 * Progress spinner to use for table operations
	 */
	private ProgressBar mProgressBar;
	private LinearLayout attachment;
	private ListView listViewChat;
	private ArrayList<FeedChat> lista;
	/**
	 * Initializes the activity
	 */

	int Option;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		try
		{
			Option =Integer.parseInt(getIntent().getStringExtra("Option"));
			if(Option==0)
			{
				addItemImage();
			}
		}
		catch(Exception e){}


		/*mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
		// Initialize the progress bar
		mProgressBar.setVisibility(ProgressBar.GONE);*/
		attachment = (LinearLayout) findViewById(R.id.attachment);

		attachment.setVisibility(attachment.GONE);

		try
		{
			idchat = getIntent().getExtras().getString("idchat");

		}
		catch(Exception excepcion)
		{
			idchat = "000000";
		}
		try {
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			setmClient(new MobileServiceClient(
					AZUREMOBILESERVICES_URI,
					AZUREMOBILESERVICES_APPKEY,
					this).withFilter(new ProgressFilter()));

			// Get the Mobile Service Table instance to use
			setmChatTable(getmClient().getTable(ChatItem.class))
			;

			lista = new ArrayList<FeedChat>();

			mTextNewChat = (EditText) findViewById(R.id.textNewChat);

			// Create an adapter to bind the items with the view
			mAdapter = new ChatItemAdapter(this,lista);
			listViewChat = (ListView) findViewById(R.id.listViewChat);
			listViewChat.setAdapter(mAdapter);

			// Load the items from the Mobile Service
			refreshItemsFromTable();

			NotificationsManager.handleNotifications(this, GCMPUSH_SENDER_ID, MyHandler.class);

			gcm = GoogleCloudMessaging.getInstance(this);

			String connectionString = AZUREPUSHNOTIFHUB_CNXSTRING;
			hub = new NotificationHub(AZUREPUSHNOTIFHUB_NAME, connectionString, this);
			registerWithNotificationHubs();

		} catch (MalformedURLException e) {
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}

		ImageButton boton = (ImageButton)findViewById(R.id.meme);
		boton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				Intent i = new Intent(ChatActivity.this, MemeViewerActivity.class);
				i.putExtra("idchat", idchat);
				i.putExtra("modo", "Normal");
				startActivityForResult(i, 10);

			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 10) {
			if(resultCode == RESULT_OK){
				String path =data.getStringExtra("path");
				Toast.makeText(this,path,Toast.LENGTH_LONG).show();
				final ChatItem item = new ChatItem();

				// This is temporary until we add authentication to the Android version
				item.setUserName(miId);

				item.setText("IMAGE-1234,"+"https://memeticamestorage.blob.core.windows.net/photos-165097224/IMG_d0dafe0c0eff42349e1f11a50e49443f.jpg");

				item.setStatus("waiting");
				item.setImagePath(path);

				item.setmIdChat(idchat);

				Date currentDate = new Date(System.currentTimeMillis());
				item.setTimeStamp(currentDate);
				FeedChat fc = new FeedChat(item.getText(),item.getUserName(),item.getId(),true);
				SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
				fc.setImagePath(path);
				fc.setTimeStamp(formatter.format(item.getTimeStamp()));
				fc.setStatus("waiting");
				lista.add(fc);
				listViewChat.setSelection(mAdapter.getCount() - 1);
				getmChatTable().insert(item, new TableOperationCallback<ChatItem>() {

					public void onCompleted(ChatItem entity, Exception exception, ServiceFilterResponse response) {

						if (exception == null) {
							mAdapter.updateToSending();
							mAdapter.notifyDataSetChanged();
							item.setStatus("sending");
							updateItem(item);
						} else {
							createAndShowDialog(exception, "Error");
						}

					}
				});

			}
		}
	}


	@SuppressWarnings("unchecked")
	private void registerWithNotificationHubs() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				try {
					String regid = gcm.register(GCMPUSH_SENDER_ID);
					hub.register(regid);
				} catch (Exception e) {
					return e;
				}
				return null;
			}
		}.execute(null, null, null);
	}

	/**
	 * Initializes the activity menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Select an option from the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_refresh) {
			refreshItemsFromTable();
		}

		return true;
	}

	/**
	 * Add a new item
	 *
	 * @param view
	 *            The view that originated the call
	 */

	public void  browseMedia(View view)
	{
		Intent intent = new Intent(ChatActivity.this,MediaBrowser.class);
		intent.putExtra("idchat",idchat);
		startActivity(intent);
	}

	public void startBlob(View view) {
		attachment.setVisibility(attachment.VISIBLE);
	}
	public void attach(View view) throws IOException {
		String name =getResources().getResourceName(view.getId());
		String segments[] = name.split("/");
		name = segments[segments.length - 1].toString();
		if(name.equals("pictureButton"))
		{
			Intent intent = new Intent(ChatActivity.this,BlobActivity.class);
			intent.putExtra("Option","0");
<<<<<<< HEAD
			intent.putExtra("idchat", idchat);
=======
			intent.putExtra("idchat",idchat);
>>>>>>> FelipeBranch
			startActivity(intent);

		}else if (name.equals("recordButton"))
		{

			Intent intent = new Intent(ChatActivity.this,Recorder.class);
			intent.putExtra("Option", "1");
<<<<<<< HEAD
			intent.putExtra("idchat", idchat);

=======
			intent.putExtra("idchat",idchat);
>>>>>>> FelipeBranch
			startActivity(intent);

		}else
		{
			Intent intent = new Intent(ChatActivity.this,BlobActivity.class);
			intent.putExtra("Option", "2");
<<<<<<< HEAD
			intent.putExtra("idchat", idchat);

=======
			intent.putExtra("idchat",idchat);
>>>>>>> FelipeBranch
			startActivity(intent);

		}
	}




	public void addItem(View view) {
		if (getmClient() == null) {
			return;
		}

		// Create a new item
		final ChatItem item = new ChatItem();

		item.setText(mTextNewChat.getText().toString());
		// This is temporary until we add authentication to the Android version
		item.setUserName(miId);

		item.setStatus("waiting");

		item.setmIdChat(idchat);

		Date currentDate = new Date(System.currentTimeMillis());
		item.setTimeStamp(currentDate);

		FeedChat fc = new FeedChat(item.getText(),item.getUserName(),item.getId(),true);
		SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
		fc.setTimeStamp(formatter.format(item.getTimeStamp()));
		fc.setStatus("waiting");
		lista.add(fc);
		mAdapter.notifyDataSetChanged();
		listViewChat.setSelection(mAdapter.getCount() - 1);

		// Insert the new item
		getmChatTable().insert(item, new TableOperationCallback<ChatItem>() {

			public void onCompleted(ChatItem entity, Exception exception, ServiceFilterResponse response) {

				if (exception == null) {
					mAdapter.updateToSending();
					mAdapter.notifyDataSetChanged();
					item.setStatus("sending");
					updateItem(item);
				} else {
					createAndShowDialog(exception, "Error");
				}

			}
		});

		mTextNewChat.setText("");
	}

	/**
	 * Refresh the list with the items in the Mobile Service Table
	 */
	private void refreshItemsFromTable() {

		// Get all the chat items and add them in the adapter
		getmChatTable().where().top(1000).execute(new TableQueryCallback<ChatItem>() {

			public void onCompleted(List<ChatItem> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					lista.clear();

					for (ChatItem item : result) {
						if (item.getmIdChat().equals(idchat))
						{
						FeedChat fc = new FeedChat(item.getText(), item.getUserName(), item.getId(), true);
						SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

						String time = formatter.format(item.getTimeStamp());
						fc.setImagePath(item.getImagePath());
						fc.setTimeStamp(time);
						fc.setStatus(item.getStatus());
						if (!item.getUserName().equals(miId)) {
							fc.setIsTheDeviceUser(false);
						}

						lista.add(fc);}
					}

					mAdapter.notifyDataSetChanged();
					listViewChat.setSelection(mAdapter.getCount() - 1);

				} else {
					createAndShowDialog(exception, "Error");
				}
			}
		});
	}

	/**
	 * Creates a dialog and shows it
	 *
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	/**
	 * Creates a dialog and shows it
	 *
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}

	/**
	 * Mobile Service Client reference
	 */
	public MobileServiceClient getmClient() {
		return mClient;
	}

	/**
	 * Mobile Service Table used to access data
	 */
	public MobileServiceTable<ChatItem> getmChatTable() {
		return mChatTable;
	}

	public void setmChatTable(MobileServiceTable<ChatItem> mChatTable) {
		this.mChatTable = mChatTable;
	}

	public void setmClient(MobileServiceClient mClient) {
		this.mClient = mClient;
	}

	private class ProgressFilter implements ServiceFilter {

		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
								  final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
				}
			});

			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {

				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
						}
					});

					if (responseCallback != null)  responseCallback.onResponse(response, exception);
				}
			});
		}
	}


	public void addItemImage() {
		if (mClient == null) {
			return;
		}


		// Create a new item
		final ChatItem item = new ChatItem();

		item.setText("Tu imágen se ha enviado");
		// This is temporary until we add authentication to the Android version
		item.setUserName("Felipe");

		item.setStatus("waiting");

		Date currentDate = new Date(System.currentTimeMillis());
		item.setTimeStamp(currentDate);

		// Insert the new item
		mChatTable.insert(item, new TableOperationCallback<ChatItem>() {

			public void onCompleted(ChatItem entity, Exception exception, ServiceFilterResponse response) {

				if (exception == null) {
					FeedChat fc = new FeedChat(entity.getText(),entity.getUserName(),entity.getId(),true);
					SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
					item.setStatus("sending");
					String time = formatter.format(item.getTimeStamp());
					fc.setStatus("sending");
					fc.setTimeStamp(time);
					//mAdapter.add(fc);
					mAdapter.notifyDataSetChanged();
					listViewChat.setSelection(mAdapter.getCount() - 1);
				} else {
					createAndShowDialog(exception, "Error");
				}

			}
		});

		mTextNewChat.setText("");
	}


	private final BroadcastReceiver mHandleMessageReceiver =
			new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
					String newUsername = intent.getExtras().getString(EXTRA_USERNAME);
					String newTimeStamp = intent.getExtras().getString(TIME_STAMP);
					String newId = intent.getExtras().getString(ID);
					//String newStatus = intent.getExtras().getString(STATUS);
					ChatItem item = new ChatItem();

					item.setText(newMessage);
					item.setUserName(newUsername);
					updateItemToSent(newId);

					if(!item.getUserName().equals(miId)) {
						FeedChat fc = new FeedChat(item.getText(), item.getUserName(), item.getId(), false);
						fc.setTimeStamp(newTimeStamp);
						fc.setStatus("sent");
						lista.add(fc);
						mAdapter.notifyDataSetChanged();
						listViewChat.setSelection(mAdapter.getCount() - 1);

					}
					else
					{
						mAdapter.updateStatus();
						mAdapter.notifyDataSetChanged();
					}
				}

			};

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(DISPLAY_MESSAGE_ACTION);
		this.registerReceiver(mHandleMessageReceiver, filter);
	}

	@Override
	public void onPause() {
		super.onPause();
		this.unregisterReceiver(mHandleMessageReceiver);
	}

	private void updateItem(final ChatItem item) {

		getmChatTable().update(item, new TableOperationCallback<ChatItem>() {

			public void onCompleted(ChatItem entity, Exception exception, ServiceFilterResponse response) {

				if (exception == null) {
				} else {
					createAndShowDialog(exception, "Error");
				}
			}
		});
	}
	private void updateItemToSent(final String id) {

		getmChatTable().lookUp(id, new TableOperationCallback<ChatItem>() {

			public void onCompleted(ChatItem entity, Exception exception, ServiceFilterResponse response) {

				if (exception == null) {
					entity.setStatus("sent");
					updateItem(entity);
				} else {
					createAndShowDialog(exception, "Error");
				}


			}
		});
	}

}