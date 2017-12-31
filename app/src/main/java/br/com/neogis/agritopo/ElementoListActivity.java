package br.com.neogis.agritopo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;

/**
 * An activity representing a list of Elementos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ElementoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ElementoListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean painelDuplo;
    private ElementoRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.elemento_list));
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layout);

        if (findViewById(R.id.elemento_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            painelDuplo = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Elemento> list = (new ElementoDaoImpl(getBaseContext()).getAll());
        viewAdapter = new ElementoRecyclerViewAdapter(list);
        recyclerView.setAdapter(viewAdapter);
    }

    public class ElementoRecyclerViewAdapter
            extends RecyclerView.Adapter<ElementoRecyclerViewAdapter.ViewHolder> {

        public final List<Elemento> mValues;

        public ElementoRecyclerViewAdapter(List<Elemento> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.elemento_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.elementoIdView.setText(Integer.toString(holder.mItem.getElementoid()));
            holder.tipoElementoView.setText(holder.mItem.getTipoElemento().getNome());
            holder.classeView.setText(holder.mItem.getClasse().getNome());
            holder.tituloView.setText(holder.mItem.getTitulo());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    if (painelDuplo) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ElementoDetailFragment.ARG_ELEMENTOID, holder.mItem.getElementoid());
                        ElementoDetailFragment fragment = new ElementoDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.elemento_detail_container, fragment)
                                .commit();
                    } else {
                        */
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ElementoDetailActivity.class);
                        intent.putExtra(ElementoDetailFragment.ARG_ELEMENTOID, holder.mItem.getElementoid());
                        intent.putExtra(ElementoDetailFragment.ARG_POSICAO_LISTA, position);

                        ElementoListActivity.this.startActivityForResult(intent, ElementoDetailFragment.ALTERAR_ELEMENTO_REQUEST);
                    //}
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView elementoIdView;
            public final TextView tipoElementoView;
            public final TextView classeView;
            public final TextView tituloView;
            public Elemento mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                elementoIdView = (TextView) view.findViewById(R.id.elementoid);
                tipoElementoView = (TextView) view.findViewById(R.id.tipoElemento);
                classeView = (TextView) view.findViewById(R.id.classe);
                tituloView = (TextView) view.findViewById(R.id.titulo);
            }

            @Override
            public String toString() {
                return super.toString() + ", " + elementoIdView.getText() + ", " + tipoElementoView.getText() + ", " + classeView.getText() + ", " + tituloView.getText();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == RESULT_OK )
        {
            if( requestCode == ElementoDetailFragment.ALTERAR_ELEMENTO_REQUEST )
            {
                int elementoId = data.getIntExtra(ElementoDetailFragment.ARG_ELEMENTOID, -1);
                int posicao_lista = data.getIntExtra(ElementoDetailFragment.ARG_POSICAO_LISTA, -1);
                if( posicao_lista > -1 && elementoId > -1 )
                {
                    Elemento e = (new ElementoDaoImpl(getBaseContext())).get(elementoId);
                    viewAdapter.mValues.set(posicao_lista, e);
                    viewAdapter.notifyItemChanged(posicao_lista);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
