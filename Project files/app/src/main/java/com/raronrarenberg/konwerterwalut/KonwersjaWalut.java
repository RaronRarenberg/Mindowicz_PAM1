package com.raronrarenberg.konwerterwalut;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class KonwersjaWalut extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12;
    private String mTekst;
    private TextView mTekstPokaz;

    public void DoIt(View view) throws Exception
    {


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(KonwersjaWalut.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konwersja_walut);

        XmlParser parser_xml = new XmlParser();
        List<XmlParser.Pozycja> pozycje = null;

        Map<String, List<String>> waluty = new HashMap<>(); //slownik z walutami, nazwa_waluty to klucz a reszta to wartosci
        List<String> wartosci = new ArrayList<>(); //wartosci do slownika
        List<String> nazwy_walut = new ArrayList<>();

        //sciezka do pliku xml
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "tabela.xml";
        destination += fileName;
        final Uri uri = Uri.parse(destination);
        //koniec sciezki
        InputStream in = null;
        try {
            in = new FileInputStream(String.valueOf(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            pozycje = parser_xml.parse(in);
            //Log.e("p", String.valueOf(pozycje));
            for (XmlParser.Pozycja pozycja : pozycje)
            {
                wartosci.add(pozycja.przelicznik);
                wartosci.add(pozycja.kod_waluty);
                wartosci.add(pozycja.kurs_sredni);
                waluty.put(pozycja.nazwa_waluty,wartosci);
                nazwy_walut.add(pozycja.nazwa_waluty);
                wartosci.clear();

            }
            Log.e("waluty",waluty.keySet().toString());

            // ListView listView = (ListView) findViewById(R.id.list_view);
            //listView.setAdapter(adapter);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nazwy_walut);
        //Spinnerowa czesc
        Spinner lista_walut = (Spinner) findViewById(R.id.lista_walut);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        lista_walut.setAdapter(adapter);
    }
}
