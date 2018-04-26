package br.com.neogis.agritopo.model;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.kml.MyBasicInfoWindow;

public class MyMarker extends Marker {
    protected Elemento elemento;

    public MyMarker(MapView m) {
        super(m);
    }

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

    @Override
    protected boolean onMarkerClickDefault(Marker marker, MapView mapView) {
        if(mInfoWindow != null && mInfoWindow.getClass().equals(MyBasicInfoWindow.class))
            ((MyBasicInfoWindow)mInfoWindow).setMap(mapView);

        return super.onMarkerClickDefault(marker, mapView);
    }
}
