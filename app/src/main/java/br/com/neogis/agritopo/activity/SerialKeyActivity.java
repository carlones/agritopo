package br.com.neogis.agritopo.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.UsuarioDaoImpl;
import br.com.neogis.agritopo.runnable.SerialKeyValidate;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_CHAVE;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_EMAIL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_MANUAL;


public class SerialKeyActivity extends AppCompatActivity /*implements SerialKeyValidate.OnSerialValidate*/ {
    private EditText serialEdit;
    private EditText emailEdit;
    private TelephonyManager telephonyManager;
    private ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_key);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean manual = (getIntent().getIntExtra(ARG_SERIALKEY_MANUAL, 0) == 1);
        String email = getIntent().getStringExtra(ARG_SERIALKEY_EMAIL);
        String chave = getIntent().getStringExtra(ARG_SERIALKEY_CHAVE);

        setTitle(getString(R.string.agritopo_ativacao));
        final SerialKeyService serialKeyService = new SerialKeyService(getBaseContext());
        ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();

        serialEdit = (EditText) findViewById(R.id.serial_key_edit);
        emailEdit = (EditText) findViewById(R.id.serial_email_edit);

        Button okButton = (Button) findViewById(R.id.serial_key_button_pronto);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateSerialKey(serialEdit.getText().toString(), emailEdit.getText().toString());
            }
        });

        getTelephonyManager();

        if ((chaveSerial != null) || (manual)) {
            if (!chaveSerial.getChave().equals("GRATUITO")) {
                setTitle(getString(R.string.agritopo_reativacao));
                if (manual) {
                    serialEdit.setText(chave);
                    emailEdit.setText(email);
                } else {
                    UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl(getBaseContext());
                    serialEdit.setText(chaveSerial.getChave());
                    emailEdit.setText(usuarioDao.get(chaveSerial.getUsuarioId()).getEmail());
                    validateSerialKey(serialEdit.getText().toString(), emailEdit.getText().toString());
                }
            }
        }
    }

    private void validateSerialKey(String serial, String email) {
        String deviceId = getDeviceId();
        if (deviceId == null)
            return;

        showProgressDialog();
        new ValidarSerialEmBackground(
                serial,
                email,
                deviceId,
                this
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private String getDeviceId() {

        // IMEI para GSM, MEID/ESN para CDMA. Nem todos os aparelhos possuem chip de telefonia
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId != null) {
            Utils.info("Telephony:" + deviceId);
            return "Telephony:" + deviceId;
        }

        // Alguns aparelhos deixam valores sem nexo nesse campo (tablet do Carlos)
        String serial = android.os.Build.SERIAL;
        if (serial != null && !serial.equals("0123456789ABCDEF")) {
            Utils.info("AndroidSerial:" + serial);
            return "AndroidSerial:" + serial;
        }

        // Evitando endereço MAC: se o wifi não estiver ativo, o endereço não será retornado
        // https://stackoverflow.com/questions/11705906/programmatically-getting-the-mac-of-an-android-device

        // Muda a cada formatação
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Utils.info("Secure android id: " + androidId);
        return "SecureAndroidId:" + androidId;
    }

    private void showProgressDialog() {
        ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.setMessage("Processando Ativação...");
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
    }

    private void mostrarRetorno(String msg_erro) {
        ringProgressDialog.dismiss();
        if (msg_erro == null) {
            Utils.toast(this, "Licença validada com sucesso!");
            finalizar(true);
        } else {
            Utils.toast(this, msg_erro);
        }
    }

    private void getTelephonyManager() {
        if (Build.VERSION.SDK_INT < 23)
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            } else
                telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                else {
                    boolean should = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE);
                    if (should) {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle(getBaseContext().getString(R.string.permissao_negada))
                                .setMessage(getBaseContext().getString(R.string.permissao_negada_mensagem))
                                .setPositiveButton(getBaseContext().getString(R.string.permissao_tentar_novamente), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getTelephonyManager();
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

    public void finalizar(boolean sucesso) {
        Intent intent = new Intent();
        //intent.putExtra(ARG_IMPORTAR_NOME_ARQUIVO, edtArquivoImportar.getText().toString());
        if (sucesso)
            setResult(RESULT_OK, intent);
        else
            setResult(RESULT_CANCELED, intent);
        finish();
        onBackPressed();
    }

    public class ValidarSerialEmBackground extends AsyncTask<Void, Void, String> {
        private String serialKey;
        private String email;
        private String deviceId;
        private String erro;
        private Context context;

        ValidarSerialEmBackground(String serialKey, String email, String deviceId, Context context) {
            this.serialKey = serialKey;
            this.email = email;
            this.deviceId = deviceId;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                new SerialKeyValidate(
                        context,
                        serialKey,
                        email,
                        deviceId
                ).run();
            } catch (Exception e) {
                erro = e.getMessage();
            }
            return erro;
        }

        @Override
        protected void onPostExecute(String result) {
            mostrarRetorno(erro);
        }
    }

}
