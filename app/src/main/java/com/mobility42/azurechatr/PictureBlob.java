package com.mobility42.azurechatr;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;

public class PictureBlob extends AsyncTask<String, Void, Void> {

    public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" +
            "AccountName=memeticamestorage;"+"AccountKey=q6Bt7iiqVbxUcGjPUSrqq4eNiT6AY6hORPlND9qbM1qbJz4SW1wPWuRKZA4lgQX/mD5Cdfihm/vUwWHLj1Q6/A==";

    File f;
    CloudStorageAccount account;
    CloudBlobClient blobClient;
    Context context;
    int option;
    public boolean finished = false;
    String idchat;

    public PictureBlob(File f,int option,Context context,String idchat)
    {
        this.option = option;
        this.context =context;
        this.f = f;
        this.idchat=idchat;
    }
    @Override
    protected void onPostExecute(Void v) {
        finished =true;
    }
    @Override
    protected Void doInBackground(String... arg0) {


        try {
            // Setup the cloud storage account.
             account = CloudStorageAccount
                    .parse(storageConnectionString);

            // Create a blob service client
             blobClient = account.createCloudBlobClient();


            if(option==0)//Upload photo
            {
                uploadPhoto();
            }
            publishProgress();


        } catch (Throwable t) {
            t.printStackTrace();
        }


        return null;
    }

    void uploadPhoto()
    {
        try {
            CloudBlobContainer container = blobClient.getContainerReference("photos"+idchat);

            // Create the container if it does not exist
            container.createIfNotExists();

            // Make the container public
            // Create a permissions object
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

            // Include public access in the permissions object
            containerPermissions
                    .setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

            // Set the permissions on the container
            container.uploadPermissions(containerPermissions);

            // Upload 3 blobs
            // Get a reference to a blob in the container

            CloudBlockBlob blob = container
                    .getBlockBlobReference("IMG_"
                            + UUID.randomUUID().toString().replace("-", "")+ ".jpg");
            blob.upload(new FileInputStream(f), f.length());
        }catch(Exception e)
        {
            Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    public boolean  getfinished(){
        return finished;
    }

}
