package com.raronrarenberg.konwerterwalut;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return czytaj_plik(parser);
        } finally {
            in.close();
        }
    }

    private List czytaj_plik(XmlPullParser parser) throws XmlPullParserException, IOException {
        List pozycje = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "tabela_kursow");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("pozycja")) {
                pozycje.add(czytaj_pozycje(parser));
            } else {
                skip(parser);
            }
        }
        return pozycje;
    }
    public static class  Pozycja
    {
        public final String nazwa_waluty;
        public final String przelicznik;
        public final String kod_waluty;
        public final String kurs_sredni;

        private Pozycja(String nazwa_waluty,String przelicznik, String kod_waluty ,String kurs_sredni)
        {
            this.nazwa_waluty = nazwa_waluty;
            this.przelicznik = przelicznik;
            this.kod_waluty = kod_waluty;
            this.kurs_sredni = kurs_sredni;
        }
    }
    private Pozycja czytaj_pozycje(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pozycja");
        String nazwa_waluty = null;
        String przelicznik = null;
        String kod_waluty = null;
        String kurs_sredni = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("nazwa_waluty"))
            {
                nazwa_waluty = czytaj_nazwe(parser);
            }
            else if (name.equals("przelicznik"))
            {
                przelicznik = czytaj_przelicznik(parser);
            }
            else if (name.equals("kod_waluty"))
            {
                kod_waluty = czytaj_kod(parser);
            }
            else if (name.equals("kurs_sredni"))
            {
                kurs_sredni = czytaj_kurs(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Pozycja(nazwa_waluty,przelicznik,  kod_waluty , kurs_sredni);
    }

    private String czytaj_nazwe(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nazwa_waluty");
        String nazwa_waluty = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nazwa_waluty");
        return nazwa_waluty;
    }

    private String czytaj_przelicznik(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "przelicznik");
        String przelicznik = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "przelicznik");
        return przelicznik;
    }

    private String czytaj_kod(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "kod_waluty");
        String kod_waluty = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "kod_waluty");
        return kod_waluty;
    }

    private String czytaj_kurs(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "kurs_sredni");
        String kurs_sredni = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "kurs_sredni");
        return kurs_sredni;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String wynik = "";
        if (parser.next() == XmlPullParser.TEXT) {
            wynik = parser.getText();
            parser.nextTag();
        }
        return wynik;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
