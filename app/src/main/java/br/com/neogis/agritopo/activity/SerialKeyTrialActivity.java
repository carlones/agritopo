package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.service.SerialKeyService;

import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class SerialKeyTrialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_key_trial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Agritopo (Licença Temporária)");

        Button botaoAtivar = (Button)findViewById(R.id.serial_key_free_button_ativar);
        botaoAtivar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
            startActivityForResult(intent, PEGAR_SERIAL_KEY);
            }
        });

        Button botaoContinuar = (Button)findViewById(R.id.serial_key_free_button_continuar);
        botaoContinuar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        SerialKeyService service = new SerialKeyService(getApplicationContext(), this);

        TextView textTempo = (TextView) findViewById(R.id.serial_key_free_text_tempo);
        textTempo.setText("Você tem " + service.getFreeTimeDays() + " dias gratuitos restantes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PEGAR_SERIAL_KEY){
            if(resultCode == RESULT_OK) {
                setResult(resultCode);
                finish();
            }
        }
    }

}
