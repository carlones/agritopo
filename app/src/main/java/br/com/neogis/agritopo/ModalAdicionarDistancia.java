package br.com.neogis.agritopo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;

/**
 * Created by Wagner on 23/09/2017.
 */

public class ModalAdicionarDistancia extends Overlay {

    MapView mapa;
    Distancia distancia;
    Activity activity;

    public ModalAdicionarDistancia(MapView mapa, Activity activity) {
        Log.i("Agritopo", "ModalAdicionarArea: iniciando classe");
        this.mapa = mapa;
        this.distancia = new Distancia();
        this.activity = activity;

        // Exibir o modal
        this.distancia.desenharEm(this.mapa);
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
        Log.i("Agritopo", "ModalAdicionarArea: Ponto registrado");

        GeoPoint ponto = new GeoPoint(this.mapa.getMapCenter().getLatitude(), this.mapa.getMapCenter().getLongitude());
        this.distancia.adicionarPonto(ponto);
        this.mapa.invalidate();

        return true; // Não propogar evento para demais overlays
    }

    // Termina o modal e devolve a área (que pode estar incompleta)
    //
    public void finalizar() {
        this.removerModal();
        if (distancia.ehValida()) {
            Intent intent = new Intent(activity.getBaseContext(), ElementoDetailActivity.class);
            intent.putExtra(ElementoDetailFragment.ARG_ELEMENTOID, 0);
            intent.putExtra(ElementoDetailFragment.ARG_TIPOELEMENTOID, 5);
            intent.putExtra(ElementoDetailFragment.ARG_CLASSEID, 3);
            intent.putExtra(ElementoDetailFragment.ARG_GEOMETRIA, distancia.serializeMyGeoPointList());
            activity.startActivityForResult(intent, ElementoDetailFragment.PICK_DISTANCIA_REQUEST);
        }
    }

    // Termina o modal, ignorando e descartando a seleção feita pelo usuário
    //
    public void cancelar()
    {
        Log.i("Agritopo", "Cancelando Distância");
        this.removerModal();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.distancia.removerDe(this.mapa);
        this.mapa.invalidate();
    }
}
