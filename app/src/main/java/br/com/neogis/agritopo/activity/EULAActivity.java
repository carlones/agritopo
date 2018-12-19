package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.utils.Utils;

public class EULAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.eula_condicoes_de_uso);
        setSupportActionBar(toolbar);
        TextView txtEula = (TextView) findViewById(R.id.txtEula);
        txtEula.setText(Html.fromHtml(getString(R.string.texto_activity_eula)));
        FloatingActionButton fabAceitar = (FloatingActionButton) findViewById(R.id.fabAceitar);
        FloatingActionButton fabRecusar = (FloatingActionButton) findViewById(R.id.fabRecusar);
        fabAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        fabRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.alert(EULAActivity.this, "Atenção", "O aplicativo não irá funcionar sem aceitar as condições.", "OK");

                //Intent intent = new Intent();
                //setResult(RESULT_CANCELED, intent);
                //finish();
            }
        });
    }

}
