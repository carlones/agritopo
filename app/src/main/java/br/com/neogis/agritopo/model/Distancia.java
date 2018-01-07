package br.com.neogis.agritopo.model;

import android.graphics.Color;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import flexjson.JSONSerializer;

import static br.com.neogis.agritopo.dao.Constantes.KM_EM_METROS;

public class Distancia {
    private MyPolyline linha;
    private Marker marcador;
    private List<GeoPoint> pontos;
    private double distancia; // em metros

    public Distancia(Elemento elemento) {
        this.pontos = new ArrayList<>();
        this.linha = new MyPolyline(elemento);
        this.linha.setColor(Color.MAGENTA);
        this.linha.setWidth(5.0f);
        setMyGeoPointList(elemento.getGeometriaListMyGeoPoint());
        setDistancia();
    }

    public void adicionarPonto(GeoPoint ponto) {
        this.pontos.add(ponto);
        this.linha.setPoints(this.pontos);
    }

    public boolean ehValida() {
        return this.pontos.size() > 1;
    }

    public Marker getMarcador() {
        return marcador;
    }

    public void setMarcador(Marker marcador) {
        this.marcador = marcador;
        this.marcador.setTitle(getLinha().getElemento().getTitulo() + "\n" + this.getDistanciaDescricao());
        this.marcador.setPosition(this.getCentro());
        this.marcador.setIcon(null);
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
            String unidadeMedida = (distancia >= KM_EM_METROS ? "Km" : "m");
            distancia = (distancia >= KM_EM_METROS ? distancia / KM_EM_METROS : distancia);
            Log.i("Agritopo", "Distancia.getDistanciaDescricao(): " + df.format(distancia) + " " + unidadeMedida);
            return df.format(distancia) + " " + unidadeMedida;
        } else {
            return "";
        }
    }

    public void desenharEm(MapView mapa) {
        desenharLinha(mapa);
        desenharMarcador(mapa);
    }

    private void desenharMarcador(MapView mapa) {
        if (getMarcador() == null)
            setMarcador(new MyMarker(mapa));
        else
            removerMarcador(mapa);

        mapa.getOverlays().add(getMarcador());
    }

    private void desenharLinha(MapView mapa) {
        mapa.getOverlays().add(this.linha);
    }

    public void removerDe(MapView mapa) {
        removerLinha(mapa);
        removerMarcador(mapa);
    }

    private void removerMarcador(MapView mapa) {
        mapa.getOverlays().remove(getMarcador());
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
