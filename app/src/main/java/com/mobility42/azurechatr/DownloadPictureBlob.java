package com.mobility42.azurechatr;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DownloadPictureBlob extends AsyncTask<String, Void, Void> {

    public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" +
            "AccountName=memeticamestorage;"+"AccountKey=q6Bt7iiqVbxUcGjPUSrqq4eNiT6AY6hORPlND9qbM1qbJz4SW1wPWuRKZA4lgQX/mD5Cdfihm/vUwWHLj1Q6/A==";

    File f;
    CloudStorageAccount account;
    CloudBlobClient blobClient;
    Context context;
    int option;
    File filesDir;
    List<Uri> files;
    public boolean finished = false;

    public DownloadPictureBlob(Context context)
    {
        this.context =context;
        filesDir = context.getCacheDir();
        files = new ArrayList<Uri>();
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
            CloudBlobContainer container = blobClient.getContainerReference("photos");


            for (ListBlobItem blobItem : container.listBlobs()) {
                // If the item is a blob, not a virtual directory.
                if (blobItem instanceof CloudBlob) {
                    // Download the item and save it to a file with the same name.
                    CloudBlob blob = (CloudBlob) blobItem;
                    blob.download(new FileOutputStream(filesDir + blob.getName()));
                    blob.downloadToFile(filesDir + blob.getName());
                    File f = new File(filesDir + blob.getName());
                    Uri uri = Uri.fromFile(f);
                    files.add(uri);
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