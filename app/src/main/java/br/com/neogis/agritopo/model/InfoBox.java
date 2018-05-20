package br.com.neogis.agritopo.model;

import android.widget.TextView;

public class InfoBox {

    private TextView view;

    public InfoBox(TextView v) {
        view = v;
    }

    public void setText(String s) {
        view.setText(s);
    }

    public void show() {
        view.setVisibility(android.view.View.VISIBLE);
    }

    public void hide() {
        view.setVisibility(android.view.View.GONE);
    }
}
