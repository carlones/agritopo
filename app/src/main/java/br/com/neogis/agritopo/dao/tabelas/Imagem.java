package br.com.neogis.agritopo.dao.tabelas;

public class Imagem {

    private int imagemid;

    private String arquivo;

    public Imagem(int imagemid, String arquivo) {
        this.imagemid = imagemid;
        this.arquivo = arquivo;
    }

    public int getImagemid() {
        return imagemid;
    }

    public void setImagemid(int imagemid) {
        this.imagemid = imagemid;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

}
