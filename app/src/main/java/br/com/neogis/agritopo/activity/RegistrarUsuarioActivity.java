package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.PESSOA_AREA_ATUACAO;
import static br.com.neogis.agritopo.utils.Constantes.PESSOA_EMAIL;
import static br.com.neogis.agritopo.utils.Constantes.PESSOA_EMPRESA;
import static br.com.neogis.agritopo.utils.Constantes.PESSOA_INFORMAR_MUNICIPIO;
import static br.com.neogis.agritopo.utils.Constantes.PESSOA_MUNICIPIO;
import static br.com.neogis.agritopo.utils.Constantes.PESSOA_MUNICIPIO_ID;
import static br.com.neogis.agritopo.utils.Constantes.PESSOA_NOME;

public class RegistrarUsuarioActivity extends FormGeralActivity {
    EditText inputNome;
    EditText inputEmail;
    EditText inputAreaAtuacao;
    EditText inputEmpresa;
    EditText inputMunicipio;

    int municipioId;

    @Override
    protected int getLayoutFormulario() {
        return R.layout.activity_registrar_usuario_form;
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

        inputNome.setText(getIntent().getStringExtra(PESSOA_NOME));
        inputEmail.setText(getIntent().getStringExtra(PESSOA_EMAIL));
        inputAreaAtuacao.setText(getIntent().getStringExtra(PESSOA_AREA_ATUACAO));
        inputEmpresa.setText(getIntent().getStringExtra(PESSOA_EMPRESA));
        inputMunicipio.setText(getIntent().getStringExtra(PESSOA_MUNICIPIO));

        municipioId = getIntent().getIntExtra(PESSOA_MUNICIPIO_ID, 0);

        inputMunicipio.setKeyListener(null); // desabilitar edição
        inputMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SelecionarMunicipioActivity.class);
                startActivityForResult(intent, PESSOA_INFORMAR_MUNICIPIO);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PESSOA_INFORMAR_MUNICIPIO) {
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
        String municipio = inputMunicipio.getText().toString();

        if( nome.trim().isEmpty() ) {
            Utils.toast(getBaseContext(), getString(R.string.registrar_usuario_erro_nome_deve_ser_preenchido));
            return;
        }
        if( email.trim().isEmpty() ) {
            Utils.toast(getBaseContext(), getString(R.string.registrar_usuario_erro_email_deve_ser_preenchido));
            return;
        }
        if( areaAtuacao.trim().isEmpty() ) {
            Utils.toast(getBaseContext(), getString(R.string.registrar_usuario_erro_area_de_atuacao_deve_ser_preenchido));
            return;
        }

        Intent result = new Intent();
        result.putExtra(PESSOA_NOME, nome);
        result.putExtra(PESSOA_EMAIL, email);
        result.putExtra(PESSOA_AREA_ATUACAO, areaAtuacao);
        result.putExtra(PESSOA_EMPRESA, empresa);
        result.putExtra(PESSOA_MUNICIPIO, municipio);
        result.putExtra(PESSOA_MUNICIPIO_ID, municipioId);
        setResult(RESULT_OK, result);
        finish();
    }
}
