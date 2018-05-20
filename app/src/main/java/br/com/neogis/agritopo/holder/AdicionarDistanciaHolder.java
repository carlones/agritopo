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
import br.com.neogis.agritopo.model.Distancia;
import br.com.neogis.agritopo.model.InfoBox;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_ELEMENTO_DISTANCIA_REQUEST;

public class AdicionarDistanciaHolder extends AdicionarElementoHolder {

    private Distancia distancia;
    private InfoBox infoBox;

    public AdicionarDistanciaHolder(MapView mapa, Activity activity, InfoBox infoBox) {
        this.aceitaSeguirGps = true;
        this.aceitaDesfazer = true;

        this.mapa = mapa;
        this.infoBox = infoBox;
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
        if(Configuration.getInstance().UsarMiraDuranteMapeamento)
            Utils.desenharMira(c, activity);
    }

    // Exibir o Ponto quando der um toque na tela
    //
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
        if( !seguindoGPS() ) // provavelmente tocou na tela apenas para mantê-la ativa
            adicionarPonto((GeoPoint) obterPonto(event, mapView));
        return true; // Não propogar evento para demais overlays
    }

    public void registrarPontoGPS(Location location) {
        GeoPoint gp = new GeoPoint(location.getLatitude(), location.getLongitude());
        adicionarPonto(gp);
    }

    private void adicionarPonto(GeoPoint ponto) {
        this.distancia.adicionarPonto(ponto);
        this.distancia.setDistancia();
        this.distancia.removerDe(mapa);
        this.distancia.desenharEm(mapa);//Configuration.getInstance().ExibirAreaDistanciaDuranteMapeamento);
        this.mapa.invalidate();
        this.atualizarInfoBox();
    }

    // Termina o modal e devolve a área (que pode estar incompleta)
    //
    public void finalizar() {
        super.finalizar();
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
        super.cancelar();
        this.removerModal();
    }

    public void desfazer() {
        super.desfazer();
        this.distancia.removerUltimoPonto();
        this.distancia.removerDe(mapa);
        this.distancia.desenharEm(mapa);
        this.distancia.setDistancia();
        this.atualizarInfoBox();
        this.mapa.invalidate();
    }

    private void removerModal() {
        this.mapa.getOverlays().remove(this);
        this.distancia.removerDe(mapa);
        if( this.infoBox != null )
            this.infoBox.hide();
        this.mapa.invalidate();
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
            if( this.distancia.ehValida() ) {
                this.infoBox.setText("Dist.: " + this.distancia.getDistanciaDescricao());
                this.infoBox.show();
            }
            else {
                this.infoBox.hide();
            }
        }
        else {
            this.infoBox.hide();
        }
    }
}
