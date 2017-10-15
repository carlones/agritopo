package br.com.neogis.agritopo.dao.tabelas;

public class Ponto {

    private int pontoid;

    private String altitude;

    private String latitude;

    private String longitude;

    public Ponto(int pontoid, String altitude, String latitude, String longitude) {
        this.pontoid = pontoid;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPontoid() {
        return pontoid;
    }

    public void setPontoid(int pontoid) {
        this.pontoid = pontoid;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
