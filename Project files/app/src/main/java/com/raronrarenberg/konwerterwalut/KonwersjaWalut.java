package com.raronrarenberg.konwerterwalut;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class KonwersjaWalut extends AppCompatActivity {

    private String mTekst;
    private TextView mTekstPokaz;

    public void DoIt(View view) throws Exception {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse("https://www.nbp.pl/kursy/xml/a051z200313.xml");
            NodeList namelist = (NodeList) doc.getElementById("1");


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        //mTekst = "lol";//dokument.getDocumentElement().getNodeName();
        //mTekstPokaz.setText(mTekst);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konwersja_walut);
        mTekstPokaz = findViewById(R.id.siemka);

        //PARSOWANIE XML'A


        //Spinnerowa czesc
        Spinner lista_walut = (Spinner) findViewById(R.id.lista_walut);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        lista_walut.setAdapter(adapter);
    }
}
