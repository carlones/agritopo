package br.com.neogis.agritopo.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_ELEMENTO_PONTO_REQUEST;


/**
 * Created by Wagner on 23/09/2017.
 */

public class AdicionarPontoHolder extends Overlay {

    ItemizedOverlayWithFocus<OverlayItem> overlay;
    MyGeoPoint geoPoint;
    private MapView mapa;
    private Activity activity;

    public AdicionarPontoHolder(MapView mapa, Activity activity) {

        this.mapa = mapa;
        this.activity = activity;
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        if(Configuration.getInstance().UsarMiraDuranteMapeamento)
            Utils.desenharMira(c, activity);
    }

    // Exibir o Ponto quando der um toque na tela
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "AdicionarPontoHolder: registrando Ponto");
        if(overlay != null)
        {
            mapa.getOverlays().remove(overlay);
        }
        geoPoint = new MyGeoPoint(obterPonto(event, mapView));
        overlay = criarOverlay(geoPoint);
        mapa.getOverlays().add(overlay);
        mapa.invalidate();

        return true; // Não propagar evento para demais overlays
    }

    public void finalizar() {
        this.removerModal();
        if (overlay != null) {
            Intent intent = new Intent(activity.getBaseContext(), ElementoDetailActivity.class);
            intent.putExtra(ARG_ELEMENTOID, 0);
            intent.putExtra(ARG_TIPOELEMENTOID, 1);
            intent.putExtra(ARG_CLASSEID, 1);
            intent.putExtra(ARG_GEOMETRIA, geoPoint.toString());
            activity.startActivityForResult(intent, PEGAR_ELEMENTO_PONTO_REQUEST);
        }
    }

    public void cancelar() {
        Log.i("Agritopo", "Cancelando Distância");
        this.removerModal();
    }

    private void removerModal() {
        if(overlay != null)
            this.mapa.getOverlays().remove(overlay);
        this.mapa.getOverlays().remove(this);
        this.mapa.invalidate();
    }

    private ItemizedOverlayWithFocus<OverlayItem> criarOverlay(IGeoPoint ponto){
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("", "", ponto));

        return new ItemizedOverlayWithFocus<OverlayItem>(activity ,items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
    }

    private IGeoPoint obterPonto(final MotionEvent event, final MapView mapView){
        IGeoPoint ponto;
        if(Configuration.getInstance().UsarMiraDuranteMapeamento)
            ponto = mapView.getMapCenter();
        else
            ponto = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
        return ponto;
    }
}
