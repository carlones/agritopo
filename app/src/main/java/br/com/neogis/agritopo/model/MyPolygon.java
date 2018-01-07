package br.com.neogis.agritopo.model;

import org.osmdroid.views.overlay.Polygon;

import br.com.neogis.agritopo.dao.tabelas.Elemento;

/**
 * Created by carlo on 03/01/2018.
 */

public class MyPolygon extends Polygon {
    private Elemento elemento;

    public MyPolygon(Elemento elemento) {
        this.elemento = elemento;
        this.setTitle(elemento.getTitulo());
        this.setSubDescription(elemento.getTipoElemento().getNome());
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
