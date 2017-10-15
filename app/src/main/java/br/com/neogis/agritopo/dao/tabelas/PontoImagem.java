package br.com.neogis.agritopo.dao.tabelas;

/**
 * Created by carlo on 15/10/2017.
 */

public class PontoImagem {

    private Ponto ponto;

    private Imagem imagem;

    public PontoImagem(Ponto ponto, Imagem imagem) {
        this.ponto = ponto;
        this.imagem = imagem;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }
}
