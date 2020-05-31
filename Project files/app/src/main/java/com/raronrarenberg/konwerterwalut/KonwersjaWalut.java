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
import android.widget.EditText;
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
    private EditText mEdit;
    Map<String, List<String>> waluty = new HashMap<String, List<String>>();

    public void DoIt(View view) throws Exception
    {
        TextView  wynikowy_text = (TextView) findViewById(R.id.textView4);
        EditText kwota_edit = (EditText) findViewById(R.id.text_input);
        Spinner lista_walut = (Spinner) findViewById(R.id.lista_walut);
        Spinner lista_walut2 = (Spinner) findViewById(R.id.lista_walut2);

        String pierwsza = lista_walut.getSelectedItem().toString();
        String druga = lista_walut2.getSelectedItem().toString();

        Double jeden =Double.parseDouble(String.valueOf(kwota_edit.getText()));
        Integer przelicznik = Integer.parseInt(waluty.get(pierwsza).get(0));

        String str_kurs_jeden = waluty.get(pierwsza).get(2);
        Log.e("check1",str_kurs_jeden);
        str_kurs_jeden= str_kurs_jeden.replaceAll(",",".");
        Log.e("check2",str_kurs_jeden);
        Double kurs_jeden = Double.parseDouble(str_kurs_jeden);

        String str_kurs_dwa = waluty.get(druga).get(2);
        str_kurs_dwa = str_kurs_dwa.replaceAll(",",".");
        Double kurs_dwa = Double.parseDouble(str_kurs_dwa);

        Double dwa = jeden / przelicznik;
        dwa = dwa * kurs_jeden;
        dwa = dwa / kurs_dwa;
        wynikowy_text.setText(Double.toString(dwa));

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
        List<XmlParser.Pozycja> pozycje = new ArrayList<>();

         //slownik z walutami, nazwa_waluty to klucz a reszta to wartosci
         //wartosci do slownika
        List<String> nazwy_walut = new ArrayList<String>();
        Spinner lista_walut = (Spinner) findViewById(R.id.lista_walut);
        Spinner lista_walut2 = (Spinner) findViewById(R.id.lista_walut2);


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
                List<String> wartosci = new ArrayList<String>();
                wartosci.add(pozycja.przelicznik);
                wartosci.add(pozycja.kod_waluty);
                wartosci.add(pozycja.kurs_sredni);

                waluty.put(pozycja.nazwa_waluty,wartosci);
                nazwy_walut.add(pozycja.nazwa_waluty);
                //wartosci.clear();

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


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        lista_walut.setAdapter(adapter);
        lista_walut2.setAdapter(adapter);
    }
}
