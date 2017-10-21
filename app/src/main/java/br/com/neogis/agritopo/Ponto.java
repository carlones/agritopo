package br.com.neogis.agritopo;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vera on 17/10/2017.
 */

public class Ponto {

    int id;
    String titulo;
    String descricao;
    GeoPoint coordenadas;

    public Ponto(GeoPoint coordenadas) {
        this.coordenadas = coordenadas;
    }
    public Ponto(String titulo, String descricao, GeoPoint coordenadas) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.coordenadas = coordenadas;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public GeoPoint getCoordenadas() { return coordenadas; }
    public double getLatitude() { return coordenadas.getLatitude(); }
    public double getLongitude() { return coordenadas.getLongitude(); }
    public double getAltitude() { return coordenadas.getAltitude(); }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCoordenadas(GeoPoint coordenadas) { this.coordenadas = coordenadas; }
    public void setLatitude(double latitude) { coordenadas.setLatitude(latitude); }
    public void setLongitude(double longitude) { coordenadas.setLongitude(longitude); }
    public void setAltitude(double altitude) { coordenadas.setAltitude(altitude); }

    public String toString() {
        return "Ponto id " + Integer.toString(id) + ", geopoint " + coordenadas.toString();
    }
}
