package br.com.neogis.agritopo.holder;

import android.graphics.Canvas;
import android.location.Location;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import br.com.neogis.agritopo.model.InfoBox;
import br.com.neogis.agritopo.utils.Utils;

/**
 * Created by carlo on 30/03/2018.
 */

public class MeuLocalHolder extends Overlay {
    private MapView mapa;
    private MyLocationNewOverlay mMyLocationNewOverlay;
    private InfoBox gpsBox;

    public MeuLocalHolder(MapView m, MyLocationNewOverlay l, InfoBox b) {
        mapa = m;
        gpsBox = b;
        mMyLocationNewOverlay = l;
        gpsBox.show();
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    public void finalizar() {
        gpsBox.hide();
        mapa.getOverlays().remove(this);
        mapa.invalidate();
    }

    public void draw(Canvas c, MapView osmv, boolean shadow) {
        Location location = mMyLocationNewOverlay.getLastFix();
        if( location == null ) // se ainda não inicializou o GPS, não hevrá último ponto
            return;

        String mText =
                "Pos: " + Utils.getFormattedLatitudeInDegree(location.getLatitude()) + " " + Utils.getFormattedLongitudeInDegree(location.getLongitude()) + "\r\n" +
                "Alt: " + Math.round(location.getAltitude()) + " m\r\n" +
                "Acc: " + location.getAccuracy() + " m\r\n" +
                        "Vel: " + ((location.getSpeed() * 3600) / 1000) + " Km/h";
        this.gpsBox.setText(mText);
        this.gpsBox.show();
    }

    public MyLocationNewOverlay getmMyLocationNewOverlay() {
        return mMyLocationNewOverlay;
    }

    public void setmMyLocationNewOverlay(MyLocationNewOverlay mMyLocationNewOverlay) {
        this.mMyLocationNewOverlay = mMyLocationNewOverlay;
    }
}
