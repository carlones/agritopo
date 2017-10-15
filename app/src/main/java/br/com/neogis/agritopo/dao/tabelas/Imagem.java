package br.com.neogis.agritopo.dao.tabelas;

public class Imagem {

    private int imagemid;

    private byte[] arquivo;

    public Imagem(int imagemid, byte[] arquivo) {
        this.imagemid = imagemid;
        this.arquivo = arquivo;
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
