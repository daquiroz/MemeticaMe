package com.mobility42.azurechatr;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class AudioBlob extends AsyncTask<String, Void, Void> {

    public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" +
            "AccountName=memeticamestorage;"+"AccountKey=q6Bt7iiqVbxUcGjPUSrqq4eNiT6AY6hORPlND9qbM1qbJz4SW1wPWuRKZA4lgQX/mD5Cdfihm/vUwWHLj1Q6/A==";

    File f;
    CloudStorageAccount account;
    CloudBlobClient blobClient;
    Context context;
    int option;
    public boolean finished = false;
    String idchat;
    public String blobname;

    public AudioBlob(File f, Context context, String idchat)
    {
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



                uploadAudio();
            publishProgress();


        } catch (Throwable t) {
            t.printStackTrace();
        }


        return null;
    }

    void uploadAudio()
    {
        try {
            CloudBlobContainer container = blobClient.getContainerReference("audio"+idchat);

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
            blobname ="Audio"
                    + UUID.randomUUID().toString().replace("-", "")+ ".3gp";
            CloudBlockBlob blob = container
                    .getBlockBlobReference(blobname);

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
