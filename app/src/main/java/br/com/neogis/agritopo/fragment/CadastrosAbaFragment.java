package br.com.neogis.agritopo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.activity.CadastrosListarActivity;
import br.com.neogis.agritopo.dao.tabelas.Elemento;

public class CadastrosAbaFragment extends Fragment {

    public String titulo; // na lista de abas
    public String classe;
    public ArrayList<Elemento> elementosDaAba = new ArrayList<>();
    public ArrayList<Integer> idsSelecionados = new ArrayList<>();

    private ElementoRecyclerViewAdapter viewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Não perder atributos quando girar a tela
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_cadastros_listar_aba, container, false);

        RecyclerView recyclerView = ((RecyclerView) v.findViewById(R.id.activity_cadastros_listar_lista));
        viewAdapter = new ElementoRecyclerViewAdapter(elementosDaAba);
        recyclerView.setAdapter(viewAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        return v;
    }

    public void redesenhar() {
        if( viewAdapter != null ) {
            viewAdapter.notifyDataSetChanged();
        }
    }

    public class ElementoRecyclerViewAdapter extends RecyclerView.Adapter<ElementoRecyclerViewAdapter.ViewHolder> {

        private List<Elemento> mValues;

        private ElementoRecyclerViewAdapter(List<Elemento> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_cadastros_listar_item, parent, false);
            return new ElementoRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ElementoRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.tituloView.setText(holder.mItem.getTitulo());
            holder.tipoElementoView.setText(holder.mItem.getTipoElemento().getNome());
            holder.extraView.setText(Elemento.getInformacaoExtra(holder.mItem));
            holder.itemView.setBackgroundColor(getResources().getColor(isSelected(holder) ? R.color.item_marcado : R.color.item_desmarcado));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CadastrosListarActivity atividade = (CadastrosListarActivity) getActivity();
                    atividade.onElementoClicado(holder.mItem);
                }
            });
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    alternarMarcacao(holder);
                    return true;
                }
            });
        }

        private boolean isSelected(ElementoRecyclerViewAdapter.ViewHolder holder) {
            int id = holder.mItem.getElementoid();
            return idsSelecionados.contains(id);
        }

        private void alternarMarcacao(ElementoRecyclerViewAdapter.ViewHolder holder) {
            int id = holder.mItem.getElementoid();
            int posicao = holder.getLayoutPosition();
            notifyItemChanged(posicao);

            CadastrosListarActivity atividade = (CadastrosListarActivity) getActivity();

            if( idsSelecionados.contains(id) ) {
                idsSelecionados.remove(Integer.valueOf(id));
                atividade.onElementoDesmarcado(holder.mItem);
            }
            else {
                idsSelecionados.add(id);
                atividade.onElementoMarcado(holder.mItem);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView tituloView;
            private final TextView tipoElementoView;
            private final TextView extraView;
            private Elemento mItem;

            private ViewHolder(View view) {
                super(view);
                mView = view;

                tituloView       = (TextView) view.findViewById(R.id.titulo);
                tipoElementoView = (TextView) view.findViewById(R.id.tipoElemento);
                extraView        = (TextView) view.findViewById(R.id.extra);
            }
        }
    }
}
