package br.com.neogis.agritopo.dao.tabelas;

public class ElementoPonto {

    private Elemento elemento;

    private Ponto ponto;

    public ElementoPonto(Elemento elemento, Ponto ponto) {
        this.elemento = elemento;
        this.ponto = ponto;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }
}
