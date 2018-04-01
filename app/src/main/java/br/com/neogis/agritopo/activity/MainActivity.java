package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.singleton.Configuration;

import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LATITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LONGITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ZOOMINICIAL;
import static br.com.neogis.agritopo.utils.Constantes.OFFLINE;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MAPA_MODO_REQUEST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Configuration.getInstance().LoadConfiguration(this);

        Intent intent = new Intent(getBaseContext(), MapActivity.class);
        intent.putExtra(ARG_MAPA_ID, 0);
        intent.putExtra(ARG_MAPA_LATITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_LONGITUDEATUAL, 0.0);
        intent.putExtra(ARG_MAPA_ZOOMINICIAL, 0);
        intent.putExtra(ARG_MAPA_MODO, OFFLINE);
        startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
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
        }
    }
}
