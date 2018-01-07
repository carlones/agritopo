package br.com.neogis.agritopo.model;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import br.com.neogis.agritopo.dao.tabelas.Elemento;

/**
 * Created by carlo on 03/01/2018.
 */

public class MyOverlayItem extends OverlayItem {
    private Elemento elemento;

    public MyOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint) {
        super(aTitle, aSnippet, aGeoPoint);
    }

    public MyOverlayItem(Elemento elemento) {
        super(elemento.getTitulo(), elemento.getDescricao(), elemento.getGeometriaMyGeoPoint());
        this.elemento = elemento;
    }

    public MyOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint, Elemento elemento) {
        super(aTitle, aSnippet, aGeoPoint);
        this.elemento = elemento;
    }

    public MyOverlayItem(String aUid, String aTitle, String aDescription, IGeoPoint aGeoPoint, Elemento elemento) {
        super(aUid, aTitle, aDescription, aGeoPoint);
        this.elemento = elemento;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
