package br.com.neogis.agritopo.dao.tabelas;

public class Imovel {

    private int imovelid;

    private String descricao;

    public Imovel(int imovelid, String descricao) {
        this.imovelid = imovelid;
        this.descricao = descricao;
    }

    public int getImovelid() {
        return imovelid;
    }

    public void setImovelid(int imovelid) {
        this.imovelid = imovelid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
