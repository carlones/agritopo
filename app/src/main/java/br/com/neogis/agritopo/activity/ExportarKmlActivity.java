package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import br.com.neogis.agritopo.R;

import static br.com.neogis.agritopo.dao.Constantes.ARG_NOME_ARQUIVO_KML;

public class ExportarKmlActivity extends AppCompatActivity {
    private EditText edtNomeArquivoKml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar_kml);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtNomeArquivoKml = (EditText) findViewById(R.id.edtNomeArquivoKml);
        FloatingActionButton btnSalvarArquivoKml = (FloatingActionButton) findViewById(R.id.btnSalvarArquivoKml);
        btnSalvarArquivoKml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ARG_NOME_ARQUIVO_KML, edtNomeArquivoKml.getText());
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
            }
        });
    }

}
