package br.com.neogis.agritopo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;

/**
 * An activity representing a single Elemento detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ElementoListActivity}.
 */
public class ElementoDetailActivity extends AppCompatActivity {

    private ElementoDetailFragment fragment;
    private int elementoId;
    private int classeId;
    private String geometria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        elementoId = getIntent().getIntExtra(ElementoDetailFragment.ARG_ELEMENTOID, 0);
        classeId = getIntent().getIntExtra(ElementoDetailFragment.ARG_CLASSEID, 0);
        geometria = getIntent().getStringExtra(ElementoDetailFragment.ARG_GEOMETRIA);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravarElemento();
                finish();
                onBackPressed();
                //Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

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
            arguments.putInt(ElementoDetailFragment.ARG_ELEMENTOID, elementoId);
            fragment = new ElementoDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.elemento_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ElementoListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gravarElemento() {
        String titulo = fragment.getTitulo();
        String descricao = fragment.getDescricao();
        String nomeTipoElemento = fragment.getNomeTipoElemento();
        Elemento mItem = fragment.getElemento();
        TipoElementoDao tipoElementoDao = new TipoElementoDaoImpl(getBaseContext());
        TipoElemento tipoElemento = tipoElementoDao.getByNome(nomeTipoElemento);
        if (tipoElemento == null) {
            tipoElemento = new TipoElemento(nomeTipoElemento);
            tipoElementoDao.insert(tipoElemento);
        }

        ElementoDao elementoDao = new ElementoDaoImpl(getBaseContext());
        if (elementoId != 0) {
            mItem.setTitulo(titulo);
            mItem.setDescricao(descricao);
            mItem.setTipoElemento(tipoElemento);
            elementoDao.update(mItem);
        } else {
            Elemento e = new Elemento(tipoElemento, mItem.getClasse(), titulo, descricao, geometria);
            elementoDao.insert(e);
        }
    }
}