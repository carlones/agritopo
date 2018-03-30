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

import br.com.neogis.agritopo.utils.UtilMedidas;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import flexjson.JSONSerializer;

import static br.com.neogis.agritopo.dao.Constantes.KM_EM_METROS;
import static br.com.neogis.agritopo.dao.Constantes.RAIO_DA_TERRA_EM_METROS;

public class Area {
    // Overlay que exibe os pontos no mapa
    private MyPolygon poligono;
    private Marker marcador;
    private List<GeoPoint> pontos;
    private double area; // m²
    private double perimetro; // m
    private String titulo = "";

    public Area(Elemento elemento) {
        this.pontos = new ArrayList<>();
        this.poligono = new MyPolygon(elemento);
        this.poligono.setFillColor(0x12121212);
        this.poligono.setStrokeColor(Color.MAGENTA);
        this.poligono.setStrokeWidth(4.0f);
        setMyGeoPointList(elemento.getGeometriaListMyGeoPoint());
        setTitulo(elemento.getTitulo());
        setArea();
        setPerimetro();
    }

    public Elemento getElemento() {
        return poligono.getElemento();
    }

    public void setElemento(Elemento elemento) {
        poligono.setElemento(elemento);
    }

    public MyPolygon getPoligono() {
        return poligono;
    }

    public void setPoligono(MyPolygon poligono) {
        this.poligono = poligono;
    }

    public Marker getMarcador() {
        return marcador;
    }

    public void setMarcador(Marker marcador) {
        this.marcador = marcador;
        this.marcador.setTitle((this.titulo.isEmpty() ? "" : this.titulo + "\n") + "Área: " + this.getAreaDescricao() + "\nPerímetro: " + this.descricaoPerimetro());
        this.marcador.setPosition(this.getCentro());
        this.marcador.setIcon(null);
    }

    public void adicionarPonto(GeoPoint ponto) {
        this.pontos.add(ponto);
        this.poligono.setPoints(this.pontos);
    }

    public boolean ehValida() {
        return this.pontos.size() > 2;
    }

    public List<GeoPoint> getPontos() {
        return this.pontos;
    }

    public void setPontos(List<GeoPoint> pontos) {
        this.pontos = pontos;
    }

    public void setArea() {
        area = 0.0;
        if (this.ehValida()) {
            area = calcularArea();
        }
    }

    public double getArea() {
        return area;
    }

    // https://gis.stackexchange.com/questions/711/how-can-i-measure-area-from-geographic-coordinates
    // https://stackoverflow.com/questions/2861272/polygon-area-calculation-using-latitude-and-longitude-generated-from-cartesian-s
    private double calcularArea() {
        double area = 0.0;
        GeoPoint p1, p2;
        for (int i = 0; i < getPontos().size(); i++) {
            p1 = getPontos().get(i);

            // comparar o último ponto com o primeiro
            if ((i + 1) == getPontos().size())
                p2 = getPontos().get(0);
            else
                p2 = getPontos().get(i + 1);

            area += Math.toRadians(p2.getLongitude() - p1.getLongitude()) * (
                    2 + Math.sin(Math.toRadians(p1.getLatitude())) +
                            Math.sin(Math.toRadians(p2.getLatitude()))
            );
        }
        area = area * 6378137.0 * 6378137.0 / 2.0;
        area = Math.abs(area);
        return area;
    }

    public void setPerimetro() {
        perimetro = 0.0;
        if (this.ehValida()) {
            perimetro = calcularPerimetro();
        }
    }

    public double getPerimetro() {
        return perimetro;
    }

    // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    public double calcularPerimetro() {
        double perimetro = 0.0;

        GeoPoint p1, p2;
        for (int i = 0; i < getPontos().size(); i++) {
            p1 = getPontos().get(i);

            // comparar o último ponto com o primeiro
            if ((i + 1) == getPontos().size())
                p2 = getPontos().get(0);
            else
                p2 = getPontos().get(i + 1);

            double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
            double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
            double a =
                    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude())) *
                                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = RAIO_DA_TERRA_EM_METROS * c; // Distance in m
            perimetro += d;
        }
        Log.d("Agritopo", "Perímetro: " + Double.toString(perimetro) + "m");
        return perimetro;
    }

    public String getAreaDescricao() {
        if (this.ehValida()) {
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
            double area = getArea();
            String unidadeMedida = UtilMedidas.ObterDescricaoMedidaArea(area);
            area = UtilMedidas.CalcularMedidaArea(area);
            Log.i("Agritopo", "Area.getAreaDescricao(): " + df.format(area) + " " + unidadeMedida);
            return df.format(area) + " " + unidadeMedida;
        } else {
            return "";
        }
    }

    public String descricaoPerimetro() {
        if (this.ehValida()) {
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
            double perimetro = getPerimetro();
            String unidadeMedida = UtilMedidas.ObterDescricaoMedidaPerimetro(perimetro);
            perimetro = UtilMedidas.CalcularMedidaPerimetro(perimetro);
            Log.i("Agritopo", "Area.descricaoPerimetro(): " + df.format(perimetro) + " " + unidadeMedida);
            return df.format(perimetro) + " " + unidadeMedida;
        } else {
            return "";
        }
    }

    public void desenharEm(MapView mapa, boolean desenharMarcador) {
        desenharPoligono(mapa);
        if(desenharMarcador)
            desenharMarcador(mapa);
    }

    private void desenharMarcador(MapView mapa) {
        if (getMarcador() != null) {
            removerMarcador(mapa);
        }
        if (ehValida()) {
            setArea();
            setPerimetro();
            setMarcador(new MyMarker(mapa));
            mapa.getOverlays().add(getMarcador());
        }
    }

    private void desenharPoligono(MapView mapa) {
        mapa.getOverlays().add(this.poligono);
    }

    public void removerDe(MapView mapa) {
        removerPoligono(mapa);
        removerMarcador(mapa);
    }

    private void removerMarcador(MapView mapa) {
        mapa.getOverlays().remove(getMarcador());
    }

    private void removerPoligono(MapView mapa) {
        mapa.getOverlays().remove(this.poligono);
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
        Log.d("Agritopo", "centro da área: " + centro.toString());
        return centro;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String serializeMyGeoPointList() {
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
        return serializer.serialize(getMyGeoPointList());
    }

    public void removerUltimoPonto() {
        GeoPoint geoPoint = null;
        for (Iterator<GeoPoint> iter = pontos.iterator(); iter.hasNext(); ) {
            geoPoint = iter.next();
        }
        if (geoPoint != null) {
            pontos.remove(geoPoint);
            poligono.setPoints(this.pontos);
        }
    }
}
