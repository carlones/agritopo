package br.com.neogis.agritopo.model;

import org.osmdroid.views.overlay.Polyline;

import br.com.neogis.agritopo.dao.tabelas.Elemento;

/**
 * Created by carlo on 06/01/2018.
 */

public class MyPolyline extends Polyline {
    private Elemento elemento;

    public MyPolyline(Elemento elemento) {
        this.elemento = elemento;
        this.setTitle(elemento.getTitulo());
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
