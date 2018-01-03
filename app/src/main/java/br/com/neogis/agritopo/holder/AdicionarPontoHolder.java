package br.com.neogis.agritopo.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import br.com.neogis.agritopo.fragment.ElementoDetailFragment;
import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.model.MyGeoPoint;


/**
 * Created by Wagner on 23/09/2017.
 */

public class AdicionarPontoHolder extends Overlay {

    private Activity activity;

    public AdicionarPontoHolder(Activity activity) {
        this.activity = activity;
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        Utils.desenharMira(c);
    }

    // Exibir o Ponto quando der um toque na tela
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "AdicionarPontoHolder: registrando Ponto");
        MyGeoPoint ponto = new MyGeoPoint((GeoPoint) mapView.getMapCenter());

        Intent intent = new Intent(activity.getBaseContext(), ElementoDetailActivity.class);
        intent.putExtra(ElementoDetailFragment.ARG_ELEMENTOID, 0);
        intent.putExtra(ElementoDetailFragment.ARG_TIPOELEMENTOID, 1);
        intent.putExtra(ElementoDetailFragment.ARG_CLASSEID, 1);
        intent.putExtra(ElementoDetailFragment.ARG_GEOMETRIA, ponto.toString());
        activity.startActivityForResult(intent, ElementoDetailFragment.PICK_PONTO_REQUEST);

        mapView.invalidate();
        return true; // Não propogar evento para demais overlays
    }
}