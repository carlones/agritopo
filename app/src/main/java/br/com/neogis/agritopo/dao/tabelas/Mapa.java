package br.com.neogis.agritopo.dao.tabelas;

public class Mapa {

    private int mapaid;

    private String descricao;

    private String local;

    private String sincronizado;

    private String tipo;

    public Mapa(int mapaid, String descricao, String local, String sincronizado, String tipo) {
        this.mapaid = mapaid;
        this.descricao = descricao;
        this.local = local;
        this.sincronizado = sincronizado;
        this.tipo = tipo;
    }

    public int getMapaid() {
        return mapaid;
    }

    public void setMapaid(int mapaid) {
        this.mapaid = mapaid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
