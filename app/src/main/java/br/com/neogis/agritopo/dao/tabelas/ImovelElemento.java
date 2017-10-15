package br.com.neogis.agritopo.dao.tabelas;

public class ImovelElemento {
    private Imovel imovel;
    private Elemento elemento;

    public ImovelElemento(Imovel imovel, Elemento elemento) {
        this.imovel = imovel;
        this.elemento = elemento;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
