package br.com.neogis.agritopo;

import android.graphics.Canvas;
import android.graphics.Paint;
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

public class ModalAdicionarArea extends Overlay {

    MapView mapa;
    Area area;

    public ModalAdicionarArea(MapView mapa) {
        Log.d("Agritopo", "ModalAdicionarArea: iniciando classe");
        this.mapa = mapa;
        this.area = new Area();

        // Exibir o modal
        this.mapa.getOverlays().add(this.area.poligono);
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        // Desenhar a mira
        //
        Paint cor = new Paint();
        cor.setARGB(255, 255, 0, 0);

        int centroX = c.getWidth() / 2;
        int centroY = c.getHeight() / 2;
        int tamanho = 50;
        ;

        c.drawLine(centroX - (tamanho / 2), centroY, centroX + (tamanho / 2), centroY, cor);
        c.drawLine(centroX, centroY - (tamanho / 2), centroX, centroY + (tamanho / 2), cor);

        // Definir parâmetros do desenho da área
        // (o map da osm se encarrega de desenhar o polígono)
        //
        //cor.setARGB(128, 255, 0, 0);
        //cor.setStyle(Paint.Style.FILL);
    }

    // Exibir o Ponto quando der um toque na tela
    //
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "ModalAdicionarArea: Ponto registrado");

        GeoPoint ponto = new GeoPoint(this.mapa.getMapCenter().getLatitude(), this.mapa.getMapCenter().getLongitude());
        this.area.adicionarPonto(ponto);
        this.mapa.invalidate();

        return true; // Não propogar evento para demais overlays
    }

    // Termina o modal e devolve a área (que pode estar incompleta)
    //
    public Area finalizar() {
        this.removerModal();
        this.area.calcularArea();
        Log.d("Agritopo", "Descrição área: " + this.area.descricaoArea());
        this.area.calcularPerimetro();
        Log.d("Agritopo", "Descrição perímetro: " + this.area.descricaoPerimetro());

        TipoElementoDao ted = new TipoElementoDaoImpl(mapa.getContext());
        TipoElemento te = ted.get(3);
        ClasseDao cd = new ClasseDaoImpl(mapa.getContext());
        Classe c = cd.get(2);
        Elemento e = new Elemento(te, c, "", "", area.getMyGeoPointList());
        ElementoDao elementoDao = new ElementoDaoImpl(mapa.getContext());
        elementoDao.insert(e);

        return this.area;
    }

    // Termina o modal, ignorando e descartando a seleção feita pelo usuário
    //
    public void cancelar() {
        this.removerModal();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.mapa.getOverlays().remove(this.area.poligono);
        this.mapa.invalidate();
    }
}
