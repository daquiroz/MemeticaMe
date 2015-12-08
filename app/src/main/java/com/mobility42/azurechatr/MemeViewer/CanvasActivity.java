package com.mobility42.azurechatr.MemeViewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobility42.azurechatr.R;

import java.util.UUID;

//http://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-interface-creation--mobile-19021
public class CanvasActivity extends Activity implements OnClickListener
{

    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, textBtn;
    private float smallBrush, mediumBrush, largeBrush;

    private Spinner font_spinner;
    private Button btnSubmit;
    private EditText textSize, imgText;

    private int textColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_layout);

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

    /** Interface **/
    @Override
    public void onClick(View view) {
        //respond to clicks
        if (view.getId() == R.id.draw_btn) {
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");

            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
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

        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }

        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    drawView.removeAllViews();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }

        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);

                    /** ------------------PATH------------------ **/
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");

                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery! "+imgSaved, Toast.LENGTH_SHORT);
                        savedToast.show();
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

        else if(view.getId()==R.id.text_btn) {
            final Dialog textDialog = new Dialog(this);
            textDialog.setTitle("Text settings:");
            textDialog.setContentView(R.layout.text_settings);

            textDialog.show();

            font_spinner = (Spinner) textDialog.findViewById(R.id.font_selector);
            font_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

            textSize = (EditText) textDialog.findViewById(R.id.size_selector);
            imgText = (EditText) textDialog.findViewById(R.id.editText_selector);

            btnSubmit = (Button) textDialog.findViewById(R.id.textsett_submit_btn);
            btnSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    TextView child = new TextView(drawView.getContext());

                    child.setText(imgText.getText().toString());

                    String txt = textSize.getText().toString();

                    if (txt.equals("")) {
                        child.setTextSize(20);
                    }
                    else {
                        child.setTextSize( Float.parseFloat( textSize.getText().toString() ) );
                    }

                    child.setTextColor(drawView.paintColor);

                    String sel = String.valueOf(font_spinner.getSelectedItem()).trim().replace(' ','_')+".ttf";
                    child.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/" + sel));

                    child.setOnTouchListener(new MyTouchListener());

                    drawView.addView(child);
                    textDialog.dismiss();
                }
            });

        }
    }



}
