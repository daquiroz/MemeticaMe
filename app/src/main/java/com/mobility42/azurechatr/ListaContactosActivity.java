package com.mobility42.azurechatr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ListaContactosActivity extends Activity {

    ContactAdapter contactadapter;
    ListView listviewcontactos;
    private ArrayList<Contact> listaContactosAzure;
    private ArrayList<Contact> listaContactosCelular;
    private ArrayList<Contact> listaFinal;
    private ArrayList<Chat> listachat;
    public String miId = DB.miId;
    public DB db;


    private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
    private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
    private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
    private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
    private MobileServiceClient mClient;
    private MobileServiceTable<Contact> contactTable;
    private MobileServiceTable<Chat> chatTable;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contactos);

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


    }

    public synchronized String retornarIdChat2(Contact contactoSeleccionado)
    {
        String id = "";
        List<String> idvalidos = new ArrayList<String>();
        List<Chat> chatQuePuedenSer = new ArrayList<Chat>();
        List<Chat> filtroUno = new ArrayList<Chat>();

        //Retornar una lista de string de id chat validos (que este yo)
        for (Chat chat : listachat) {
            if (chat.getIdcontact().equals(miId))
            {
                idvalidos.add(chat.getIdchat());
            }
        }

        //Para cada chat reviso si tienen id tales que yo pertenezca a los chat. Los chat que pueden ser son todos donde yo aparezo, y los de mis contactos en ellos.
        for (Chat chat : listachat)
        {
            for (String idv : idvalidos)
            {
                if (chat.getIdchat() == idv)
                {
                    chatQuePuedenSer.add(chat);
                    break;
                }
            }
        }

        List<Chat> chatQueEstaLaPersonaSeleccionada = new ArrayList<Chat>();
        //Para todos los chat que pueden ser reviso si esta la persona que seleccione.
        List<Chat> chatIDS = new ArrayList<Chat>();

        for (Chat chatvalido : chatQuePuedenSer)
        {
            //Si esta la persona que seleccione, veo si hay mas gente en ese chat, ademas de los dos.
            if (chatvalido.getIdcontact().equals(contactoSeleccionado.getId()))
            {
                chatIDS = traerChatID(listachat, chatvalido.getIdchat());
                if (chatIDS.size() == 2)
                {
                    return chatvalido.getIdchat();
                }
            }
        }


        if (filtroUno.isEmpty())
        {
            Random random = new Random();
            Integer r = random.nextInt();
            String ran = r.toString();
            addChat(contactoSeleccionado.getId(), ran);
            addChat(miId, ran);
            return ran;
        }
        else
        {
            List<Chat> filtro2 = new ArrayList<Chat>();
            for (Chat chat : filtroUno)
            {
                filtro2 = traerChatID(listachat, chat.getIdchat());
                if (filtro2.size() == 2 )
                {
                    return filtro2.get(0).getIdchat();
                }
            }

        }


        return "";

    }

    public synchronized String retornarIdChat(Contact contactoSeleccionado)
    {
        String id = "";
        List<String> idvalidos = new ArrayList<String>();
        List<Chat> chatQuePuedenSer = new ArrayList<Chat>();
        List<Chat> filtroUno = new ArrayList<Chat>();

        //Retornar una lista de string de id chat validos (que este yo)
        for (Chat chat : listachat) {
            if (chat.getIdcontact().equals(miId))
            {
                idvalidos.add(chat.getIdchat());
            }
        }

        //Para cada chat reviso si tienen id tales que yo pertenezca a los chat. Los chat que pueden ser son todos donde yo aparezo, y los de mis contactos en ellos.
        for (Chat chat : listachat)
        {
            for (String idv : idvalidos)
            {
                if (chat.getIdchat() == idv)
                {
                    chatQuePuedenSer.add(chat);
                    break;
                }
            }
        }

        //Para todos los chat que pueden ser reviso si esta la persona que sleccione
        for (Chat chatvalido : chatQuePuedenSer)
        {
            for (Chat chatexistente : listachat)
            {
                if (chatvalido.getIdchat().equals(chatexistente.getIdchat()) && chatvalido.getIdcontact().compareTo(chatexistente.getIdcontact()) ==1 )
                    filtroUno.add(chatexistente);
            }
        }

        if (filtroUno.isEmpty())
        {
            Random random = new Random();
            Integer r = random.nextInt();
            String ran = r.toString();
            addChat(contactoSeleccionado.getId(), ran);
            addChat(miId, ran);
            return ran;
        }
        else
        {
            List<Chat> filtro2 = new ArrayList<Chat>();
            for (Chat chat : filtroUno)
            {
                filtro2 = traerChatID(listachat, chat.getIdchat());
                if (filtro2.size() == 2 )
                {
                    return filtro2.get(0).getIdchat();
                }
            }
            return "hola";

        }


     //   return id;

    }


    public List<Chat> traerChatID(List<Chat> listachat, String idchat)
    {
        List<Chat> listachatid = new ArrayList<Chat>();
        for (Chat ch : listachat)
        {
            if (ch.getIdchat().equals(idchat) )
            {
                listachatid.add(ch);
            }
        }
        return listachatid;

    }



    private synchronized void llenarTablaChat() {

        // Get all the chat items and add them in the adapter
        chatTable.execute(new TableQueryCallback<Chat>() {

            public void onCompleted(List<Chat> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    listachat.clear();

                    for (Chat item : result) {

                        Chat c = new Chat(item.getNamechat(), item.getIdcreador(), item.getIdcontact(), item.getIdchat(), item.getFecha() );
                        listachat.add(c);
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
                    DB.listaC = listaContactosAzure;


                } else {
                    //createAndShowDialog(exception, "Error");
                }

            }
        });



    }

    public void addChat(String idpersona, String idRandom) {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final Chat item = new Chat();

        item.setNamechat(null);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_contactos, menu);
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






}
