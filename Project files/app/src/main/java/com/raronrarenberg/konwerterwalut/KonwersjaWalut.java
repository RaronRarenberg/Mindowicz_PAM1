package com.raronrarenberg.konwerterwalut;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class KonwersjaWalut extends AppCompatActivity {

    private String mTekst;
    private TextView mTekstPokaz;
    public void DoIt(View view)
    {
        mTekstPokaz.setText(mTekst);
        System.out.println(mTekst);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konwersja_walut);
        mTekstPokaz = findViewById(R.id.siemka);

        //PARSOWANIE XML'A


        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder buder = factory.newDocumentBuilder();
            Document dokument = buder.parse("content://com.android.externalstorage.documents/document/primary%3ADownload%2Ftabela");
            mTekst = dokument.getDocumentElement().getNodeName();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

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
