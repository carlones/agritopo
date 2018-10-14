package br.com.neogis.agritopo.dao.tabelas.Integracao;

import java.util.Date;

/**
 * Created by marci on 06/08/2018.
 */

public class Sincronizacao {
    public static final int ID = 1;

    private int id;
    private Date Data;

    public void setId(int id) {
        this.id = id;
    }

    public void setData(Date data) {
        Data = data;
    }

    public int getId() {
        return id;
    }

    public Date getData() {
        return Data;
    }
}
