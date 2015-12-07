package com.mobility42.azurechatr.MemeViewer;

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
import com.mobility42.azurechatr.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dani on 07-12-15.
 */
public class CanalActivity extends Activity{


//    ContactAdapter contactadapter;
//    ListView listviewcontactos;
//    private ArrayList<Meme> listaMemes;
      public String miId = DB.miId;
      public DB db;

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

        ImageButton btn = (ImageButton)findViewById(R.id.btVolverChat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaContactosActivity.this, ListaChatActivity.class));
            }
        });

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        db = new DB();

        // Sets the columns to retrieve for the user profile
        String[] projection = new String[]
                {
                        ContactsContract.Profile._ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.Profile.LOOKUP_KEY,
                        ContactsContract.Profile.PHOTO_THUMBNAIL_URI
                };


        CursorLoader cursor = new CursorLoader(
                getApplicationContext(),  // The activity's context
                uri,              // The entity content URI for a single contact
                projection,               // The columns to retrieve
                null,                     // Retrieve all the raw contacts and their data rows.
                null,                     //
                null);               // Sort by the raw contact ID.



        ContentResolver cr = getContentResolver();

        Cursor cur = cr.query(uri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados


        listaContactosCelular = new ArrayList<Contact>();
        if (cur.moveToFirst())
        {
            int colid= cur.getColumnIndex(projection[0]);
            int colname = cur.getColumnIndex(projection[1]);
            int colkey = cur.getColumnIndex(projection[2]);
            int colphoto = cur.getColumnIndex(projection[3]);

            String nombre;
            String mail = "";
            String id;
            String estado = "";
            String contactphoto;

            do
            {
                listaContactosCelular.add(new Contact(cur.getString(colname),
                        mail, cur.getString(colid), estado,cur.getString(colphoto) ));
            } while (cur.moveToNext());
        }


        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    AZUREMOBILESERVICES_URI,
                    AZUREMOBILESERVICES_APPKEY,
                    this);

            // Get the Mobile Service Table instance to use
            contactTable = mClient.getTable(Contact.class);
            chatTable = mClient.getTable(Chat.class);

            listaContactosAzure = new ArrayList<Contact>();
            listachat = new ArrayList<Chat>();

            llenarTablaChat();
            //listaFinal = new ArrayList<Contact>();

            //mTextNewChat = (EditText) findViewById(R.id.textNewChat);

            // Create an adapter to bind the items with the view
//            mAdapter = new ChatItemAdapter(this,lista);
//            listViewChat = (ListView) findViewById(R.id.listViewChat);
//            listViewChat.setAdapter(mAdapter);

            // Load the items from the Mobile Service //

            refreshItemsFromTable();


            contactadapter = new ContactAdapter(this, listaContactosAzure);

            listviewcontactos = (ListView) findViewById(R.id.listviewcontact);
            listviewcontactos.setAdapter(contactadapter);


        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


        listviewcontactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Contact item = contactadapter.getItem(position);
                String idcontactoseleccionado = item.getId();


                //METODO QUE DADO UN CONTACTO BUSCA EL ID DEL CHAT QUE TENEMOS EN COMUN. EN CASO DE NO EXISTIR CREA UNO.
                String idchat = retornarIdChat2(item);

                Intent intent = new Intent(ListaContactosActivity.this, ChatActivity.class);
                intent.putExtra("idchat", idchat);
                startActivity(intent);

                Toast.makeText(getBaseContext(), idchat, Toast.LENGTH_SHORT).show();

            }
        });


        ImageButton btnAddContact = (ImageButton)findViewById(R.id.btAgregarContacto);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);

            }
        });

}
