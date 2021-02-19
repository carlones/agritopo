package br.com.neogis.agritopo.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_CHAVE;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_MANUAL;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class SeletorLicencaActivity extends AppCompatActivity {
    private ProgressDialog ringProgressDialog;
    private EditText edtDeviceId;
    private EditText caminhoArquivoChave;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seletor_licenca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtDeviceId = (EditText) findViewById(R.layout.edtDeviceId);
        caminhoArquivoChave = (EditText) findViewById(R.layout.txtCaminho);

        edtDeviceId.setText(Utils.getDeviceId(this));
        caminhoArquivoChave.setText(Configuration.getInstance().DiretorioLeituraArquivos + File.separator + "chave.ngs");
        boolean manual = (getIntent().getIntExtra(ARG_SERIALKEY_MANUAL, 0) == 1);
        String chave = getIntent().getStringExtra(ARG_SERIALKEY_CHAVE);

        final SerialKeyService serialKeyService = new SerialKeyService(getBaseContext());
        ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();

        Button btnValidarLicenca = (Button) findViewById(R.id.btnValidarLicenca);
        btnValidarLicenca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                showProgressDialog();
                if (serial.expiration.getTime() < DateUtils.getCurrentDate().getTime())
                    throw new Exception(context.getString(R.string.erro_ao_validar_licenca));
                Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
                intent.putExtra(ARG_SERIALKEY_EMAIL, getIntent().getStringExtra(ARG_SERIALKEY_EMAIL));
                intent.putExtra(ARG_SERIALKEY_CHAVE, getIntent().getStringExtra(ARG_SERIALKEY_CHAVE));
                intent.putExtra(ARG_SERIALKEY_MANUAL, getIntent().getStringExtra(ARG_SERIALKEY_MANUAL));
                startActivityForResult(intent, PEGAR_SERIAL_KEY);*/
            }
        });
        Button btnVersaoGratuita = (Button) findViewById(R.id.btnVersaoGratuita);
        btnVersaoGratuita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*ChaveSerial serialKeyService = new ChaveSerial;
                SerialKeyView serialKeyView = new SerialKeyView();
                serialKeyView.id = 0;
                serialKeyView.licencaTipo = new LicencaTipoView();
                serialKeyView.licencaTipo.id = 1;
                serialKeyView.licencaTipo.descricao = "Gratuito";
                serialKeyView.licencaTipo.validadeDias = 999999;
                serialKeyView.key = "GRATUITO";
                serialKeyView.user = new UserView();
                serialKeyView.user.email = "contato@neogis.com.br";
                serialKeyView.proprietario = new UserView();
                serialKeyView.proprietario.email = "contato@neogis.com.br";
                try {
                    serialKeyView.expiration = DateUtils.convertoToDateddMMyyyy("31/12/2500");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                serialKeyService.setChaveSerial(serialKeyView);
                finalizar(true);*/
            }
        });

        if (manual) {
            if (!chave.equals("GRATUITO")) {
                /*Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
                intent.putExtra(ARG_SERIALKEY_EMAIL, email);
                intent.putExtra(ARG_SERIALKEY_CHAVE, chave);
                intent.putExtra(ARG_SERIALKEY_MANUAL, 1);
                startActivityForResult(intent, PEGAR_SERIAL_KEY);*/
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PEGAR_SERIAL_KEY:
                if (resultCode == RESULT_OK) {
                    finalizar(true);
                }
                if (resultCode == RESULT_CANCELED) {
                    //finalizar(false);
                }
                break;
        }
    }

    public void finalizar(boolean sucesso) {
        Intent intent = new Intent();
        if (sucesso)
            setResult(RESULT_OK, intent);
        else
            setResult(RESULT_CANCELED, intent);
        finish();
        try {
            onBackPressed();
        } catch (Exception e) {

        }
    }

    public void composeEmail(String[] addresses, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showProgressDialog() {
        ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.setMessage(getString(R.string.processando_ativacao));
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
    }

    private void mostrarRetorno(String msg_erro) {
        ringProgressDialog.dismiss();
        if (msg_erro == null) {
            Utils.toast(this, getString(R.string.licenca_validada_com_sucesso));
            finalizar(true);
        } else {
            Utils.toast(this, msg_erro);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    telephonyManager = Utils.getTelephonyManager(this);
                } else {
                    boolean should = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE);
                    if (should) {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle(getBaseContext().getString(R.string.permissao_negada))
                                .setMessage(getBaseContext().getString(R.string.permissao_negada_mensagem))
                                .setPositiveButton(getBaseContext().getString(R.string.permissao_tentar_novamente), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Utils.getTelephonyManager(getParent());
                                    }
                                })
                                .setNegativeButton(getBaseContext().getString(R.string.permissao_sair), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finalizar(false);
                                    }
                                }).show();
                    } else {
                        finalizar(true);
                    }
                }
                break;
            }
        }
    }
}
