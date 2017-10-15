package br.com.neogis.agritopo.dao.tabelas;

public class GeradorId {

    private String tabela;

    private int id_atual;

    public GeradorId(String tabela, int id_atual) {
        this.tabela = tabela;
        this.id_atual = id_atual;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public int getId_atual() {
        return id_atual;
    }

    public void setId_atual(int id_atual) {
        this.id_atual = id_atual;
    }
}
