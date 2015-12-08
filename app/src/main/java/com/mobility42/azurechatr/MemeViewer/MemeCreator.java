package com.mobility42.azurechatr.MemeViewer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobility42.azurechatr.CanalActivity;
import com.mobility42.azurechatr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemeCreator extends Activity {

    Typeface memefont;
    //static final String FONT_PATH = "Fonts/Action_Man.ttf";
    static final String FONT_PATH = "Fonts/Impact.ttf";
    private RelativeLayout meme_layout;
    private ImageView meme_image;
    private TextView upper_text;
    private TextView lower_text;
    private Button done_btn;
    private String idchat;
    private String modocanal;
    private String idcanal;
    private String modo;
    private String nombrecanal;
    private String categoria;
    private String etiquetas;
    File imgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_creator);
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
                modocanal = getIntent().getExtras().getString("modocanal");
                idcanal = getIntent().getExtras().getString("idcanal");
                nombrecanal = getIntent().getExtras().getString("nombrecanal");
                categoria = getIntent().getExtras().getString("categoria");
                etiquetas = getIntent().getExtras().getString("etiquetas");

            } catch (Exception excepcion) {

                modocanal = "000000";
                categoria = "000000";
                etiquetas = "000000";
                idcanal = "000000";
            }

        }

        memefont = Typeface.createFromAsset(getAssets(),FONT_PATH);

        meme_layout = (RelativeLayout) findViewById(R.id.meme_layout);
        meme_image = (ImageView) findViewById(R.id.memecreator_image); //findViewById(getIntent().getIntExtra("SENT_ID", 0));
        upper_text = (OutlineTextView) findViewById(R.id.memecreator_upperText);
        lower_text = (OutlineTextView) findViewById(R.id.memecreator_lowerText);
        upper_text.setTextColor(Color.WHITE);
        lower_text.setTextColor(Color.WHITE);
        upper_text.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Offsets are for centering the TextView on the touch location
                    v.setX(event.getRawX() - v.getWidth() / 2.0f);
                    v.setY(event.getRawY() - v.getHeight() / 2.0f);

                }
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;

            }

        });
        upper_text.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        lower_text.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });




        lower_text.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Offsets are for centering the TextView on the touch location
                    v.setX(event.getRawX() - v.getWidth() / 2.0f);
                    v.setY(event.getRawY() - v.getHeight() / 2.0f);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                }
                return false;

            }

        });

        upper_text.setTypeface(memefont);
        lower_text.setTypeface(memefont);

        meme_image.setImageDrawable(MemeViewerActivity.cosa);
    }

    public void click(View v) {
        if(modo.equals("Normal")) {
            upper_text.clearFocus();
            upper_text.setEnabled(false);
            upper_text.setCursorVisible(false);
            upper_text.setKeyListener(null);
            upper_text.setBackgroundColor(Color.TRANSPARENT);
            lower_text.clearFocus();
            lower_text.setEnabled(false);
            lower_text.setCursorVisible(false);
            lower_text.setKeyListener(null);
            lower_text.setBackgroundColor(Color.TRANSPARENT);
            addImageToGallery();
            String path = imgPath.getPath();
            Intent setData = new Intent();
            setData.putExtra("path", path);
            setResult(RESULT_OK, setData);
            finish();

        }
        else if(modo.equals("Canal")){
            upper_text.clearFocus();
            upper_text.setEnabled(false);
            upper_text.setCursorVisible(false);
            upper_text.setKeyListener(null);
            upper_text.setBackgroundColor(Color.TRANSPARENT);
            lower_text.clearFocus();
            lower_text.setEnabled(false);
            lower_text.setCursorVisible(false);
            lower_text.setKeyListener(null);
            lower_text.setBackgroundColor(Color.TRANSPARENT);
            addImageToGallery();

            if(modocanal.equals("Creador")) {
                String path = imgPath.getPath();
                Intent canal = new Intent(this, CanalActivity.class);
                canal.putExtra("etiquetas", etiquetas);
                canal.putExtra("nombrecanal", nombrecanal);
                canal.putExtra("idcanal", idcanal);
                canal.putExtra("modocanal", modocanal);
                canal.putExtra("categoria", categoria);
                canal.putExtra("path", path);
                startActivity(canal);
            }
        }

    }

    public void addImageToGallery() {

        meme_layout.setDrawingCacheEnabled(true);
        Bitmap bimg = meme_layout.getDrawingCache();

        File mydir2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "memeticame");
        if (!mydir2.exists()) {
            mydir2.mkdirs();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date = dateFormat.format(new Date()).toString().replace(" ", "");

        imgPath = new File(mydir2,"meme_"+date+".png");
        FileOutputStream fOs = null;

        try {
            fOs = new FileOutputStream(imgPath);
            bimg.compress(Bitmap.CompressFormat.PNG, 100, fOs);
            fOs.flush();
            fOs.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, imgPath.getPath());

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    private void printScreen () {
        meme_layout.setDrawingCacheEnabled(true);
        Bitmap bimg = meme_layout.getDrawingCache();

        File mydir2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "memeticame");
        if (!mydir2.exists()) {
            mydir2.mkdirs();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date = dateFormat.format(new Date()).toString().replace(" ", "");

        File imgPath = new File(mydir2,"meme_"+date+".png");
        FileOutputStream fOs = null;

        try {
            fOs = new FileOutputStream(imgPath);
            bimg.compress(Bitmap.CompressFormat.PNG, 100, fOs);
            fOs.flush();
            fOs.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }




    private void viewToImage() {
        Bitmap bitmap = meme_layout.getDrawingCache();
        meme_layout.buildDrawingCache();
        try {
            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/path/to/file.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



}
