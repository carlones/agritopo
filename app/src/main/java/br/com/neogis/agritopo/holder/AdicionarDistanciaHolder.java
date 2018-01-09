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
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;
import br.com.neogis.agritopo.model.Distancia;
import br.com.neogis.agritopo.model.MyGeoPoint;

import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.dao.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_ELEMENTO_DISTANCIA_REQUEST;

/**
 * Created by Wagner on 23/09/2017.
 */

public class AdicionarDistanciaHolder extends Overlay {

    private MapView mapa;
    private Distancia distancia;
    private Activity activity;

    public AdicionarDistanciaHolder(MapView mapa, Activity activity) {
        Log.i("Agritopo", "AdicionarAreaHolder: iniciando classe");
        this.mapa = mapa;
        List<MyGeoPoint> lista = new ArrayList<>();
        ClasseDao classeDao = new ClasseDaoImpl(activity.getBaseContext());
        Classe classe = classeDao.get(3);
        TipoElementoDao ted = new TipoElementoDaoImpl(activity.getBaseContext());
        TipoElemento te = ted.get(5);
        this.distancia = new Distancia(new Elemento(te, classe, lista));
        this.activity = activity;
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
        GeoPoint ponto = new GeoPoint(this.mapa.getMapCenter().getLatitude(), this.mapa.getMapCenter().getLongitude());
        this.distancia.adicionarPonto(ponto);
        this.distancia.removerDe(mapa);
        this.distancia.desenharEm(mapa);
        this.mapa.invalidate();
        return true; // Não propogar evento para demais overlays
    }

    // Termina o modal e devolve a área (que pode estar incompleta)
    //
    public void finalizar() {
        this.removerModal();
        if (distancia.ehValida()) {
            Intent intent = new Intent(activity.getBaseContext(), ElementoDetailActivity.class);
            intent.putExtra(ARG_ELEMENTOID, 0);
            intent.putExtra(ARG_TIPOELEMENTOID, 5);
            intent.putExtra(ARG_CLASSEID, 3);
            intent.putExtra(ARG_GEOMETRIA, distancia.serializeMyGeoPointList());
            activity.startActivityForResult(intent, PEGAR_ELEMENTO_DISTANCIA_REQUEST);
        }
    }

    // Termina o modal, ignorando e descartando a seleção feita pelo usuário
    //
    public void cancelar() {
        Log.i("Agritopo", "Cancelando Distância");
        this.removerModal();
    }

    public void desfazer() {
        this.distancia.removerUltimoPonto();
        this.distancia.desenharEm(mapa);
        this.mapa.invalidate();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.distancia.removerDe(mapa);
        this.mapa.invalidate();
    }
}
