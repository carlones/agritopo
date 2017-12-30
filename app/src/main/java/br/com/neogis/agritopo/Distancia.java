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

public class Distancia {
    // Overlay que exibe os pontos no mapa
    public Polygon linha;
    public Marker texto;
    List<GeoPoint> pontos;
    double distancia; // em metros

    public Distancia() {
        this.pontos = new ArrayList<>();
        this.linha = new Polygon();

        // Cor e estilo da área
        this.linha.setStrokeColor(Color.MAGENTA);
        this.linha.setStrokeWidth(5.0f);
    }

    public void adicionarPonto(GeoPoint ponto) {
        if( this.ehValida() ) return; // não deixar adicionar mais que 2 pontos

        this.pontos.add(ponto);
        this.linha.setPoints(this.pontos);

        this.definirTexto();
    }

    public boolean ehValida() {
        return this.pontos.size() == 2;
    }

    private void definirTexto() {
        if( this.texto != null && this.ehValida() ) {
            calcularDistancia();
            this.texto.setTitle(this.descricaoDistancia());
            this.texto.setPosition(this.getCentro());
        }
    }

    public List<GeoPoint> getPontos() {
        return this.pontos;
    }

    // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    //
    public void calcularDistancia() {
        this.distancia = 0.0;
        if( !this.ehValida() )
            return;

        double raioTerra = 6371000; // Radius of the earth in m

        GeoPoint p1 = this.pontos.get(0);
        GeoPoint p2 = this.pontos.get(1);

        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude())) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        this.distancia = raioTerra * c; // Distance in m

        Log.i("Agritopo", "Distância: " + this.descricaoDistancia());
    }

    public String descricaoDistancia() {
        DecimalFormat df = new DecimalFormat("#,###,###,##0.0");
        Log.i("Agritopo", "Distancia.descricaoDistancia(): " + df.format(this.distancia) + " m");
        return df.format(this.distancia) + " m";
    }

    public void desenharEm(MapView mapa) {
        mapa.getOverlays().add(this.linha);

        if( this.texto == null )
            this.texto = new Marcador(mapa);
        this.definirTexto();

        // Usar texto ao invés de ícone
        Marcador.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
        this.texto.setIcon(null);

        mapa.getOverlays().add(this.texto);
    }

    public void removerDe(MapView mapa) {
        mapa.getOverlays().remove(this.linha);
        mapa.getOverlays().remove(this.texto);
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
        Log.i("Agritopo", "centro da linha: " + centro.toString());
        return centro;
    }
}
