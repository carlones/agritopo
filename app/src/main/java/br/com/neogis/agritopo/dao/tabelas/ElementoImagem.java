package br.com.neogis.agritopo.dao.tabelas;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoImagem {

    private int id;

    private int idElemento;

    private Imagem imagem;

    public ElementoImagem(int id, int idElemento, Imagem imagem) {
        this.setId(id);
        this.idElemento = idElemento;
        this.imagem = imagem;
    }

    public int getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(int elemento) {
        this.idElemento = elemento;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
