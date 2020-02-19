package br.com.neogis.agritopo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaClasseDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;
import br.com.neogis.agritopo.fragment.ElementoDetailFragment;
import br.com.neogis.agritopo.fragment.ElementoDetailGeomFragment;

import static br.com.neogis.agritopo.dao.tabelas.ClasseEnum.PONTO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTO_CENTRALIZAR;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_POSICAO_LISTA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_TIPOELEMENTOID;

/**
 * An activity representing a single Elemento detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CadastrosListarActivity}.
 */
public class ElementoDetailActivity extends AppCompatActivity {
    private ElementoDetailFragment fragment;
    private ElementoDetailGeomFragment fragmentGeom;
    private boolean geomAtivado;
    private int elementoId;
    private String geometria;
    private int posicao_lista;
    private Elemento mItem;
    private String tagFragmento = "tag_fragmento_elemento";
    private String tagFragmentoGeom = "tag_fragmento_elemento_geom";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        elementoId = getIntent().getIntExtra(ARG_ELEMENTOID, 0);
        int tipoelementoId = getIntent().getIntExtra(ARG_TIPOELEMENTOID, 0);
        int classeId = getIntent().getIntExtra(ARG_CLASSEID, 0);
        geometria = getIntent().getStringExtra(ARG_GEOMETRIA);
        posicao_lista = getIntent().getIntExtra(ARG_POSICAO_LISTA, -1);

        ClasseDao classeDao = FabricaClasseDao.Criar(getBaseContext());
        Classe classe = classeDao.get(classeId);
        geomAtivado = false;
        FloatingActionButton btnElementoSalvar = (FloatingActionButton) findViewById(R.id.btnElementoSalvar);
        btnElementoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (geomAtivado) {
                    geomAtivado = false;
                    getFragmentManager().beginTransaction()
                            .hide(fragmentGeom)
                            .show(fragment)
                            .commit();
                    geometria = fragmentGeom.getGeometria().toString();
                    fragment.setGeometria(fragmentGeom.getGeometria());
                } else {
                    gravarElemento();
                    Intent intent = new Intent();
                    intent.putExtra(ARG_ELEMENTOID, mItem.getElementoid());
                    intent.putExtra(ARG_POSICAO_LISTA, posicao_lista);
                    setResult(RESULT_OK, intent);
                    finish();
                    onBackPressed();
                }
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

        FloatingActionButton btnElementoAlterarLocal = (FloatingActionButton) findViewById(R.id.btnElementoAlterarLocal);
        btnElementoAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!geomAtivado) {
                    geomAtivado = true;
                    getFragmentManager().beginTransaction()
                            .hide(fragment)
                            .show(fragmentGeom)
                            .commit();
                }
            }
        });
        btnElementoAlterarLocal.setVisibility((classe.getClasseEnum() == PONTO ? View.VISIBLE : View.INVISIBLE));

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

            Bundle arguments1 = new Bundle();
            arguments1.putInt(ARG_ELEMENTOID, elementoId);
            arguments1.putInt(ARG_TIPOELEMENTOID, tipoelementoId);
            arguments1.putInt(ARG_CLASSEID, classeId);
            arguments1.putString(ARG_GEOMETRIA, geometria);

            fragment = new ElementoDetailFragment();
            fragment.setArguments(arguments1);
            fragment.setListaImagens(new ArrayList<String>());
            fragment.setListaImagensExcluir(new ArrayList<String>());

            Bundle arguments2 = new Bundle();
            arguments2.putInt(ARG_ELEMENTOID, elementoId);
            arguments2.putInt(ARG_TIPOELEMENTOID, tipoelementoId);
            arguments2.putInt(ARG_CLASSEID, classeId);
            arguments2.putString(ARG_GEOMETRIA, geometria);
            fragmentGeom = new ElementoDetailGeomFragment();
            fragmentGeom.setArguments(arguments2);

            getFragmentManager().beginTransaction()
                    .add(R.id.elemento_detail_container, fragment, tagFragmento)
                    .add(R.id.elemento_detail_geom_container, fragmentGeom, tagFragmentoGeom)
                    .hide(fragmentGeom)
                    .commit();
        } else {
            fragment = (ElementoDetailFragment) getFragmentManager().getFragment(savedInstanceState, tagFragmento);
            fragmentGeom = (ElementoDetailGeomFragment) getFragmentManager().getFragment(savedInstanceState, tagFragmentoGeom);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, tagFragmento, fragment);
        getFragmentManager().putFragment(outState, tagFragmentoGeom, fragmentGeom);
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

    private void gravarElemento() {
        if (fragment == null)
            fragment = (ElementoDetailFragment) getFragmentManager().findFragmentByTag(tagFragmento);

        String titulo = fragment.getTitulo();
        String descricao = fragment.getDescricao();
        String nomeTipoElemento = fragment.getNomeTipoElemento();
        TipoElementoDao tipoElementoDao = FabricaTipoElementoDao.Criar(getBaseContext());
        TipoElemento tipoElemento = tipoElementoDao.getByNome(nomeTipoElemento);
        if (tipoElemento == null) {
            tipoElemento = new TipoElemento(nomeTipoElemento);
            tipoElementoDao.save(tipoElemento);
        }
        mItem = fragment.getElemento();
        mItem.setTitulo(titulo);
        mItem.setDescricao(descricao);
        mItem.setTipoElemento(tipoElemento);

        mItem.getImages().clear();
        for (String image : fragment.getListaImagens())
            mItem.addImage(image);

        ElementoDao elementoDao = FabricaElementoDao.Criar(getBaseContext());
        if (elementoId == 0)
            mItem.setGeometria(geometria);

        elementoDao.save(mItem);
    }
}
