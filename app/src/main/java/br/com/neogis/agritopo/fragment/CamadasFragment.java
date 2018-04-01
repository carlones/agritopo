package br.com.neogis.agritopo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.holder.CamadaHolder;
import br.com.neogis.agritopo.holder.IconTreeItemHolder;
import br.com.neogis.agritopo.holder.ProfileHolder;
import br.com.neogis.agritopo.holder.SelectableHeaderHolder;
import br.com.neogis.agritopo.holder.SelectableItemHolder;
import br.com.neogis.agritopo.model.ArvoreCamada;
import br.com.neogis.agritopo.model.TreeNode;
import br.com.neogis.agritopo.view.AndroidTreeView;

/**
 * Created by carlo on 30/12/2017.
 */

public class CamadasFragment extends Fragment {
    private AndroidTreeView tView;
    private boolean selectionModeEnabled = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_selectable_nodes, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        /*View selectionModeButton = rootView.findViewById(R.id.btn_toggleSelection);
        selectionModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionModeEnabled = !selectionModeEnabled;
                tView.setSelectionModeEnabled(selectionModeEnabled);
            }
        });*/

        View selectAllBtn = rootView.findViewById(R.id.btn_selectAll);
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectionModeEnabled) {
                    Toast.makeText(getActivity(), "Enable selection mode first", Toast.LENGTH_SHORT).show();
                }
                tView.selectAll(true);
            }
        });

        View deselectAll = rootView.findViewById(R.id.btn_deselectAll);
        deselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectionModeEnabled) {
                    Toast.makeText(getActivity(), "Enable selection mode first", Toast.LENGTH_SHORT).show();
                }
                tView.deselectAll();
            }
        });

        /*View check = rootView.findViewById(R.id.btn_checkSelection);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectionModeEnabled) {
                    Toast.makeText(getActivity(), "Enable selection mode first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), tView.getSelected().size() + " selected", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        TreeNode root = TreeNode.root();

        CamadaHolder camadaHolder = CamadaHolder.getInstance();
        for(ArvoreCamada arvore: camadaHolder.camadas) {
            TreeNode s = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_sd_storage, arvore.nome)).setViewHolder(new ProfileHolder(getActivity()));
            montarArvore(arvore, s);
            root.addChild(s);
        }

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setSelectionModeEnabled(selectionModeEnabled);
        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        return rootView;
    }

    private void montarArvore(ArvoreCamada arvore, TreeNode parentNode) {
        for (ArvoreCamada filho: arvore.filhos) {
            if( filho.ehPasta() ) {
                TreeNode folderNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, filho.nome)).setViewHolder(new SelectableHeaderHolder(getActivity()));
                folderNode.setSelected(filho.selecionado);
                folderNode.arvoreCamada = filho;
                parentNode.addChild(folderNode);
                montarArvore(filho, folderNode);
            }
            else {
                TreeNode fileNode = new TreeNode(filho.nome).setViewHolder(new SelectableItemHolder(getActivity()));
                fileNode.setSelected(filho.selecionado);
                fileNode.arvoreCamada = filho;
                parentNode.addChild(fileNode);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    public void identificarSelecionados() {
        CamadaHolder.getInstance().limparSelecionados();
        for(TreeNode tn: tView.getSelected()) {
            if( tn.arvoreCamada != null)
                tn.arvoreCamada.selecionado = true;
        }
    }
}

