package com.mobility42.azurechatr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CrearChatActivity extends Activity {

    ContactSelectAdapter contactadapter;
    ListView listviewcontactos;
    private ArrayList<Contact> listaContactosAzure;
    private ArrayList<Contact> listaContactosCelular;
    private ArrayList<Contact> listaFinal;
    private ArrayList<Contact> listContactAdd;


    private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
    private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
    private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
    private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
    private MobileServiceClient mClient;
    private MobileServiceTable<Contact> contactTable;
    private MobileServiceTable<Chat> chatTable;

    public String miId = DB.miId;
    public EditText mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_chat);

        listContactAdd = new ArrayList<Contact>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;


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

            refreshItemsFromTable();

            contactadapter = new ContactSelectAdapter(this, listaContactosAzure);

            listviewcontactos = (ListView) findViewById(R.id.listviewcontact);
            listviewcontactos.setAdapter(contactadapter);

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }




        ImageButton btn = (ImageButton)findViewById(R.id.btCrearChatOk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r = new Random();
                Integer aux = r.nextInt();
                String idchat = aux.toString();


                mEdit = (EditText)( (View)v.getParent()).findViewById(R.id.editText);
                String nombreChat = mEdit.getText().toString();

                for (Contact c : listContactAdd) {
                    addChat(nombreChat, c.getId(), idchat);
                }
                addChat(nombreChat, miId, idchat);

                Intent intent = new Intent(CrearChatActivity.this, ChatActivity.class);
                intent.putExtra("idchat", idchat);
                startActivity(intent);

             }
        });


        listviewcontactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Contact contact = contactadapter.getItem(position);
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkcontacto);
                if (cb.isChecked() == true)
                {
                    cb.setChecked(false);
                    listContactAdd.remove(contact);
                    Toast.makeText(CrearChatActivity.this, contact.getId(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    cb.setChecked(true);
                    listContactAdd.add(contact);
                    Toast.makeText(CrearChatActivity.this, contact.getId(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private synchronized void refreshItemsFromTable() {

        // Get all the chat items and add them in the adapter
        contactTable.execute(new TableQueryCallback<Contact>() {

            public void onCompleted(List<Contact> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    listaContactosAzure.clear();

                    for (Contact item : result) {
                        Contact fc = new Contact(item.getName(), item.getMail(), item.getId(), item.getEstado(), item.getContactphoto());
                        if (CompararString(listaContactosCelular, fc.getId()) == true) {
                            listaContactosAzure.add(fc);
                        }
                    }
                    //listaFinal = CompararListas(listaContactosCelular, listaContactosAzure);
                    contactadapter.notifyDataSetChanged();
                    listviewcontactos.setAdapter(contactadapter);
                } else {
                    //createAndShowDialog(exception, "Error");
                }


            }
        });



    }


    public Boolean CompararString (ArrayList<Contact> listaContactosCelular, String numeroDB)
    {

        for (Contact contactoCel : listaContactosCelular)
        {
            String numero = contactoCel.getName().replace(" ", "");
            String numero2 = numero.replace("+", "");
            if (numero2.equals(numeroDB))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addChat(String nombre, String idpersona, String idRandom) {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final Chat item = new Chat();

        item.setNamechat(nombre);
        item.setIdchat(idRandom);
        item.setIdcontact(idpersona);
        item.setFecha("11/11/2015");
        item.setIdcreador(miId);

        // Insert the new item
        chatTable.insert(item, new TableOperationCallback<Chat>() {

            @Override
            public void onCompleted(Chat chat, Exception e1, ServiceFilterResponse serviceFilterResponse) {

            }
        });

    }

}
