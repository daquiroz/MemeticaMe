package com.mobility42.azurechatr;

import android.content.Context;
import android.os.AsyncTask;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadAudioBlob extends AsyncTask<String, Void, Void> {

    public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" +
            "AccountName=memeticamestorage;"+"AccountKey=q6Bt7iiqVbxUcGjPUSrqq4eNiT6AY6hORPlND9qbM1qbJz4SW1wPWuRKZA4lgQX/mD5Cdfihm/vUwWHLj1Q6/A==";

    File f;
    CloudStorageAccount account;
    CloudBlobClient blobClient;
    Context context;
    File filesDir;
    List<String> files;
    public boolean finished = false;
    String chatid;
    String audioblobName;
    public String blobDir;
    public DownloadAudioBlob(Context context, String chatid,String audioblobName)
    {   this.chatid = chatid;
        this.audioblobName = audioblobName;
        this.context =context;
        filesDir = context.getCacheDir();
        files = new ArrayList<String>();

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



                downloadPhotos();


        } catch (Throwable t) {
            t.printStackTrace();
        }


        return null;

    }

    void downloadPhotos()
    {
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference("audio" + chatid);

            boolean encontrado = false;
            for (ListBlobItem blobItem : container.listBlobs()) {
                // If the item is a blob, not a virtual directory.
                if (blobItem instanceof CloudBlob) {


                    CloudBlob blob = (CloudBlob) blobItem;
                    String tempblobName = blob.getName();

                    if(((CloudBlob) blobItem).getName().equals(audioblobName)) {
                        blob.download(new FileOutputStream(filesDir + tempblobName));
                        blobDir = filesDir + blob.getName();
                        encontrado =true;
                        break;
                    }
                    int a=2;

                }
            }



        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }


    }
    public boolean  getfinished(){
        return finished;
    }

}
/*        // Download the blob
        // For each item in the container
        for (ListBlobItem blobItem : container.listBlobs()) {
            // If the item is a blob, not a virtual directory
            if (blobItem instanceof CloudBlockBlob) {
                // Download the text
                CloudBlockBlob retrievedBlob = (CloudBlockBlob) blobItem;
            }
        }

        // List the blobs in a container, loop over them and
        // output the URI of each of them
        for (ListBlobItem blobItem : container.listBlobs()) {
        }

        // Delete the blobs
        //blob1.deleteIfExists();
        // blob2.deleteIfExists();
        // blob3.deleteIfExists();

        // Delete the container
        container.deleteIfExists();*/