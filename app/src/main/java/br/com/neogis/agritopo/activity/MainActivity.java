package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.singleton.Configuration;

import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LATITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LONGITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ZOOMINICIAL;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MAPA_MODO_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.OFFLINE;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY_TRIAL;

public class MainActivity extends AppCompatActivity {
    private SerialKeyService serialKeyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Configuration.getInstance().LoadConfiguration(this);
        serialKeyService = new SerialKeyService(getApplicationContext());

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
                Toast.makeText(this, "Necessária ativação.", Toast.LENGTH_LONG).show();
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
}
