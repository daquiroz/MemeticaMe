package com.mobility42.azurechatr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class MemeChannelAdapter extends BaseAdapter {

	private ArrayList<Meme> items;

	private final Context context;

	// the context is needed to inflate views in getView()
	public MemeChannelAdapter(Context context, ArrayList<Meme> items) {
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
			row = inflater.inflate(R.layout.feed_meme_channel, parent, false);
		}

		row.setTag(currentItem);

		final ImageView meme = (ImageView) row.findViewById(R.id.meme_photo);
		final TextView categoria = (TextView) row.findViewById(R.id.categoria);

		final TextView ranking = (TextView) row.findViewById(R.id.ranking);

		final TextView etiquetas = (TextView) row.findViewById(R.id.etiquetas);

		if(exists(currentItem.getUrl()))
		{
			UrlImageViewHelper.setUrlDrawable(meme, currentItem.getUrl(), R.drawable.photo);

		}
		else {
			File imgFile = new File(currentItem.getImagePath());

			if (imgFile.exists()) {

				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


				meme.setImageBitmap(myBitmap);
				meme.setVisibility(View.VISIBLE);


			}
		}

		categoria.setText(currentItem.getCategoria());

		ranking.setText(currentItem.getRanking());

		etiquetas.setText(currentItem.getEtiquetas());
		return row;
	}

	public static boolean exists(String URLName){
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}