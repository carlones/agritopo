package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.singleton.Configuration;

import static br.com.neogis.agritopo.dao.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.dao.Constantes.OFFLINE;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_MAPA_MODO_REQUEST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Configuration.getInstance().LoadConfiguration(this);

        Intent intent = new Intent(getBaseContext(), MapActivity.class);
        intent.putExtra(ARG_MAPA_MODO, OFFLINE);
        startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PEGAR_MAPA_MODO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                intent.putExtra(ARG_MAPA_MODO, data.getExtras().getInt(ARG_MAPA_MODO));
                startActivityForResult(intent, PEGAR_MAPA_MODO_REQUEST);
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
}
