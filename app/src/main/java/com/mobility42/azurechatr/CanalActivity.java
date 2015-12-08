package com.mobility42.azurechatr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mobility42.azurechatr.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dani on 07-12-15.
 */
public class CanalActivity extends Activity{


    MemeChannelAdapter memeadapter;
    ListView listviewmemes;
    private ArrayList<Meme> listaMemes;
      public String miId = DB.miId;
      public DB db;
    String idcanal;

    private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
    private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
    private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
    private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
    private MobileServiceClient mClient;
    private MobileServiceTable<Meme> memesTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canal_meme);

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        db = new DB();


        try
        {
            idcanal = getIntent().getExtras().getString("idcanal");
        }
        catch(Exception excepcion)
        {
            idcanal = null;
        }


        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    AZUREMOBILESERVICES_URI,
                    AZUREMOBILESERVICES_APPKEY,
                    this);

            // Get the Mobile Service Table instance to use
            memesTable = mClient.getTable(Meme.class);

            listaMemes = new ArrayList<Meme>();

            llenarTablaMemes();
            //listaFinal = new ArrayList<Contact>();

            //mTextNewChat = (EditText) findViewById(R.id.textNewChat);

            // Create an adapter to bind the items with the view
//            mAdapter = new ChatItemAdapter(this,lista);
//            listViewChat = (ListView) findViewById(R.id.listViewChat);
//            listViewChat.setAdapter(mAdapter);

            // Load the items from the Mobile Service //

            //refreshItemsFromTable();


            memeadapter = new MemeChannelAdapter(this, listaMemes);

            listviewmemes = (ListView) findViewById(R.id.listviewMemes);
            listviewmemes.setAdapter(memeadapter);


        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


        listviewmemes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Meme item = memeadapter.getItem(position);
                //String idcontactos1eleccionado = item.getId();

                //METODO QUE DADO UN CONTACTO BUSCA EL ID DEL CHAT QUE TENEMOS EN COMUN. EN CASO DE NO EXISTIR CREA UNO.
                //String idchat = retornarIdChat2(item);

                //Intent intent = new Intent(ListaContactosActivity.this, ChatActivity.class);
                // intent.putExtra("idchat", idchat);
                // startActivity(intent);

                //Toast.makeText(getBaseContext(), idchat, Toast.LENGTH_SHORT).show();

            }
        });




    }

    private synchronized void llenarTablaMemes() {


        // Get all the chat items and add them in the adapter
        memesTable.execute(new TableQueryCallback<Meme>() {

            public void onCompleted(List<Meme> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    listaMemes.clear();

                    for (Meme item : result) {
                        if (item.getIdcanal().equals(idcanal))
                        {
                            Meme c = new Meme(item.getUrl(), item.getRanking(), item.getEtiquetas(), item.getCategoria(), item.getIdcanal(), item.getNombrecanal(), item.getId());
                            listaMemes.add(c);
                        }
                    }

                    //listaFinal = CompararListas(listaContactosCelular, listaContactosAzure);
                    //contactadapter.notifyDataSetChanged();
                    //listviewcontactos.setAdapter(contactadapter);


                } else {
                    //createAndShowDialog(exception, "Error");
                }

            }
        });



    }
}
