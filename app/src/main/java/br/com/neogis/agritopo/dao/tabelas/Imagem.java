package br.com.neogis.agritopo.dao.tabelas;

public class Imagem {

    private int imagemid;

    private Elemento elemento;

    private byte[] arquivo;

    public Imagem(int imagemid, Elemento elemento, byte[] arquivo) {
        this.imagemid = imagemid;
        this.elemento = elemento;
        this.arquivo = arquivo;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public int getImagemid() {
        return imagemid;
    }

    public void setImagemid(int imagemid) {
        this.imagemid = imagemid;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

}
