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

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.singleton.Licenca;
import br.com.neogis.agritopo.singleton.Configuration;

import static br.com.neogis.agritopo.utils.Constantes.ARG_LICENCA_TIPO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LATITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LONGITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ZOOMINICIAL;
import static br.com.neogis.agritopo.utils.Constantes.OFFLINE;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MAPA_MODO_REQUEST;


public class MainActivity extends AppCompatActivity {
    private List<String> permissions;

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
        try {
            Licenca.getInstance().LoadLicenca(getApplicationContext());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        createRootDirectory();
        startMapActivity(Licenca.getInstance().getTipoAutorizado());
     }

    private void startMapActivity(Licenca.LicencaTipo licencaTipo) {
        Intent intent = new Intent(getBaseContext(), MapActivity.class);
        intent.putExtra(ARG_LICENCA_TIPO, licencaTipo.toString());
        intent.putExtra(ARG_MAPA_ID, 0);
        intent.putExtra(ARG_MAPA_LATITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_LONGITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_ZOOMINICIAL, 0);
        intent.putExtra(ARG_MAPA_MODO, OFFLINE);
        startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
    }

    private void createRootDirectory() {
        File diretorioFotos = new File(Configuration.getInstance().DiretorioFotos);
        diretorioFotos.mkdirs();
    }

    private void requestPermissions() {
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
