package br.com.neogis.agritopo.dao.tabelas;

public class CampoDinamico {

    private int campodinamicoid;

    private TipoElemento tipoElemento;

    private String nome;

    private String tipocomponente;

    public CampoDinamico(int campodinamicoid, String nome, String tipocomponente) {
        this.campodinamicoid = campodinamicoid;
        this.nome = nome;
        this.tipocomponente = tipocomponente;
    }

    public int getCampodinamicoid() {
        return campodinamicoid;
    }

    public void setCampodinamicoid(int campodinamicoid) {
        this.campodinamicoid = campodinamicoid;
    }

    public TipoElemento getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(TipoElemento tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipocomponente() {
        return tipocomponente;
    }

    public void setTipocomponente(String tipocomponente) {
        this.tipocomponente = tipocomponente;
    }
}
