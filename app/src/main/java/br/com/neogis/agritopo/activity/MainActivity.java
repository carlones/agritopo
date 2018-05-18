package br.com.neogis.agritopo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.singleton.Configuration;

import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LATITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LONGITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ZOOMINICIAL;
import static br.com.neogis.agritopo.utils.Constantes.OFFLINE;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MAPA_MODO_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY_TRIAL;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_COARSE_LOCATION = 2;
    private static final int REQUEST_FINE_LOCATION = 2;
    private static final int REQUEST_WIFI_STATE = 2;
    private static final int REQUEST_NETWORK_STATE = 2;
    private static final int REQUEST_INTERNET = 2;

    private SerialKeyService serialKeyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions();

        Configuration.getInstance().LoadConfiguration(getApplicationContext());

        createRootDirectory();

        serialKeyService = new SerialKeyService(getApplicationContext(), this);

        if(serialKeyService.containsValidSerialKey())
            startMapActivity();
        else if(serialKeyService.containsFreeSerialKey())
            startTrialSerialActivity();
        else
            startSerialActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PEGAR_MAPA_MODO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                intent.putExtra(ARG_MAPA_ID, data.getExtras().getInt(ARG_MAPA_ID));
                intent.putExtra(ARG_MAPA_LATITUDEATUAL, data.getExtras().getDouble(ARG_MAPA_LATITUDEATUAL));
                intent.putExtra(ARG_MAPA_LONGITUDEATUAL, data.getExtras().getDouble(ARG_MAPA_LONGITUDEATUAL));
                intent.putExtra(ARG_MAPA_ZOOMINICIAL, data.getExtras().getInt(ARG_MAPA_ZOOMINICIAL));
                intent.putExtra(ARG_MAPA_MODO, data.getExtras().getInt(ARG_MAPA_MODO));
                startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }else if(requestCode == PEGAR_SERIAL_KEY){
            if (resultCode == RESULT_OK)
                if(serialKeyService.containsValidSerialKey())
                    startMapActivity();
            if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "A ativação é necessária.", Toast.LENGTH_LONG).show();
                finish();
            }
        }else if(requestCode == PEGAR_SERIAL_KEY_TRIAL)
        {
            if (resultCode == RESULT_OK)
                if(serialKeyService.containsValidSerialKey() || serialKeyService.containsFreeSerialKey())
                    startMapActivity();
            if(resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void startMapActivity(){
        Intent intent = new Intent(getBaseContext(), MapActivity.class);
        intent.putExtra(ARG_MAPA_ID, 0);
        intent.putExtra(ARG_MAPA_LATITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_LONGITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_ZOOMINICIAL, 0);
        intent.putExtra(ARG_MAPA_MODO, OFFLINE);
        startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
    }

    private void startSerialActivity(){
        Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
        startActivityForResult(intent, PEGAR_SERIAL_KEY);
    }

    private void startTrialSerialActivity(){
        Intent intent = new Intent(getBaseContext(), SerialKeyTrialActivity.class);
        startActivityForResult(intent, PEGAR_SERIAL_KEY_TRIAL);
    }

    private void createRootDirectory(){
        File diretorioFotos = new File(Configuration.getInstance().DiretorioFotos);
        diretorioFotos.mkdirs();
    }

    private void requestPermissions(){
        requestSpecificPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_EXTERNAL_STORAGE);
        requestSpecificPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_COARSE_LOCATION);
        requestSpecificPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        requestSpecificPermission(Manifest.permission.ACCESS_WIFI_STATE, REQUEST_WIFI_STATE);
        requestSpecificPermission(Manifest.permission.ACCESS_NETWORK_STATE, REQUEST_NETWORK_STATE);
        requestSpecificPermission(Manifest.permission.INTERNET, REQUEST_INTERNET);
    }

    private void requestSpecificPermission(String permission, int code){
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, code);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[], int[] grantResults)
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {

        }
        else {
            if(permissions.length > 0) {
                boolean should = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (should) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Permissão Negada")
                            .setMessage("Sem esta permissão você terá problemas ao utilizar o aplicativo.")
                            .setPositiveButton("Tentar novamente.", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    requestSpecificPermission(permissions[0], requestCode);
                                }
                            })
                            .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                } else {
                    finish();
                }
            }
        }
    }
}
