package br.com.neogis.agritopo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;

import static br.com.neogis.agritopo.dao.Constantes.ALTERAR_ELEMENTO_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_POSICAO_LISTA;

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
    private int classeId;
    private ElementoRecyclerViewAdapter viewAdapter;
    private ArrayList<Integer> ids_selecionados = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        classeId = getIntent().getIntExtra(ARG_CLASSEID, 0);

        setContentView(R.layout.activity_elemento_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastros");
        setSupportActionBar(toolbar);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList("ids_selecionados", ids_selecionados);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ids_selecionados = savedInstanceState.getIntegerArrayList("ids_selecionados");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cadastros, menu);

        boolean mostrarBotaoRemover = !ids_selecionados.isEmpty();
        MenuItem botaoRemover = menu.findItem(R.id.menu_cadastros_acao_remover);
        botaoRemover.setVisible(mostrarBotaoRemover);
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        viewAdapter = new ElementoRecyclerViewAdapter(reconstruirLista());
        recyclerView.setAdapter(viewAdapter);
    }

    public void removerElementosSelecionados(MenuItem item) {
        if (ids_selecionados.isEmpty()) return;

        String mensagemAlerta = ids_selecionados.size() == 1
                ? "Remover esse item?"
                : "Remover " + Integer.toString(ids_selecionados.size()) + " itens?";
        AlertDialog confirmacao = new AlertDialog.Builder(this)
                .setMessage(mensagemAlerta)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ElementoDao dao = new ElementoDaoImpl(getBaseContext());
                        for (Integer id : ids_selecionados) {
                            Elemento e = dao.get(id);
                            dao.delete(e);
                        }

                        // Ocultar o botao de remover
                        ids_selecionados.clear();
                        invalidateOptionsMenu();

                        viewAdapter.mValues = reconstruirLista();
                        viewAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }

                })
                .create();
        confirmacao.show();
    }

    private List<Elemento> reconstruirLista() {
        ElementoDao elementoDao = new ElementoDaoImpl(getBaseContext());
        List<Elemento> list = null;
        if (classeId == 0) {
            list = elementoDao.getAll();
        } else {
            list = elementoDao.getByClasse(classeId);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ALTERAR_ELEMENTO_REQUEST) {
                int elementoId = data.getIntExtra(ARG_ELEMENTOID, -1);
                int posicao_lista = data.getIntExtra(ARG_POSICAO_LISTA, -1);
                if (posicao_lista > -1 && elementoId > -1) {
                    Elemento e = (new ElementoDaoImpl(getBaseContext())).get(elementoId);
                    viewAdapter.mValues.set(posicao_lista, e);
                    viewAdapter.notifyItemChanged(posicao_lista);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class ElementoRecyclerViewAdapter
            extends RecyclerView.Adapter<ElementoRecyclerViewAdapter.ViewHolder> {

        public List<Elemento> mValues;

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

            holder.itemView.setBackgroundColor(getResources().getColor(isSelected(holder) ? R.color.item_marcado : R.color.item_desmarcado));

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
                    intent.putExtra(ARG_ELEMENTOID, holder.mItem.getElementoid());
                    intent.putExtra(ARG_POSICAO_LISTA, position);

                    ElementoListActivity.this.startActivityForResult(intent, ALTERAR_ELEMENTO_REQUEST);
                    //}
                }
            });
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    toggleSelection(holder);
                    int posicao = holder.getLayoutPosition();
                    notifyItemChanged(posicao);
                    return true;
                }
            });
        }

        private boolean isSelected(ViewHolder holder) {
            Integer id = new Integer(holder.mItem.getElementoid());
            return ids_selecionados.contains(id);
        }

        private void toggleSelection(ViewHolder holder) {
            Integer id = new Integer(holder.mItem.getElementoid());
            if (ids_selecionados.contains(id)) {
                ids_selecionados.remove(id);
            } else {
                ids_selecionados.add(id);
            }
            invalidateOptionsMenu(); // mostrar/ocultar botão Remover
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
}
