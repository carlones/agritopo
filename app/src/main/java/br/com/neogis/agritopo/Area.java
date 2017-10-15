package br.com.neogis.agritopo;

import android.graphics.Color;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Area {
    // Overlay que exibe os pontos no mapa
    public Polygon poligono;
    List<GeoPoint> pontos;

    public Area() {
        this.pontos = new ArrayList<GeoPoint>();
        this.poligono = new Polygon();
        // Cor e estilo da área
        this.poligono.setFillColor(0x12121212);
        this.poligono.setStrokeColor(Color.RED);
        this.poligono.setStrokeWidth(1);
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

    public String toString() {
        return this.pontos.toString();
    }
}
