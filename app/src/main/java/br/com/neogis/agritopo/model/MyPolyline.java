package br.com.neogis.agritopo.model;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.kml.MyBasicInfoWindow;

/**
 * Created by carlo on 06/01/2018.
 */

public class MyPolyline extends Polyline {
    private Elemento elemento;

    public MyPolyline() {

    }

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

    @Override
    protected boolean onClickDefault(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        if (mInfoWindow == null)
            return true;

        if(mInfoWindow.getClass().equals(MyBasicInfoWindow.class))
            ((MyBasicInfoWindow)mInfoWindow).setMap(mapView);

        showInfoWindow(eventPos);
        return true;
    }
}
