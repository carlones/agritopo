package br.com.neogis.agritopo.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.model.Area;
import br.com.neogis.agritopo.model.MyGeoPoint;

import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.dao.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_ELEMENTO_AREA_REQUEST;

/**
 * Created by Wagner on 23/09/2017.
 */

public class AdicionarAreaHolder extends Overlay {

    private MapView mapa;
    private Area area;
    private Activity activity;

    public AdicionarAreaHolder(MapView mapa, Activity activity) {
        Log.d("Agritopo", "AdicionarAreaHolder: iniciando classe");
        this.mapa = mapa;
        this.activity = activity;

        List<MyGeoPoint> lista = new ArrayList<>();
        this.area = new Area(new Elemento(lista));

        // Exibir o modal
        this.mapa.getOverlays().add(this.area.getPoligono());
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        Utils.desenharMira(c);
    }

    // Exibir o Ponto quando der um toque na tela
    //
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "AdicionarAreaHolder: Ponto registrado");

        GeoPoint ponto = new GeoPoint(this.mapa.getMapCenter().getLatitude(), this.mapa.getMapCenter().getLongitude());
        this.area.adicionarPonto(ponto);
        this.mapa.invalidate();

        return true; // Não propogar evento para demais overlays
    }

    // Termina o modal e devolve a área (que pode estar incompleta)
    //
    public void finalizar() {
        this.removerModal();
        if (area.ehValida()) {
            Log.d("Agritopo", "Nova área é válida, adicionando à lista");
            Intent intent = new Intent(activity.getBaseContext(), ElementoDetailActivity.class);
            intent.putExtra(ARG_ELEMENTOID, 0);
            intent.putExtra(ARG_TIPOELEMENTOID, 3);
            intent.putExtra(ARG_CLASSEID, 2);
            intent.putExtra(ARG_GEOMETRIA, area.serializeMyGeoPointList());
            activity.startActivityForResult(intent, PEGAR_ELEMENTO_AREA_REQUEST);
        } else {
            Log.d("Agritopo", "Nova área é inválida, descartando");
        }
    }

    // Termina o modal, ignorando e descartando a seleção feita pelo usuário
    //
    public void cancelar() {
        this.removerModal();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.mapa.getOverlays().remove(this.area.getPoligono());
        this.mapa.invalidate();
    }
}
