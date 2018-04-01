package br.com.neogis.agritopo.model;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import br.com.neogis.agritopo.dao.tabelas.Elemento;

public class MyMarker extends Marker {
    protected Elemento elemento;

    public MyMarker(MapView m, Elemento e) {
        super(m);
        elemento = e;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
