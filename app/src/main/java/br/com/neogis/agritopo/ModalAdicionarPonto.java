package br.com.neogis.agritopo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

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

public class ModalAdicionarPonto extends Overlay {

    private MapView mapa;
    private ItemizedOverlayWithFocus<OverlayItem> listaPontos;

    public ModalAdicionarPonto(MapView mapa, ItemizedOverlayWithFocus<OverlayItem> listaPontosOverlay) {
        this.mapa = mapa;
        this.listaPontos = listaPontosOverlay;
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        Paint cor = new Paint();
        cor.setARGB(255, 255, 0, 0);

        int centroX = c.getWidth() / 2;
        int centroY = c.getHeight() / 2;
        int tamanho = 50;

        c.drawLine(centroX - (tamanho / 2), centroY, centroX + (tamanho / 2), centroY, cor);
        c.drawLine(centroX, centroY - (tamanho / 2), centroX, centroY + (tamanho / 2), cor);
    }

    // Exibir o Ponto quando der um toque na tela
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "ModalAdicionarPonto: registrando Ponto");
        MyGeoPoint ponto = new MyGeoPoint((GeoPoint) mapView.getMapCenter());

        TipoElementoDao ted = new TipoElementoDaoImpl(mapView.getContext());
        TipoElemento te = ted.get(1);
        ClasseDao cd = new ClasseDaoImpl(mapView.getContext());
        Classe c = cd.get(1);
        Elemento e = new Elemento(te, c, "", "", ponto);
        ElementoDao dao = new ElementoDaoImpl(mapView.getContext());
        dao.insert(e);
        Log.d("Agritopo", "ModalAdicionarPonto: Ponto salvo: " + e.toString());

        listaPontos.addItem(new OverlayItem(e.getTitulo(), e.getDescricao(), ponto));
        mapView.invalidate();

        return true; // Não propogar evento para demais overlays
    }
}
