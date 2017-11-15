package br.com.neogis.agritopo;

import android.graphics.Color;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Area {
    // Overlay que exibe os pontos no mapa
    public Polygon poligono;
    public Marker texto;
    List<GeoPoint> pontos;
    double area; // m²
    double perimetro; // m

    public Area() {
        this.pontos = new ArrayList<>();
        this.poligono = new Polygon();

        // Cor e estilo da área
        this.poligono.setFillColor(0x12121212);
        this.poligono.setStrokeColor(Color.MAGENTA);
        this.poligono.setStrokeWidth(4.0f);
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

    // https://gis.stackexchange.com/questions/711/how-can-i-measure-area-from-geographic-coordinates
    // https://stackoverflow.com/questions/2861272/polygon-area-calculation-using-latitude-and-longitude-generated-from-cartesian-s
    //
    public void calcularArea() {
        this.area = 0.0;
        if( !this.ehValida() )
            return;

        GeoPoint p1, p2;
        for (int i=0; i<this.pontos.size(); i++) {
            p1 = this.pontos.get(i);

            // comparar o último ponto com o primeiro
            if( (i+1) == this.pontos.size() )
                p2 = this.pontos.get(0);
            else
                p2 = this.pontos.get(i+1);

            this.area += Math.toRadians(p2.getLongitude() - p1.getLongitude()) * (
                    2 + Math.sin(Math.toRadians(p1.getLatitude())) +
                            Math.sin(Math.toRadians(p2.getLatitude()))
            );
        }
        this.area = this.area * 6378137.0 * 6378137.0 / 2.0;
        this.area = Math.abs(this.area);
        Log.d("Agritopo", "Área: " + Double.toString(this.area) + "m²");
    }

    // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    //
    public void calcularPerimetro() {
        this.perimetro = 0.0;
        if( !this.ehValida() )
            return;

        double raioTerra = 6371000; // Radius of the earth in m

        GeoPoint p1, p2;
        for (int i=0; i<this.pontos.size(); i++) {
            p1 = this.pontos.get(i);

            // comparar o último ponto com o primeiro
            if( (i+1) == this.pontos.size() )
                p2 = this.pontos.get(0);
            else
                p2 = this.pontos.get(i+1);

            double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
            double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
            double a =
                    Math.sin(dLat/2) * Math.sin(dLat/2) +
                            Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude())) *
                                    Math.sin(dLon/2) * Math.sin(dLon/2)
                    ;
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double d = raioTerra * c; // Distance in m
            this.perimetro += d;
        }
        Log.d("Agritopo", "Perímetro: " + Double.toString(this.perimetro) + "m");
    }

    public String descricaoArea() {
        DecimalFormat df = new DecimalFormat("#,###,###,##0.0");
        return df.format(this.area) + " m²";
    }

    public String descricaoPerimetro() {
        DecimalFormat df = new DecimalFormat("#,###,###,##0.0");
        return df.format(this.perimetro) + " m";
    }

    public void desenharEm(MapView mapa) {
        mapa.getOverlays().add(this.poligono);

        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
        if( this.texto == null )
            this.texto = new Marker(mapa);
        this.texto.setTitle("Área " + this.descricaoArea() + ", Perímetro: " + this.descricaoPerimetro());
        this.texto.setIcon(null);
        this.texto.setPosition(this.getCentro());
        mapa.getOverlays().add(this.texto);
    }

    public void removerDe(MapView mapa) {
        mapa.getOverlays().remove(this.poligono);
        mapa.getOverlays().remove(this.texto);
    }

    public GeoPoint getCentro() {
        double centroLat = 0.0;
        double centroLon = 0.0;
        for(GeoPoint ponto : this.getPontos()) {
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
            adicionarPonto((GeoPoint) ponto);
        }
        calcularArea();
        calcularPerimetro();
    }
}
