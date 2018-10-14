package br.com.neogis.agritopo.dao.tabelas.Integracao;

import java.util.Date;

/**
 * Created by marci on 06/08/2018.
 */

public class Alteracao {
    private long id;
    private long tipoId;
    private Date data;
    private TipoAlteracao tipo;
    private String GUID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoAlteracao getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlteracao tipo) {
        this.tipo = tipo;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public long getTipoId() {
        return tipoId;
    }

    public void setTipoId(long tipoId) {
        this.tipoId = tipoId;
    }
}
