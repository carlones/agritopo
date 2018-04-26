package br.com.neogis.agritopo.model;

import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Polygon;

import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.kml.MyBasicInfoWindow;

/**
 * Created by carlo on 03/01/2018.
 */

public class MyPolygon extends Polygon {
    private Elemento elemento;

    public MyPolygon() {}

    public MyPolygon(Elemento elemento) {
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
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView){
        if (mInfoWindow == null)
            //no support for tap:
            return false;
        boolean tapped = contains(event);

        if (tapped){
            if(mInfoWindow.getClass().equals(MyBasicInfoWindow.class))
                ((MyBasicInfoWindow)mInfoWindow).setMap(mapView);
        }

        return super.onSingleTapConfirmed(event, mapView);
    }
}
