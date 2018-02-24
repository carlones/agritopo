package br.com.neogis.agritopo.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.model.MyGeoPoint;

import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.dao.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_ELEMENTO_PONTO_REQUEST;


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
        Utils.desenharMira(c, activity);
    }

    // Exibir o Ponto quando der um toque na tela
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "AdicionarPontoHolder: registrando Ponto");
        MyGeoPoint ponto = new MyGeoPoint((GeoPoint) mapView.getMapCenter());

        Intent intent = new Intent(activity.getBaseContext(), ElementoDetailActivity.class);
        intent.putExtra(ARG_ELEMENTOID, 0);
        intent.putExtra(ARG_TIPOELEMENTOID, 1);
        intent.putExtra(ARG_CLASSEID, 1);
        intent.putExtra(ARG_GEOMETRIA, ponto.toString());
        activity.startActivityForResult(intent, PEGAR_ELEMENTO_PONTO_REQUEST);

        mapView.invalidate();
        return true; // Não propagar evento para demais overlays
    }
}
