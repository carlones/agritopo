package br.com.neogis.agritopo.dao.tabelas;

import java.util.Date;

import br.com.neogis.agritopo.dao.tabelas.Integracao.ISincronizavel;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;

public class TipoElemento implements ISincronizavel {

    private int tipoelementoid;

    private String nome;

    public TipoElemento(int tipoelementoid, String nome) {
        this.tipoelementoid = tipoelementoid;
        this.nome = nome;
    }

    public TipoElemento(String nome) {
        this.nome = nome;
    }

    public int getTipoelementoid() {
        return tipoelementoid;
    }

    public void setTipoelementoid(int tipoelementoid) {
        this.tipoelementoid = tipoelementoid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public long getId() {
        return tipoelementoid;
    }

    @Override
    public TipoAlteracao getTipoAlteracao() {
        return TipoAlteracao.TipoElemento;
    }
}
