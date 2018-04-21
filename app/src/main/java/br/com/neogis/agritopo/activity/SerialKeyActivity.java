package br.com.neogis.agritopo.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.runnable.CamadasLoader;
import br.com.neogis.agritopo.runnable.SerialKeyValidate;


public class SerialKeyActivity extends AppCompatActivity implements SerialKeyValidate.OnSerialValidate {
    private EditText serialEdit;
    private EditText emailEdit;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_key);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Ativação");

        serialEdit = (EditText) findViewById(R.id.serial_key_edit);
        emailEdit = (EditText) findViewById(R.id.serial_email_edit);

        Button okButton = (Button) findViewById(R.id.serial_key_button_pronto);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateSerialKey(serialEdit.getText().toString(), emailEdit.getText().toString());
            }
        });

        getTelephonyManager();
    }

    private void validateSerialKey(String serial, String email) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        showProgressDialog(
                getApplicationContext(),
                new SerialKeyValidate(
                        serial,
                        email,
                        telephonyManager.getDeviceId(),
                        this),
                "Processando Ativação...");

    }

    private void showProgressDialog(final Context context, final Runnable runnable, String text) {
        final ProgressDialog ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.setMessage(text);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                ringProgressDialog.dismiss();
            }
        });
        th.setPriority(Thread.MAX_PRIORITY);
        th.start();
    }

    @Override
    public void onSucess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFail() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void getTelephonyManager(){
        if ((int) Build.VERSION.SDK_INT < 23)
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }
            else
                telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                else                {
                    boolean should = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE);
                    if(should)
                    {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle("Permissão Negada")
                            .setMessage("Sem Esta permissão você não poderá utilizar o aplicativo.")
                            .setPositiveButton("Tentar Novamente.", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getTelephonyManager();
                                }
                            })
                            .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                    }
                    else                    {
                        finish();
                    }
                }
                break;
            }
        }
    }

}
