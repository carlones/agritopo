package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;
import br.com.neogis.agritopo.fragment.ElementoDetailFragment;

import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTO_CENTRALIZAR;
import static br.com.neogis.agritopo.dao.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.dao.Constantes.ARG_POSICAO_LISTA;
import static br.com.neogis.agritopo.dao.Constantes.ARG_TIPOELEMENTOID;

/**
 * An activity representing a single Elemento detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CadastrosListarActivity}.
 */
public class ElementoDetailActivity extends AppCompatActivity {
    private ElementoDetailFragment fragment;
    private int elementoId;
    private int tipoelementoId;
    private int classeId;
    private String geometria;
    private int posicao_lista;
    private Elemento mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        elementoId = getIntent().getIntExtra(ARG_ELEMENTOID, 0);
        tipoelementoId = getIntent().getIntExtra(ARG_TIPOELEMENTOID, 0);
        classeId = getIntent().getIntExtra(ARG_CLASSEID, 0);
        geometria = getIntent().getStringExtra(ARG_GEOMETRIA);
        posicao_lista = getIntent().getIntExtra(ARG_POSICAO_LISTA, -1);

        FloatingActionButton btnElementoSalvar = (FloatingActionButton) findViewById(R.id.btnElementoSalvar);
        btnElementoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravarElemento();
                Intent intent = new Intent();
                intent.putExtra(ARG_ELEMENTOID, mItem.getElementoid());
                intent.putExtra(ARG_POSICAO_LISTA, posicao_lista);
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
            }
        });
        FloatingActionButton btnElementoFocar = (FloatingActionButton) findViewById(R.id.btnElementoFocar);
        btnElementoFocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ARG_ELEMENTOID, elementoId);
                intent.putExtra(ARG_ELEMENTO_CENTRALIZAR, 1);
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
                //Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        btnElementoFocar.setVisibility((elementoId == 0 ? View.INVISIBLE : View.VISIBLE));

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_ELEMENTOID, elementoId);
            arguments.putInt(ARG_TIPOELEMENTOID, tipoelementoId);
            arguments.putInt(ARG_CLASSEID, classeId);
            arguments.putString(ARG_GEOMETRIA, geometria);
            fragment = new ElementoDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.elemento_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); //finaliza a activity atual, retornando codigo de cancelamento(0), e direcionando para a ultima activity do callstack
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gravarElemento() {
        String titulo = fragment.getTitulo();
        String descricao = fragment.getDescricao();
        String nomeTipoElemento = fragment.getNomeTipoElemento();
        TipoElementoDao tipoElementoDao = new TipoElementoDaoImpl(getBaseContext());
        TipoElemento tipoElemento = tipoElementoDao.getByNome(nomeTipoElemento);
        if (tipoElemento == null) {
            tipoElemento = new TipoElemento(nomeTipoElemento);
            tipoElementoDao.insert(tipoElemento);
        }
        mItem = fragment.getElemento();
        mItem.setTitulo(titulo);
        mItem.setDescricao(descricao);
        mItem.setTipoElemento(tipoElemento);

        ElementoDao elementoDao = new ElementoDaoImpl(getBaseContext());
        if (elementoId != 0) {
            elementoDao.update(mItem);
        } else {
            mItem.setGeometria(geometria);
            elementoDao.insert(mItem);
        }
    }
}
