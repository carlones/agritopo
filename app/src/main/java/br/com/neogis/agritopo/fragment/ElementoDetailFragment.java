package br.com.neogis.agritopo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;

import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.dao.Constantes.ARG_TIPOELEMENTOID;

/**
 * A fragment representing a single Elemento detail screen.
 * This fragment is either contained in a {@link br.com.neogis.agritopo.activity.CadastrosListarActivity}
 * in two-pane mode (on tablets) or a {@link ElementoDetailActivity}
 * on handsets.
 */
public class ElementoDetailFragment extends Fragment {
    private Elemento mItem;
    private EditText elementoTitulo;
    private AutoCompleteTextView elementoTipoElemento;
    private EditText elementoDescricao;
    private EditText elementoInformacao;
    private EditText elementoDataCriacao;
    private EditText elementoDataModificacao;
    private EditText elementoMetadados;

    public ElementoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int elementoId = getArguments().getInt(ARG_ELEMENTOID);
        if (elementoId != 0) {
            ElementoDao elementoDao = new ElementoDaoImpl(getActivity().getBaseContext());
            mItem = elementoDao.get(elementoId);
        } else {
            ClasseDao classeDao = new ClasseDaoImpl(getActivity().getBaseContext());
            Classe classe = classeDao.get(getArguments().getInt(ARG_CLASSEID, 0));
            TipoElementoDao ted = new TipoElementoDaoImpl(getActivity().getBaseContext());
            TipoElemento te = ted.get(getArguments().getInt(ARG_TIPOELEMENTOID, 0));
            mItem = new Elemento(te, classe, "", "", getArguments().getString(ARG_GEOMETRIA));
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getClasse().getNome());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_elemento_detail, container, false);

        elementoTitulo = (EditText) rootView.findViewById(R.id.elementoTitulo);
        elementoTipoElemento = (AutoCompleteTextView) rootView.findViewById(R.id.elementoTipoElemento);
        elementoDescricao = (EditText) rootView.findViewById(R.id.elementoDescricao);
        elementoInformacao = (EditText) rootView.findViewById(R.id.elementoInformacao);
        elementoDataCriacao = (EditText) rootView.findViewById(R.id.elementoDataCriacao);
        elementoDataModificacao = (EditText) rootView.findViewById(R.id.elementoDataModificacao);
        elementoMetadados = (EditText) rootView.findViewById(R.id.elementoMetadados);

        TipoElementoDao tipoElementoDao = new TipoElementoDaoImpl(rootView.getContext());
        List<TipoElemento> listaTipoElemento = tipoElementoDao.getAll();
        List<String> nomesTipoElemento = new ArrayList<>();
        for (TipoElemento te : listaTipoElemento) {
            nomesTipoElemento.add(te.getNome());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                rootView.getContext(),
                android.R.layout.simple_dropdown_item_1line,
                nomesTipoElemento
        );
        elementoTipoElemento.setAdapter(adapter);
        elementoTipoElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementoTipoElemento.showDropDown();
            }
        });

        if (mItem != null) {
            elementoTitulo.setText(mItem.getTitulo());
            elementoTipoElemento.setText(mItem.getTipoElemento().getNome());
            elementoDescricao.setText(mItem.getDescricao());
            elementoDataCriacao.setText(mItem.getCreated_at().toString());
            elementoDataModificacao.setText(mItem.getModified_at().toString());
            elementoInformacao.setKeyListener(null);
            elementoInformacao.setText(Elemento.getInformacaoExtra(mItem));
            elementoMetadados.setKeyListener(null);
            elementoMetadados.setText(mItem.getGeometria());
        }

        return rootView;
    }

    public String getTitulo() {
        return elementoTitulo.getText().toString();
    }

    public String getDescricao() {
        return elementoDescricao.getText().toString();
    }

    public String getNomeTipoElemento() {
        return elementoTipoElemento.getText().toString();
    }

    public Elemento getElemento() {
        return mItem;
    }
}
