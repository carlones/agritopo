package br.com.neogis.agritopo.model;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vera on 17/10/2017.
 */

public class Ponto {

    private int id;
    private String titulo;
    private String descricao;
    private GeoPoint coordenadas;

    public Ponto(GeoPoint coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Ponto(String titulo, String descricao, GeoPoint coordenadas) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.coordenadas = coordenadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public GeoPoint getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(GeoPoint coordenadas) {
        this.coordenadas = coordenadas;
    }

    public double getLatitude() {
        return coordenadas.getLatitude();
    }

    public void setLatitude(double latitude) {
        coordenadas.setLatitude(latitude);
    }

    public double getLongitude() {
        return coordenadas.getLongitude();
    }

    public void setLongitude(double longitude) {
        coordenadas.setLongitude(longitude);
    }

    public double getAltitude() {
        return coordenadas.getAltitude();
    }

    public void setAltitude(double altitude) {
        coordenadas.setAltitude(altitude);
    }

    public String toString() {
        return "Ponto id " + Integer.toString(id) + ", geopoint " + coordenadas.toString();
    }
}
