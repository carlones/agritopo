package br.com.neogis.agritopo.model;

import android.support.v4.content.res.ResourcesCompat;

import org.osmdroid.views.MapView;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.kml.MyBasicInfoWindow;

/**
 * Created by Vera on 17/10/2017.
 */

public class Ponto {
    private Elemento elemento;
    private MyMarker ponto;

    public Ponto(Elemento e) {
        elemento = e;
    }

    public Elemento getElemento() {
        return ponto.getElemento();
    }

    public MyMarker getPonto() {
        return ponto;
    }

    public void setPonto(MyMarker ponto) {
        this.ponto = ponto;
    }

    public void desenharEm(MapView mapa) {
        desenharPonto(mapa);
    }

    public void removerDe(MapView mapa) {
        removerPonto(mapa);
    }

    private void removerPonto(MapView mapa) {
        ponto.remove(mapa);
    }

    private void desenharPonto(MapView mapa) {
        ponto = new MyMarker(mapa, elemento);
        ponto.setTitle(elemento.getTitulo());
        ponto.setSnippet("<b>" + elemento.getTipoElemento().getNome() + "</b>" + (elemento.getDescricao().isEmpty() ? "" : "<br>" + elemento.getDescricao()));
        ponto.setInfoWindow(new MyBasicInfoWindow(R.layout.bonuspack_bubble, mapa));
        ponto.setIcon(ResourcesCompat.getDrawable(mapa.getResources(), org.osmdroid.bonuspack.R.drawable.marker_default, null));
        ponto.setPosition(elemento.getGeometriaMyGeoPoint());
        mapa.getOverlays().add(ponto);
    }
}
