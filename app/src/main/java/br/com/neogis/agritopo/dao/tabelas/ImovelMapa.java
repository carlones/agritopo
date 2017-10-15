package br.com.neogis.agritopo.dao.tabelas;

public class ImovelMapa {
    private Imovel imovel;
    private Mapa mapa;

    public ImovelMapa(Imovel imovel, Mapa mapa) {
        this.imovel = imovel;
        this.mapa = mapa;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
}
