package br.com.neogis.agritopo.dao.tabelas;

public class Configuracao {

    private String configuracaoid;

    private String nome;

    private String tipo;

    private String valor;

    public Configuracao(String configuracaoid, String nome, String tipo, String valor) {
        this.configuracaoid = configuracaoid;
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getConfiguracaoid() {
        return configuracaoid;
    }

    public void setConfiguracaoid(String configuracaoid) {
        this.configuracaoid = configuracaoid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
