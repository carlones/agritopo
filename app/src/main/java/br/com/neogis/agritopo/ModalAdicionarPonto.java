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

import br.com.neogis.agritopo.dao.tabelas.PontoDao;
import br.com.neogis.agritopo.dao.tabelas.PontoDaoImpl;

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
        //PopupPonto popupPonto = new PopupPonto(mapView.getContext());
        Ponto p = new Ponto("Titulo exemplo", "Descrição exemplo\r\nem várias linhas", (GeoPoint) mapView.getMapCenter());

        PontoDao dao = new PontoDaoImpl(mapView.getContext());
        dao.insert(p);
        Log.d("Agritopo", "ModalAdicionarPonto: Ponto salvo: " + p.toString());

        listaPontos.addItem(new OverlayItem(p.getTitulo(), p.getDescricao(), p.getCoordenadas()));
        mapView.invalidate();

        return true; // Não propogar evento para demais overlays
    }
}
