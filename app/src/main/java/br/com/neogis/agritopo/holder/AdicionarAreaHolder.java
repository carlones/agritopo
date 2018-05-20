package br.com.neogis.agritopo.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;
import br.com.neogis.agritopo.model.Area;
import br.com.neogis.agritopo.model.InfoBox;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_ELEMENTO_AREA_REQUEST;

public class AdicionarAreaHolder extends AdicionarElementoHolder {

    private Area area;
    private InfoBox infoBox;

    public AdicionarAreaHolder(MapView mapa, Activity activity, InfoBox infoBox) {
        this.aceitaSeguirGps = true;
        this.aceitaDesfazer = true;

        this.mapa = mapa;
        this.activity = activity;
        this.infoBox = infoBox;

        List<MyGeoPoint> lista = new ArrayList<>();
        ClasseDao classeDao = new ClasseDaoImpl(activity.getBaseContext());
        Classe classe = classeDao.get(2);
        TipoElementoDao ted = new TipoElementoDaoImpl(activity.getBaseContext());
        TipoElemento te = ted.get(3);
        this.area = new Area(new Elemento(te, classe, lista));
        this.area.desenharEm(mapa);
        this.mapa.getOverlays().add(this);
        this.mapa.invalidate();
    }

    // Desenha a mira no meio do mapa
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        if(Configuration.getInstance().UsarMiraDuranteMapeamento)
            Utils.desenharMira(c, activity);
    }

    // Exibir o Ponto quando der um toque na tela
    //
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        if( !seguindoGPS() ) // provavelmente tocou na tela apenas para mantê-la ativa
            this.adicionarPonto((GeoPoint) obterPonto(event, mapView));
        return true; // Não propogar evento para demais overlays
    }

    public void registrarPontoGPS(Location location) {
        GeoPoint gp = new GeoPoint(location.getLatitude(), location.getLongitude());
        adicionarPonto(gp);
    }

    private void adicionarPonto(GeoPoint gp) {
        this.area.adicionarPonto(gp);
        this.area.setArea();
        this.area.setPerimetro();
        this.area.removerDe(mapa);
        this.area.desenharEm(mapa);
        this.mapa.invalidate();
        this.atualizarInfoBox();
    }

    // Termina o modal e devolve a área (que pode estar incompleta)
    //
    public void finalizar() {
        super.finalizar();
        this.removerModal();
        if (area.ehValida()) {
            criarUltimaAresta();
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

    private void criarUltimaAresta() {
        area.adicionarPonto(area.getPontos().get(0));
    }

    // Termina o modal, ignorando e descartando a seleção feita pelo usuário
    //
    public void cancelar() {
        super.cancelar();
        this.removerModal();
    }

    public void desfazer() {
        super.desfazer();
        this.area.removerUltimoPonto();
        this.area.setArea();
        this.area.setPerimetro();
        this.atualizarInfoBox();
        this.mapa.invalidate();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.area.removerDe(mapa);
        this.mapa.invalidate();
        if( this.infoBox != null )
            this.infoBox.hide();
    }

    private IGeoPoint obterPonto(final MotionEvent event, final MapView mapView){
        IGeoPoint ponto;
        if(Configuration.getInstance().UsarMiraDuranteMapeamento)
            ponto = mapView.getMapCenter();
        else
            ponto = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
        return ponto;
    }

    private void atualizarInfoBox() {
        if( this.infoBox == null ) return;
        if( Configuration.getInstance().ExibirAreaDistanciaDuranteMapeamento ) {
            if (this.area.ehValida()) {
                this.infoBox.setText("Área: " + this.area.getAreaDescricao() + "\nPerím.: " + this.area.descricaoPerimetro());
                this.infoBox.show();
            } else {
                this.infoBox.hide();
            }
        }
        else {
            this.infoBox.hide();
        }
    }
}
