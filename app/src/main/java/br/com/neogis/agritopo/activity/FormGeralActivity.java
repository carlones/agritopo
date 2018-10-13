package br.com.neogis.agritopo.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import br.com.neogis.agritopo.R;

/**
 * Herde dessa classe para construir formulários mais facilmente.
 *
 * Você ganha de brinde um layout com toolbar e um botão de confirmar.
 *
 * Você precisará:
 * - fazer override do método getLayoutFormulario() e retornar o seu layout de formulário
 * - dar um setTitle()
 * - estender o método confirmar(View), que é chamado quando o usuário clica em confirmar
 *
 * Veja ColetarDadosPessoaActivity para pegar um exemplo
 */

public class FormGeralActivity extends AppCompatActivity {
    protected int layoutId = R.layout.form_geral;
    protected int toolbarId = R.id.toolbar;
    protected int frameConteudoId = R.id.frame_conteudo;

    protected AppBarLayout appbar;
    protected Toolbar toolbar;
    protected LinearLayout frameConteudo;
    protected FloatingActionButton botaoConfimar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layoutId);

        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        // Aqui inserimos o layout do formulário definido na classe filha
        // dentro do nosso placeholder (frameConteudoId)
        frameConteudo = (LinearLayout) findViewById(frameConteudoId);
        getLayoutInflater().inflate(getLayoutFormulario(), frameConteudo);

        appbar = (AppBarLayout) findViewById(R.id.app_bar);
        botaoConfimar = (FloatingActionButton) findViewById(R.id.botao_confirmar);
    }

    protected int getLayoutFormulario() {
        return 0;
    }

    public void confirmar(View v) {}
}

