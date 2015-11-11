package com.mobility42.azurechatr.MemeViewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobility42.azurechatr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MemeCreator extends Activity {

    Typeface memefont;
    static final String FONT_PATH = "Fonts/Action_Man.ttf";
    private RelativeLayout meme_layout;
    private ImageView meme_image;
    private TextView upper_text;
    private TextView lower_text;
    private Button done_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_creator);
        memefont = Typeface.createFromAsset(getAssets(),FONT_PATH);

        meme_layout = (RelativeLayout) findViewById(R.id.meme_layout);
        meme_image = (ImageView) findViewById(R.id.memecreator_image); //findViewById(getIntent().getIntExtra("SENT_ID", 0));
        upper_text = (TextView) findViewById(R.id.memecreator_upperText);
        lower_text = (TextView) findViewById(R.id.memecreator_lowerText);

        upper_text.setTypeface(memefont);
        lower_text.setTypeface(memefont);

        meme_image.setImageDrawable(MemeViewerActivity.cosa);
    }

    public void click(View v) {
        upper_text.clearFocus();
        lower_text.clearFocus();
        printScreen();
        finish();

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
