package br.com.neogis.agritopo.holder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import br.com.neogis.agritopo.R;
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
        String mText =
                "Pos: " + Utils.getFormattedLatitudeInDegree(mMyLocationNewOverlay.getLastFix().getLatitude()) + " " + Utils.getFormattedLongitudeInDegree(mMyLocationNewOverlay.getLastFix().getLongitude()) + "\r\n" +
                "Alt: " + Math.round(mMyLocationNewOverlay.getLastFix().getAltitude()) + " m\r\n" +
                "Acc: " + mMyLocationNewOverlay.getLastFix().getAccuracy() + " m\r\n" +
                        "Vel: " + ((mMyLocationNewOverlay.getLastFix().getSpeed() * 3600) / 1000) + " Km/h";
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
