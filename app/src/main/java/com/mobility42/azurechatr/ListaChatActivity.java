package com.mobility42.azurechatr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ListaChatActivity extends Activity {


    ChatAdapter chatAdapter;
    ListView listviewchat;
    private ArrayList<Chat> listaChat;
    //public String miId = "56985003385";


    private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
    private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
    private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
    private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
    private MobileServiceClient mClient;
    private MobileServiceTable<Chat> chatTable;
    public DB db;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_chat);

        db = new DB();
        db.miId = "56992773617";

        ImageButton btn = (ImageButton)findViewById(R.id.btCrearChat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaChatActivity.this, CrearChatActivity.class));
            }
        });

        ImageButton btn2 = (ImageButton)findViewById(R.id.btVerContactos);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaChatActivity.this, ListaContactosActivity.class));
            }
        });

        context = getApplicationContext();

        db = new DB();

        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    AZUREMOBILESERVICES_URI,
                    AZUREMOBILESERVICES_APPKEY,
                    this);

            // Get the Mobile Service Table instance to use
            chatTable = mClient.getTable(Chat.class);

            listaChat = new ArrayList<Chat>();



            refreshItemsFromTable();


            chatAdapter = new ChatAdapter(this, listaChat);

            listviewchat = (ListView) findViewById(R.id.listviewcontact);
            listviewchat.setAdapter(chatAdapter);



        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


        listviewchat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Chat item = chatAdapter.getItem(position);
                String idchat = item.getIdchat();
                Toast.makeText(getBaseContext(), idchat, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListaChatActivity.this, ChatActivity.class);
                intent.putExtra("idchat", idchat);
                startActivity(intent);
            }
        });

    }


    private synchronized void refreshItemsFromTable() {

        // Get all the chat items and add them in the adapter
        chatTable.execute(new TableQueryCallback<Chat>() {

            public void onCompleted(List<Chat> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    listaChat.clear();

                    List<Chat> listatotal = result;
                    result = filtrarChat(result);

                    for (Chat item : result) {
                        Chat c = new Chat(item.getNamechat(), item.getIdcreador(), item.getIdcontact(), item.getIdchat(), item.getFecha());
                        if(item.getNamechat() == null)
                        {
                            String nombrechatnuevo = nombreChat(item.getIdchat(), listatotal);
                            c.setIdcontact(nombrechatnuevo);
                            listaChat.add(c);
                        }
                        else {
                            listaChat.add(c);
                        }
                        //String nombrechat = nombreChat(c.getIdchat(), result);
                        //if (nombrechat != "") {
                           //     c.setNamechat(nombrechat);

                            //}
                        }
                    }

//                    for (Chat item : result) {
//                        Chat c = new Chat(item.getNamechat(), item.getIdcreador(), item.getIdcontact(), item.getIdchat(), item.getFecha());
//                        //Metodo que filtra si es un chat en donde estoy
//                        if (pertenezcoAlChat(c, miId)) {
//                            //Si es valido para mi, busco el nombre. Metodo retorna "" si
//                            String nombrechat = nombreChat(c.getIdchat(), result);
//                            if (nombrechat != "") {
//                                c.setNamechat(nombrechat);
//                                listaChat.add(c);
//                            }
//                        }
//                    }

                    chatAdapter.notifyDataSetChanged();
                    listviewchat.setAdapter(chatAdapter);





            }
        });



    }

    private List<Chat> filtrarChat(List<Chat> result) {

        List<Chat> lista = new ArrayList<Chat>();
        for (Chat chat : result)
        {
            if (chat.getIdcontact().equals(db.miId))
            {
                lista.add(chat);
            }
        }
        return lista;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_chat, menu);
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

    public boolean pertenezcoAlChat(Chat chat, String miId)
    {
        if (chat.getIdcontact().equals(miId))
        {
            return true;
        }
        return false;
    }


    //Busco el chat con cierto id, tal que si es el chat que tengo con una persona me retorne el id de esa persona.
    // En caso que no exista retornara "".

    public String nombreChat(String idchat, List<Chat> listadechat)
    {
        for (Chat chat : listadechat)
        {
            if (chat.getIdchat().equals(idchat) && !chat.getIdcontact().equals(db.miId) ) {

                return chat.getIdcontact();
                 //   String enviar = BuscarNombre(chat.getIdcontact());
                 //   return enviar;

            }
        }

        return "";

    }



    private String BuscarNombre(String idcontact) {

        for (Contact c : db.listaC)
        {
            if (c.getId().equals(idcontact))
                return c.getName();
        }
        return null;
    }






}
