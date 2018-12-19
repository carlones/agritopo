package br.com.neogis.agritopo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.singleton.Configuration;

import static br.com.neogis.agritopo.utils.Constantes.ARG_LICENCA_TIPO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LATITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LONGITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ZOOMINICIAL;
import static br.com.neogis.agritopo.utils.Constantes.OFFLINE;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_EULA;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MAPA_MODO_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class MainActivity extends AppCompatActivity {
    private List<String> permissions;

    private SerialKeyService serialKeyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions();
        /**
         * Não implementar nada abaixo disso
         * implementar no metodo "continueonCreateAfterPermissions"
         * é chamado depois que o usuario aceita as permissoes
         * Marciel
         */
    }

    private void continueonCreateAfterPermissions() {
        Configuration.getInstance().LoadConfiguration(getApplicationContext());

        createRootDirectory();

        serialKeyService = new SerialKeyService(getApplicationContext());
        ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();

        if (chaveSerial != null)
            startMapActivity(chaveSerial.getTipo());
        else {
            if (!serialKeyService.containsChaveSerial()) {
                Intent intent = new Intent(getBaseContext(), EULAActivity.class);
                startActivityForResult(intent, PEGAR_EULA);
            } else {
                Intent intent = new Intent(getBaseContext(), SeletorLicencaActivity.class);
                startActivityForResult(intent, PEGAR_SERIAL_KEY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PEGAR_MAPA_MODO_REQUEST:
                if (resultCode == RESULT_OK) {
                    ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();
                    Intent intent = new Intent(getBaseContext(), MapActivity.class);
                    intent.putExtra(ARG_LICENCA_TIPO, chaveSerial.getTipo().toString());
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
                break;
            case PEGAR_SERIAL_KEY:
                if (resultCode == RESULT_OK) {
                    ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();
                    startMapActivity(chaveSerial.getTipo());
                }
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "A ativação é necessária.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case PEGAR_EULA:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(getBaseContext(), SeletorLicencaActivity.class);
                    startActivityForResult(intent, PEGAR_SERIAL_KEY);
                }
                if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;
        }
    }

    private void startMapActivity(ChaveSerial.LicencaTipo licencaTipo) {
        Intent intent = new Intent(getBaseContext(), MapActivity.class);
        intent.putExtra(ARG_LICENCA_TIPO, licencaTipo.toString());
        intent.putExtra(ARG_MAPA_ID, 0);
        intent.putExtra(ARG_MAPA_LATITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_LONGITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_ZOOMINICIAL, 0);
        intent.putExtra(ARG_MAPA_MODO, OFFLINE);
        startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
    }

    private void startSerialActivity() {
        Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
        startActivityForResult(intent, PEGAR_SERIAL_KEY);
    }

    private void createRootDirectory() {
        File diretorioFotos = new File(Configuration.getInstance().DiretorioFotos);
        diretorioFotos.mkdirs();
    }

    private void requestPermissions() {
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.INTERNET);
        requestSpecificPermission(0);
    }

    private void requestSpecificPermission(int index) {
        if (permissions.size() > index) {
            String permission = permissions.get(index);
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, index);
            } else
                requestSpecificPermission(index + 1);
        } else
            continueonCreateAfterPermissions();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestSpecificPermission(requestCode + 1);
        } else {
            if (permissions.length > 0) {
                boolean should = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (should) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle(getBaseContext().getString(R.string.permissao_negada))
                            .setMessage(getBaseContext().getString(R.string.permissao_negada_mensagem))
                            .setPositiveButton(getBaseContext().getString(R.string.permissao_tentar_novamente), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    requestSpecificPermission(requestCode);
                                }
                            })
                            .setNegativeButton(getBaseContext().getString(R.string.permissao_sair), new DialogInterface.OnClickListener() {
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
