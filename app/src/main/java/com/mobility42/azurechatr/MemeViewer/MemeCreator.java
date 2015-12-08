package com.mobility42.azurechatr.MemeViewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobility42.azurechatr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MemeCreator extends Activity implements View.OnClickListener {

    //Typeface memefont;
    //static final String FONT_PATH = "Fonts/Action_Man.ttf";
    //static final String FONT_PATH = "Fonts/Impact.ttf";

    private ImageView meme_image;

    private Button done_btn;
    private String idchat;
    private String idcanal;
    private String modo;
    private String categoria;
    private String etiquetas;
    File imgPath;

    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, textBtn;
    private float smallBrush, mediumBrush, largeBrush;

    private Spinner font_spinner;
    private Button btnSubmit;
    private EditText textSize, imgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_layout);
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

        drawView = (DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawView.setBrushSize(mediumBrush);
        drawView.setOnDragListener(new MyDragListener());
        drawView.setOnDragListener(new MyDragListener());

        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        textBtn = (ImageButton)findViewById(R.id.text_btn);
        textBtn.setOnClickListener(this);



        /*upper_text.setOnTouchListener(new View.OnTouchListener() {

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
/*
        });*//**//*
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
        });*//*




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
        lower_text.setTypeface(memefont);*/

        //meme_image.setImageDrawable(MemeViewerActivity.cosa);
        drawView.setBackground(MemeViewerActivity.cosa);
    }

    public void paintClicked(View view){
        //use chosen color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));
            currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint));
            currPaint=(ImageButton)view;

            drawBtn = (ImageButton)findViewById(R.id.draw_btn);
            drawBtn.setOnClickListener(this);
        }
    }

    public void onClick(View view) {

        //respond to clicks
        if (view.getId() == R.id.draw_btn) {
            //draw button clicked
            drawMode();
        }

        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            eraseMode();
        }

        else if(view.getId()==R.id.new_btn){
            //new button
            newMode();
        }

        else if(view.getId()==R.id.save_btn){
            //save drawing
            saveMode();
        }

        else if(view.getId()==R.id.text_btn) {
            textMode();
        }
    }



    private void touchSelection(String path) {

        if(modo.equals("Normal")) {

            Intent setData = new Intent();
            setData.putExtra("path", path);
            setResult(RESULT_OK, setData);
            finish();
        }
        else if(modo.equals("Canal")){

            Intent canal = new Intent(this, MemeCreator.class);
            canal.putExtra("etiquetas", etiquetas);
            canal.putExtra("idcanal", idcanal);
            canal.putExtra("path", path);
            startActivity(canal);
        }
    }

    private void saveMode() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //save drawing
                makeAllTextUnfocusable();
                drawView.setDrawingCacheEnabled(true);

                /** ------------------PATH------------------ **/
                /*String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "drawing");
                Uri a = MediaStore.Images.Media.getContentUri();*/
                addImageToGallery();

                if(imgPath!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery! "+imgPath.getPath(), Toast.LENGTH_SHORT);
                    savedToast.show();


                    touchSelection(imgPath.getPath());
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }

                drawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    private void eraseMode() {

        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Eraser size:");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    private void drawMode() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush size:");

        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                drawView.setErase(false);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                drawView.setErase(false);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                drawView.setErase(false);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    private void newMode() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawView.startNew();
                drawView.removeAllViews();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    private void textMode() {

        final Dialog textDialog = new Dialog(this);
        textDialog.setTitle("Text settings:");
        textDialog.setContentView(R.layout.text_settings);

        textDialog.show();

        font_spinner = (Spinner) textDialog.findViewById(R.id.font_selector);
        font_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        textSize = (EditText) textDialog.findViewById(R.id.size_selector);
        imgText = (EditText) textDialog.findViewById(R.id.editText_selector);

        btnSubmit = (Button) textDialog.findViewById(R.id.textsett_submit_btn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OutlineTextView child = new OutlineTextView(drawView.getContext());

                child.setText(imgText.getText().toString());

                String txt = textSize.getText().toString();

                if (txt.equals("")) {
                    child.setTextSize(20);
                } else {
                    child.setTextSize(Float.parseFloat(textSize.getText().toString()));
                }

                child.setTextColor(drawView.paintColor);

                String sel = String.valueOf(font_spinner.getSelectedItem()).trim().replace(' ', '_') + ".ttf";
                child.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/" + sel));

                setChildOnTouch(child);
                setChildActionModeCallback(child);

                drawView.addView(child);
                textDialog.dismiss();
            }
        });
    }

    private void setChildOnTouch(View child) {
        child.setOnTouchListener(new View.OnTouchListener() {

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
    }

    private void setChildActionModeCallback(OutlineTextView child) {
        child.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
    }

    private void makeAllTextUnfocusable() {

        for( int i=0; i < drawView.getChildCount(); i++) {
            OutlineTextView text = (OutlineTextView)drawView.getChildAt(i);
            text.clearFocus();
            text.setEnabled(false);
            text.setCursorVisible(false);
            text.setKeyListener(null);
            text.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void addImageToGallery() {


        Bitmap bimg = drawView.getDrawingCache();

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
}
