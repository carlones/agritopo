package br.com.neogis.agritopo.dao.tabelas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Elemento {

    private int elementoid;

    private TipoElemento tipoElemento;

    private Classe classe;

    private String titulo;

    private String descricao;

    private String created_at;

    private String modified_at;

    public Elemento(int elementoid, TipoElemento tipoElemento, Classe classe, String titulo, String descricao) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.elementoid = elementoid;
        this.tipoElemento = tipoElemento;
        this.classe = classe;
        this.titulo = titulo;
        this.descricao = descricao;
        this.created_at = dateFormat.format(cal);
        this.modified_at = dateFormat.format(cal);
    }

    public int getElementoid() {
        return elementoid;
    }

    public void setElementoid(int elementoid) {
        this.elementoid = elementoid;
        setModified_at();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        setModified_at();
    }

    public TipoElemento getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(TipoElemento tipoElemento) {
        this.tipoElemento = tipoElemento;
        setModified_at();
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
        setModified_at();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        setModified_at();
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public void setModified_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.modified_at = dateFormat.format(cal);
    }
}
