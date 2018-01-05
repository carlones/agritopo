package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import br.com.neogis.agritopo.R;

import static br.com.neogis.agritopo.dao.Constantes.ARG_EXPORTAR_NOME_ARQUIVO;
import static br.com.neogis.agritopo.dao.Constantes.ARG_EXPORTAR_TIPO_ARQUIVO;

public class ExportarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText edtNomeArquivo = (EditText) findViewById(R.id.edtNomeArquivo);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupTipo);
        FloatingActionButton btnSalvarArquivo = (FloatingActionButton) findViewById(R.id.btnSalvarArquivo);
        btnSalvarArquivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ARG_EXPORTAR_NOME_ARQUIVO, edtNomeArquivo.getText().toString());
                if (radioGroup.getCheckedRadioButtonId() == R.id.rdbKml) {
                    intent.putExtra(ARG_EXPORTAR_TIPO_ARQUIVO, "kml");
                }
                if (radioGroup.getCheckedRadioButtonId() == R.id.rdbGeoJson) {
                    intent.putExtra(ARG_EXPORTAR_TIPO_ARQUIVO, "geojson");
                }
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
            }
        });
    }

}
