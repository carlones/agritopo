package br.com.neogis.agritopo.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class SelectableItemHolder extends TreeNode.BaseNodeViewHolder<String> {
    private CheckBox nodeSelector;

    public SelectableItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, String value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_item, null, false);

        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
            }
        });
        nodeSelector.setChecked(node.isSelected());

        LinearLayout picture_wrapper = (LinearLayout) view.findViewById(R.id.picture_wrapper);
        picture_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeSelector.setChecked(!nodeSelector.isChecked());
            }
        });

        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value);
        tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeSelector.setChecked(!nodeSelector.isChecked());
            }
        });

        if (node.isLastChild()) {
            view.findViewById(R.id.bot_line).setVisibility(View.INVISIBLE);
        }

        return view;
    }


    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        nodeSelector.setChecked(mNode.isSelected());
    }
}
