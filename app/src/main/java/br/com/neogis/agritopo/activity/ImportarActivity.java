package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.neogis.agritopo.R;

import static br.com.neogis.agritopo.dao.Constantes.ARG_IMPORTAR_NOME_ARQUIVO;

public class ImportarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final EditText edtArquivoImportar = (EditText) findViewById(R.id.edtArquivoImportar);
        FloatingActionButton btnSalvarArquivo = (FloatingActionButton) findViewById(R.id.btnSalvarArquivo);
        btnSalvarArquivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ARG_IMPORTAR_NOME_ARQUIVO, edtArquivoImportar.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Importar Arquivo");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
