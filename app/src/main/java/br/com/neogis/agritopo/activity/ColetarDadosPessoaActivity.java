package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.neogis.agritopo.R;

public class ColetarDadosPessoaActivity extends FormGeralActivity {

    public static final String NOME         = "nome";
    public static final String EMAIL        = "email";
    public static final String AREA_ATUACAO = "area_atuacao";
    public static final String EMPRESA      = "empresa";
    public static final String MUNICIPIO    = "municipio";
    public static final String MUNICIPIO_ID = "municipio_id";

    public static final int INFORMAR_MUNICIPIO = 1;

    EditText inputNome;
    EditText inputEmail;
    EditText inputAreaAtuacao;
    EditText inputEmpresa;
    EditText inputMunicipio;

    int municipioId;

    @Override
    protected int getLayoutFormulario() {
        return R.layout.activity_coletar_dados_pessoa_form;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Informe seus dados");

        // para que o formulário caiba inteiramente na tela ao carregar
        appbar.setExpanded(false);

        inputNome        = (EditText) findViewById(R.id.nome       );
        inputEmail       = (EditText) findViewById(R.id.email      );
        inputAreaAtuacao = (EditText) findViewById(R.id.areaAtuacao);
        inputEmpresa     = (EditText) findViewById(R.id.empresa    );
        inputMunicipio   = (EditText) findViewById(R.id.municipio  );

        inputNome       .setText(getIntent().getStringExtra(NOME        ));
        inputEmail      .setText(getIntent().getStringExtra(EMAIL       ));
        inputAreaAtuacao.setText(getIntent().getStringExtra(AREA_ATUACAO));
        inputEmpresa    .setText(getIntent().getStringExtra(EMPRESA     ));
        inputMunicipio  .setText(getIntent().getStringExtra(MUNICIPIO   ));

        municipioId = getIntent().getIntExtra(MUNICIPIO_ID, 0);

        inputMunicipio.setKeyListener(null); // desabilitar edição
        inputMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SelecionarMunicipioActivity.class);
                startActivityForResult(intent, INFORMAR_MUNICIPIO);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == INFORMAR_MUNICIPIO ) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                municipioId = data.getIntExtra(SelecionarMunicipioActivity.MUNICIPIO_ID, 0);
                String municipio = data.getStringExtra(SelecionarMunicipioActivity.MUNICIPIO);
                String sigla = data.getStringExtra(SelecionarMunicipioActivity.ESTADO_SIGLA);
                inputMunicipio.setText(municipio + " - " + sigla);
            }
        }
    }

    public void confirmar(View v) {
        String nome        = inputNome       .getText().toString();
        String email       = inputEmail      .getText().toString();
        String areaAtuacao = inputAreaAtuacao.getText().toString();
        String empresa     = inputEmpresa    .getText().toString();

        if( nome.trim().isEmpty() ) {
            avisar("Nome deve ser preenchido");
            return;
        }
        if( email.trim().isEmpty() ) {
            avisar("Email deve ser preenchido");
            return;
        }
        if( areaAtuacao.trim().isEmpty() ) {
            avisar("Área de atuação deve ser preenchida");
            return;
        }

        Intent result = new Intent();
        result.putExtra(NOME        , nome       );
        result.putExtra(EMAIL       , email      );
        result.putExtra(AREA_ATUACAO, areaAtuacao);
        result.putExtra(EMPRESA     , empresa    );
        result.putExtra(MUNICIPIO_ID, municipioId);
        setResult(RESULT_OK, result);
        finish();
    }

    private void avisar(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
