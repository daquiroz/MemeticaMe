package com.mobility42.azurechatr;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.view.View;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;


/**
 * Created by Dani on 10-11-15.
 */



public class AsyncTableChat extends AsyncTask<Void, Void, ArrayList<Chat>> {

    private ArrayList<Chat> listaChat;
    public String miId = "56985003385";


    private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
    private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
    private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
    private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
    private MobileServiceClient mClient;
    private MobileServiceTable<Chat> chatTable;
    private Context context;

    public ChatAdapter chatAdapter;
    public ListView listviewchat;

    public AsyncTableChat(Context context)
    {

        this.context = context;
    }


    @Override
    protected ArrayList<Chat> doInBackground(Void... params) {


        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(AZUREMOBILESERVICES_URI,AZUREMOBILESERVICES_APPKEY, context);

            chatTable = mClient.getTable(Chat.class);

            listaChat = new ArrayList<Chat>();

            chatTable.execute(new TableQueryCallback<Chat>() {

                public void onCompleted(List<Chat> result, int count, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        //listaChat.clear();

                        for (Chat item : result) {
                            Chat c = new Chat(item.getNamechat(), item.getIdcreador(), item.getIdcontact(), item.getIdchat(), item.getFecha() );
                             listaChat.add(c);
                        }
                    } else {
                        //createAndShowDialog(exception, "Error");
                    }
                }
            });



        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


        return listaChat;
    }





}
