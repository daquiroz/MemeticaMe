package com.mobility42.azurechatr.MemeViewer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobility42.azurechatr.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MemeViewerActivity extends Activity {

    private static final String MEME_FILE_PATH = "the-meme-files";
    private ArrayList<Drawable> meme_list = new ArrayList<>();
    private ArrayList<FeedMeme> feedmeme_list = new ArrayList<>();
    private ListView meme_listview;
    private String idchat;
    private String modo;
    private String idcanal;
    private String categoria;
    private String etiquetas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meme_view);

        try
        {
            modo = getIntent().getExtras().getString("modo");
        }
        catch(Exception excepcion)
        {
            idchat = "Normal";
        }

        if(modo.equals("Normal")) {
            try {

                idchat = getIntent().getExtras().getString("idchat");

            } catch (Exception excepcion) {

                idchat = "000000";

            }
        }
        else if(modo.equals("Canal")) {
            try {

                idcanal = getIntent().getExtras().getString("idcanal");
                categoria = getIntent().getExtras().getString("categoria");
                etiquetas = getIntent().getExtras().getString("etiquetas");

            } catch (Exception excepcion) {

                categoria = "000000";
                etiquetas = "000000";
                idcanal = "000000";
            }

        }


        addListFiles(MEME_FILE_PATH);
        generateFeedMemeList();
        MemeAdapter memeAdapter = new MemeAdapter(this, feedmeme_list);

        meme_listview = (ListView) findViewById(R.id.memeView_list);
        meme_listview.setAdapter(memeAdapter);
    }

    static Drawable cosa;
    static int cosa_h;
    static int cosa_w;

    public void createMeme(View v) {
        ImageView imageView = (ImageView)v;
        MemeViewerActivity.cosa_h = imageView.getHeight();
        MemeViewerActivity.cosa_w = imageView.getWidth();
        MemeViewerActivity.cosa = imageView.getDrawable();

        Intent i = new Intent(this, MemeCreator.class);
        i.putExtra("idchat", idchat);
        i.putExtra("modo", "Normal");
        startActivityForResult(i, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == RESULT_OK){
                String path =data.getStringExtra("path");
                Intent setData = new Intent();
                setData.putExtra("path", path);
                setResult(RESULT_OK, setData);
                finish();
            }
        }
    }


    private void addListFiles(String dirFrom) {

        Resources res = getResources(); //if you are in an activity
        AssetManager am = res.getAssets();

        try {
            String fileList[] = am.list(dirFrom);
            //magia

            if (fileList != null)
            {
                for ( String meme : fileList)
                {
                    try {
                        // get input stream
                        InputStream ims = getAssets().open(MEME_FILE_PATH+"/"+meme);
                        // load image as Drawable
                        Drawable d = Drawable.createFromStream(ims, null);
                        meme_list.add(d);
                    }
                    catch(IOException ex) {
                        CharSequence text = "Error!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(this, text, duration);
                        toast.show();
                    }
                }
            }
        }
        catch (Exception e) {
            CharSequence text = "Error!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

    private void generateFeedMemeList() {
        int index = 0;

        while(index < meme_list.size()) {
            Drawable d_aux;

            if(index+1 < meme_list.size()) { d_aux = meme_list.get(index+1); }
            else { d_aux = null; }

            FeedMeme aux = new FeedMeme(meme_list.get(index), d_aux);
            feedmeme_list.add(aux);

            index+=2;
        }
    }
}
