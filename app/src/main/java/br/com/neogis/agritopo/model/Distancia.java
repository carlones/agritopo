package br.com.neogis.agritopo.model;

import android.graphics.Color;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.utils.UtilMedidas;
import br.com.neogis.agritopo.utils.Utils;
import flexjson.JSONSerializer;

public class Distancia {
    private MyPolyline linha;
    private List<GeoPoint> pontos;
    private double distancia; // em metros

    public Distancia(Elemento elemento) {
        pontos = new ArrayList<>();
        linha = new MyPolyline(elemento);
        linha.setColor(Color.MAGENTA);
        linha.setWidth(5.0f);
        setMyGeoPointList(elemento.getGeometriaListMyGeoPoint());
        setDistancia();
    }

    public void adicionarPonto(GeoPoint ponto) {
        pontos.add(ponto);
        linha.setPoints(this.pontos);
    }

    public boolean ehValida() {
        return this.pontos.size() > 1;
    }

    public List<GeoPoint> getPontos() {
        return this.pontos;
    }

    public void setPontos(List<GeoPoint> pontos) {
        this.pontos = pontos;
    }

    public void setDistancia() {
        distancia = 0.0;
        if (this.ehValida()) {
            distancia = calcularDistancia();
        }
    }

    public double getDistancia() {
        return distancia;
    }

    private double calcularDistancia() {
        GeoPoint p1 = null, p2;
        boolean ehPrimeiro = true;
        double distancia = 0.0;
        for (GeoPoint ponto : pontos) {
            if (ehPrimeiro) {
                p1 = ponto;
                ehPrimeiro = false;
            } else {
                p2 = ponto;
                distancia += Utils.medirDistanciaEmMetros(p1, p2);
                p1 = p2;
            }
        }
        return distancia;
    }

    public String getDistanciaDescricao() {
        if (this.ehValida()) {
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
            double distancia = getDistancia();
            String unidadeMedida = UtilMedidas.ObterDescricaoMedidaPerimetro(distancia);
            distancia = UtilMedidas.CalcularMedidaPerimetro(distancia);
            Log.i("Agritopo", "Distancia.getDistanciaDescricao(): " + df.format(distancia) + " " + unidadeMedida);
            return df.format(distancia) + " " + unidadeMedida;
        } else {
            return "";
        }
    }

    public void desenharEm(MapView mapa) {
        desenharLinha(mapa);
    }

    private void desenharLinha(MapView mapa) {
        linha.setTitle(getElemento().getTitulo());
        linha.setSnippet("<b>" + getElemento().getTipoElemento().getNome() + "</b>" +
                (getElemento().getDescricao().isEmpty() ? "" : "<br>" + getElemento().getDescricao()) +
                "<br>" + getDistanciaDescricao());
        linha.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapa));
        mapa.getOverlays().add(this.linha);
    }

    public void removerDe(MapView mapa) {
        removerLinha(mapa);
    }

    private void removerLinha(MapView mapa) {
        mapa.getOverlays().remove(this.linha);
    }

    public String toString() {
        return this.pontos.toString();
    }

    public List<MyGeoPoint> getMyGeoPointList() {
        List<MyGeoPoint> lista = new ArrayList<>();
        for (GeoPoint ponto : pontos) {
            lista.add(new MyGeoPoint(ponto));
        }
        return lista;
    }

    public void setMyGeoPointList(List<MyGeoPoint> lista) {
        pontos.clear();
        for (MyGeoPoint ponto : lista) {
            adicionarPonto(ponto);
        }
    }

    public GeoPoint getCentro() {
        double centroLat = 0.0;
        double centroLon = 0.0;
        for (GeoPoint ponto : this.getPontos()) {
            centroLat += ponto.getLatitude();
            centroLon += ponto.getLongitude();
        }
        centroLat = centroLat / this.getPontos().size();
        centroLon = centroLon / this.getPontos().size();
        GeoPoint centro = new GeoPoint(centroLat, centroLon);
        Log.i("Agritopo", "centro da linha: " + centro.toString());
        return centro;
    }

    public String serializeMyGeoPointList() {
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
        return serializer.serialize(getMyGeoPointList());
    }

    public Elemento getElemento() {
        return linha.getElemento();
    }

    public void setElemento(Elemento elemento) {
        this.linha.setElemento(elemento);
    }

    public MyPolyline getLinha() {
        return linha;
    }

    public void setLinha(MyPolyline linha) {
        this.linha = linha;
    }

    public void removerUltimoPonto() {
        GeoPoint geoPoint = null;
        for (Iterator<GeoPoint> iter = pontos.iterator(); iter.hasNext(); ) {
            geoPoint = iter.next();
        }
        if (geoPoint != null) {
            pontos.remove(geoPoint);
            linha.setPoints(this.pontos);
        }
    }
}
