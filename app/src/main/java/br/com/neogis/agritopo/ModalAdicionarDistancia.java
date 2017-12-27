package br.com.neogis.agritopo;

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

    public ModalAdicionarDistancia(MapView mapa) {
        Log.i("Agritopo", "ModalAdicionarArea: iniciando classe");
        this.mapa = mapa;
        this.distancia = new Distancia();

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
    public Distancia finalizar() {
        this.removerModal();
        Log.i("Agritopo", "Finalizando Distância: " + this.distancia.toString());

        TipoElementoDao ted = new TipoElementoDaoImpl(mapa.getContext());
        TipoElemento te = ted.get(5);
        ClasseDao cd = new ClasseDaoImpl(mapa.getContext());
        Classe c = cd.get(3);
        Elemento e = new Elemento(te, c, "", "", distancia.getMyGeoPointList());
        ElementoDao elementoDao = new ElementoDaoImpl(mapa.getContext());
        elementoDao.insert(e);

        return this.distancia;
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
