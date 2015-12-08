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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
public class SubirMemeCanal extends Activity{


    ArrayAdapter<String> categoriasPosibles;
    ListView listacategorias;
    //private ArrayList<Meme> listaMemes;
    public String miId = DB.miId;
    public DB db;
    String idcanal;
    String nombrecanal;
    String categoria;
    String etiquetas;


    private String AZUREMOBILESERVICES_URI = "https://memeticameapp.azure-mobile.net/";
    private String AZUREMOBILESERVICES_APPKEY = "GTjbnmDzTewswxMxsTzKSVpFjvYrbS22";
    private String AZUREPUSHNOTIFHUB_NAME = "memeticameapphub";
    private String AZUREPUSHNOTIFHUB_CNXSTRING = "Endpoint=sb://memeticameapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=1oxnmbuBGmEEREVPqqIhwV1ATMWMsFu2tquHaR8lPEQ=";
    private MobileServiceClient mClient;
    private MobileServiceTable<Meme> memesTable;
    private List<String> idCanalesExistentes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subir_meme_canal);

        db = new DB();

        try
        {
            idcanal = getIntent().getExtras().getString("idcanal");
            nombrecanal = getIntent().getExtras().getString("nombrecanal");
            categoria = getIntent().getExtras().getString("categoria");
            Toast.makeText(getApplicationContext(),
                    idcanal + " " +nombrecanal + " "+categoria, Toast.LENGTH_LONG)
                    .show();
        }
        catch(Exception excepcion)
        {
        }

        ImageButton btCrearCanalOK= (ImageButton)findViewById(R.id.listoMeme);
        btCrearCanalOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText Etiquetas = (EditText)findViewById(R.id.agregarEtiquetas);

                etiquetas = Etiquetas.getText().toString();

                //HAY QUE CAMBIAR A QUE ACTIVITY TE MANDA
                Intent intent = new Intent(SubirMemeCanal.this, ListaCanales.class);
                intent.putExtra("nombrecanal", nombrecanal);
                intent.putExtra("categoria", categoria);
                intent.putExtra("idcanal", idcanal);
                intent.putExtra("etiquetas", etiquetas);
                startActivity(intent);

            }
        });






    }






    //    lisviewcanales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //        @Override
    //        public void onItemClick(AdapterView<?> parent, View view, int position,
    //                                long id) {

    //            Meme item = memeadapter.getItem(position);
    //            //String idcontactos1eleccionado = item.getId();

    //            //METODO QUE DADO UN CONTACTO BUSCA EL ID DEL CHAT QUE TENEMOS EN COMUN. EN CASO DE NO EXISTIR CREA UNO.
    //            //String idchat = retornarIdChat2(item);

    //            //Intent intent = new Intent(ListaContactosActivity.this, ChatActivity.class);
    //            // intent.putExtra("idchat", idchat);
    //            // startActivity(intent);

    //            //Toast.makeText(getBaseContext(), idchat, Toast.LENGTH_SHORT).show();

    //        }
    //    });




}



