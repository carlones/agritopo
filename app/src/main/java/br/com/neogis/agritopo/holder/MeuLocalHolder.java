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
import br.com.neogis.agritopo.utils.Utils;

/**
 * Created by carlo on 30/03/2018.
 */

public class MeuLocalHolder extends Overlay {
    private final float[] mMatrixValues = new float[9];
    protected TextPaint mTextPaint = new TextPaint();
    private MapView mapa;
    private MyLocationNewOverlay mMyLocationNewOverlay;
    private Matrix mMatrix = new Matrix();

    public MeuLocalHolder(MapView m, MyLocationNewOverlay l) {
        mapa = m;
        mMyLocationNewOverlay = l;
        mTextPaint.setFilterBitmap(true);
        mTextPaint.clearShadowLayer();
        int scaledSize = m.getContext().getResources().getDimensionPixelSize(R.dimen.gps_info);
        mTextPaint.setTextSize(scaledSize);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setShadowLayer(1.5f, -2, 2, Color.BLACK);
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    public void finalizar() {
        mapa.getOverlays().remove(this);
        mapa.invalidate();
    }

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        String mText = "\r\n\r\n\r\n\r\n" +
                "Pos: " + Utils.getFormattedLatitudeInDegree(mMyLocationNewOverlay.getLastFix().getLatitude()) + " " + Utils.getFormattedLongitudeInDegree(mMyLocationNewOverlay.getLastFix().getLongitude()) + "\r\n" +
                "Alt: " + Math.round(mMyLocationNewOverlay.getLastFix().getAltitude()) + " m\r\n" +
                "Acc: " + mMyLocationNewOverlay.getLastFix().getAccuracy() + " m\r\n" +
                "Vel: " + mMyLocationNewOverlay.getLastFix().getSpeed() + " Km/h\r\n";

        StaticLayout mTextLayout = new StaticLayout(mText, mTextPaint, c.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        c.save();
        c.getMatrix(mMatrix);
        mMatrix.getValues(mMatrixValues);

        final float tx = (-mMatrixValues[Matrix.MTRANS_X] + 20) / mMatrixValues[Matrix.MSCALE_X];
        final float ty = (-mMatrixValues[Matrix.MTRANS_Y]) / mMatrixValues[Matrix.MSCALE_Y];

        c.translate(tx, ty);
        mTextLayout.draw(c);
        c.restore();
    }

    public MyLocationNewOverlay getmMyLocationNewOverlay() {
        return mMyLocationNewOverlay;
    }

    public void setmMyLocationNewOverlay(MyLocationNewOverlay mMyLocationNewOverlay) {
        this.mMyLocationNewOverlay = mMyLocationNewOverlay;
    }
}
