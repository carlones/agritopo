package br.com.neogis.agritopo.dao.tabelas;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoImagem {

    private Elemento elemento;

    private Imagem imagem;

    public ElementoImagem(Elemento elemento, Imagem imagem) {
        this.elemento = elemento;
        this.imagem = imagem;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }
}
