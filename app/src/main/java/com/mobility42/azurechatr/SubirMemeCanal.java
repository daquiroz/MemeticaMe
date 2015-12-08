package com.mobility42.azurechatr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.mobility42.azurechatr.MemeViewer.MemeViewerActivity;

import java.util.List;


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
    String modo;
    String modocanal;
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
            modo = getIntent().getExtras().getString("modo");

            idcanal = getIntent().getExtras().getString("idcanal");
            nombrecanal = getIntent().getExtras().getString("nombrecanal");
            categoria = getIntent().getExtras().getString("categoria");
            modocanal = getIntent().getExtras().getString("modocanal");

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

                if(modocanal.equals("Creador")) {
                    Intent intent = new Intent(SubirMemeCanal.this, MemeViewerActivity.class);
                    intent.putExtra("nombrecanal", nombrecanal);
                    intent.putExtra("categoria", categoria);
                    intent.putExtra("idcanal", idcanal);
                    intent.putExtra("etiquetas", etiquetas);
                    intent.putExtra("modo", "Canal");
                    intent.putExtra("modocanal",modocanal);
                    startActivity(intent);

                }else if(modocanal.equals("Subir")){
                    Intent intent = new Intent(SubirMemeCanal.this, MemeViewerActivity.class);
                    intent.putExtra("nombrecanal", nombrecanal);
                    intent.putExtra("categoria", categoria);
                    intent.putExtra("idcanal", idcanal);
                    intent.putExtra("etiquetas", etiquetas);
                    intent.putExtra("modo", "Canal");
                    intent.putExtra("modocanal",modocanal);
                    startActivityForResult(intent, 9);
                }
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 9) {
            if(resultCode == RESULT_OK){
                String path =data.getStringExtra("path");
                String etiquetas =data.getStringExtra("etiquetas");
                Intent setData = new Intent();
                setData.putExtra("path", path);
                setData.putExtra("etiquetas", etiquetas);
                setResult(RESULT_OK, setData);
                finish();

                // This is temporary until we add authentication to the Android version


            }
        }
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



