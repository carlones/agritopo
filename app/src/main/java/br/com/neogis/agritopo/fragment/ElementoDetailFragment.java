package br.com.neogis.agritopo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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
import br.com.neogis.agritopo.activity.ElementoListActivity;
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;

/**
 * A fragment representing a single Elemento detail screen.
 * This fragment is either contained in a {@link ElementoListActivity}
 * in two-pane mode (on tablets) or a {@link ElementoDetailActivity}
 * on handsets.
 */
public class ElementoDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ELEMENTOID = "elementoid";
    public static final String ARG_CLASSEID = "classeid";
    public static final String ARG_TIPOELEMENTOID = "tipoelementoid";
    public static final String ARG_GEOMETRIA = "geometria";
    public static final String ARG_POSICAO_LISTA = "posicao_lista";

    public static final int PICK_AREA_REQUEST = 4000;
    public static final int PICK_DISTANCIA_REQUEST = 3000;
    public static final int PICK_PONTO_REQUEST = 5000;

    public static final int ALTERAR_ELEMENTO_REQUEST = 10;

    /**
     * The dummy content this fragment is presenting.
     */
    private Elemento mItem;

    private EditText elementoTituloView;
    private AutoCompleteTextView elementoTipoElementoView;
    private EditText elementoDescricaoView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ElementoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int elementoId = getArguments().getInt(ARG_ELEMENTOID);
        if (elementoId != 0) {
            ElementoDao elementoDao = new ElementoDaoImpl(getContext());
            mItem = elementoDao.get(elementoId);
        } else {
            ClasseDao classeDao = new ClasseDaoImpl(this.getContext());
            Classe classe = classeDao.get(getArguments().getInt(ARG_CLASSEID, 0));
            TipoElementoDao ted = new TipoElementoDaoImpl(this.getContext());
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
        View rootView = inflater.inflate(R.layout.elemento_detail, container, false);

        elementoTituloView = (EditText) rootView.findViewById(R.id.elementoTitulo);
        elementoTipoElementoView = (AutoCompleteTextView) rootView.findViewById(R.id.elementoTipoElemento);
        elementoDescricaoView = (EditText) rootView.findViewById(R.id.elementoDescricao);

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
        elementoTipoElementoView.setAdapter(adapter);
        elementoTipoElementoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementoTipoElementoView.showDropDown();
            }
        });

        if (mItem != null) {
            elementoTituloView.setText(mItem.getTitulo());
            elementoTipoElementoView.setText(mItem.getTipoElemento().getNome());
            elementoDescricaoView.setText(mItem.getDescricao());
        }

        return rootView;
    }

    public String getTitulo() {
        return elementoTituloView.getText().toString();
    }

    public String getDescricao() {
        return elementoDescricaoView.getText().toString();
    }

    public String getNomeTipoElemento() {
        return elementoTipoElementoView.getText().toString();
    }

    public Elemento getElemento() {
        return mItem;
    }
}
