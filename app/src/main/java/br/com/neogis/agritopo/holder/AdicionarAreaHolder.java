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
import br.com.neogis.agritopo.model.Area;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.singleton.Configuration;

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
        ClasseDao classeDao = new ClasseDaoImpl(activity.getBaseContext());
        Classe classe = classeDao.get(2);
        TipoElementoDao ted = new TipoElementoDaoImpl(activity.getBaseContext());
        TipoElemento te = ted.get(3);
        this.area = new Area(new Elemento(te, classe, lista));
        this.area.desenharEm(mapa, Configuration.getInstance().ExibirAreaDistanciaDuranteMapeamento);
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        Utils.desenharMira(c, activity);
    }

    // Exibir o Ponto quando der um toque na tela
    //
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        Log.d("Agritopo", "AdicionarAreaHolder: Ponto registrado");

        GeoPoint ponto = new GeoPoint(this.mapa.getMapCenter().getLatitude(), this.mapa.getMapCenter().getLongitude());
        this.area.adicionarPonto(ponto);
        this.area.setArea();
        this.area.setPerimetro();
        this.area.removerDe(mapa);
        this.area.desenharEm(mapa, Configuration.getInstance().ExibirAreaDistanciaDuranteMapeamento);
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

    public void desfazer() {
        this.area.removerUltimoPonto();
        this.mapa.invalidate();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.area.removerDe(mapa);
        this.mapa.invalidate();
    }
}
