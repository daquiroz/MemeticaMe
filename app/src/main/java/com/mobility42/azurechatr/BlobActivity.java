package com.mobility42.azurechatr;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class BlobActivity extends Activity {


    private int PICK_IMAGE_REQUEST = 1;
    File filesDir;
    int Option;
    String idchat = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesDir = this.getCacheDir();
        Option =Integer.parseInt(getIntent().getStringExtra("Option"));

        idchat =getIntent().getStringExtra("idchat");
        Toast.makeText(this, "id adquirido!!!! --->" + idchat, Toast.LENGTH_SHORT).show();


        if (Option == 0)
        {
            setContentView(R.layout.photoblob_layout);
        }else if (Option ==1)
        {
            setContentView(R.layout.activity_recorder);



        }else if (Option ==2)
        {

        }

    }

    public void openPhotoGallery(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();


            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);


               final PictureBlob PB = new PictureBlob(persistImage(bitmap,"IMAGEEEEEEEEEN"),0,this,idchat);
                PB.execute();

                Intent intent = new Intent(BlobActivity.this, ChatActivity.class);
                intent.putExtra("idchat",idchat);
                intent.putExtra("Option", "0");
                intent.putExtra("idchat", idchat);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private File persistImage(Bitmap bitmap, String name) {
        File imageFile = new File(filesDir, name + ".jpg");
        imageFile.delete();
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e("TAAAAAAG", "Error writing bitmap", e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            return null;
        }
    }

}
