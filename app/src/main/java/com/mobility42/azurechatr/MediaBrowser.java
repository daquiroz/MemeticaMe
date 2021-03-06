package com.mobility42.azurechatr;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.File;
import java.util.List;

public class MediaBrowser extends Activity {

    //the images to display
    Integer[] imageIDs = {

            R.drawable.ic_circulo,
            R.drawable.back,R.drawable.clip,R.drawable.clock,R.drawable.checkmark,R.drawable.ic_launcher

    };
    File filesDir;
    List<String> photoUris;
    String idchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesDir = this.getCacheDir();
        final ImageAdapter IA =new ImageAdapter(this);
        idchat =getIntent().getStringExtra("idchat");

        // Note that Gallery view is deprecated in Android 4.1---

        final DownloadPictureBlob DPB = new DownloadPictureBlob(this,idchat);
        DPB.execute();
        Thread t = new Thread(new Runnable() {
            public void run()
            {
                while (DPB.getfinished()==false)
                {

                }
                MediaBrowser.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("UI thread", "I am the UI thread");
                        setContentView(R.layout.activity_media_browser);
                        Gallery gallery = (Gallery) findViewById(R.id.gallery);
                        if (DPB.getBlobsCount() != 0)
                        {
                            TextView txt = (TextView)findViewById(R.id.txtMediaBrowser);
                            txt.setVisibility(View.GONE);

                            photoUris = DPB.files;
                            gallery.setAdapter(IA);
                            gallery.setOnItemClickListener(new OnItemClickListener() {

                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                    Toast.makeText(getBaseContext(), "pic" + (position + 1) + " selected",
                                            Toast.LENGTH_SHORT).show();
                                    // display the images selected
                                    ImageView imageView = (ImageView) findViewById(R.id.image1);
                                    //imageView.setImageResource(imageIDs[position]);
                                    UrlImageViewHelper.setUrlDrawable(imageView, "https://memeticamestorage.blob.core.windows.net/photos" + idchat + "/" + photoUris.get(position), R.drawable.photo);

                                }
                            });
                        }else
                        {
                            setContentView(R.layout.activity_media_browser);
                            TextView txt = (TextView)findViewById(R.id.txtMediaBrowser);
                            txt.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }
        });
        t.start();

    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;

        public ImageAdapter(Context c) {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return photoUris.size();
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {

           try {
               ImageView imageView = new ImageView(context);
               //imageView.setImageResource(imageIDs[position]);
               UrlImageViewHelper.setUrlDrawable(imageView, "https://memeticamestorage.blob.core.windows.net/photos"+idchat+"/" + photoUris.get(position), R.drawable.photo);
               imageView.setBackgroundResource(itemBackground);
               return imageView;
           }catch (Exception e){
               e.printStackTrace();return null;}
        }
    }
}
