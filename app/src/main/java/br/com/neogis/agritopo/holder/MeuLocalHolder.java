package br.com.neogis.agritopo.holder;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.dao.tabelas.Mapa;

/**
 * Created by carlo on 30/03/2018.
 */

public class MeuLocalHolder extends Overlay {
    private MapView mapa;
    private MyLocationNewOverlay mMyLocationNewOverlay;
    protected Paint mPaint = new Paint();
    private final float[] mMatrixValues = new float[9];
    private Matrix mMatrix = new Matrix();

    public MeuLocalHolder(MapView m, MyLocationNewOverlay l) {
        mapa = m;
        mMyLocationNewOverlay = l;
        mPaint.setFilterBitmap(true);
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    public void finalizar() {
        mapa.getOverlays().remove(this);
        mapa.invalidate();
    }

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        c.getMatrix(mMatrix);
        mMatrix.getValues(mMatrixValues);
        final float tx = (-mMatrixValues[Matrix.MTRANS_X] + 20) / mMatrixValues[Matrix.MSCALE_X];
        final float ty = (-mMatrixValues[Matrix.MTRANS_Y] + 90) / mMatrixValues[Matrix.MSCALE_Y];
        c.drawText("Pos: " + Utils.getFormattedLatitudeInDegree(mMyLocationNewOverlay.getLastFix().getLatitude()) + " " + Utils.getFormattedLongitudeInDegree(mMyLocationNewOverlay.getLastFix().getLongitude()), tx, ty + 5, mPaint);
        c.drawText("Alt: " + Math.round(mMyLocationNewOverlay.getLastFix().getAltitude()) + "m", tx, ty + 20, mPaint);
        c.drawText("Acc: " + mMyLocationNewOverlay.getLastFix().getAccuracy() + "m", tx, ty + 35, mPaint);
        c.drawText("Vel: " + mMyLocationNewOverlay.getLastFix().getSpeed() + " Km/h", tx, ty + 50, mPaint);
    }

    public MyLocationNewOverlay getmMyLocationNewOverlay() {
        return mMyLocationNewOverlay;
    }

    public void setmMyLocationNewOverlay(MyLocationNewOverlay mMyLocationNewOverlay) {
        this.mMyLocationNewOverlay = mMyLocationNewOverlay;
    }
}
