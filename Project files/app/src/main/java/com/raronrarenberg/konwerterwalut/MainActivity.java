package com.raronrarenberg.konwerterwalut;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE_CODE = 1000;
    long id_w_kolejce;
    DownloadManager dm;
    private Button przycisk_do_walut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        przycisk_do_walut = (Button) findViewById(R.id.button2);
        przycisk_do_walut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Otworz_Activity_Konwersji();
            }
        });
    }

    public void Otworz_Activity_Konwersji()
    {
        Intent intent = new Intent(this,KonwersjaWalut.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Klik(View widok)
    {
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            String[]  pozwolenie = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //pop up proszący o pozwolenie na zapis
            requestPermissions(pozwolenie, PERMISSION_STORAGE_CODE);
        }
        else
        {
            Pobierz_Waluty();
        }
    }
    public void Pobierz_Waluty()
    {
        dm = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://www.nbp.pl/kursy/xml/a051z200313.xml");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Pobieranie");
        request.setTitle("Pobieram kursy walut");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"tabela");
        dm.enqueue(request);
    }

    public void Rezultat_Otrzymanego_Pozwolenia(int kod_pozwolenia, @NonNull String[] pozwolenie, @NonNull int[] rezultaty_pozwolenia)
    {
        switch (kod_pozwolenia)
        {
            case PERMISSION_STORAGE_CODE:
            {
                if(rezultaty_pozwolenia.length > 0 && rezultaty_pozwolenia[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Pobierz_Waluty();
                }
                else
                {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
