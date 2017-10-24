package br.com.neogis.agritopo.dao.tabelas;

public class TipoElemento {

    private int tipoelementoid;

    private String nome;

    public TipoElemento(int tipoelementoid, String nome) {
        this.tipoelementoid = tipoelementoid;
        this.nome = nome;
    }

    public TipoElemento(String nome) {
        this.nome = nome;
    }

    public int getTipoelementoid() {
        return tipoelementoid;
    }

    public void setTipoelementoid(int tipoelementoid) {
        this.tipoelementoid = tipoelementoid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
