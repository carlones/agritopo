package br.com.neogis.agritopo.dao.tabelas;

import java.util.Date;

/**
 * Created by marci on 21/04/2018.
 */

public class ChaveSerial {
    private int serialId;
    private String chave;
    private Date dataexpiracao;
    private int usuarioId;
    private ChaveSerialTipo tipo;

    public ChaveSerial(int serialId, String chave, Date dataexpiracao, int usuarioid, ChaveSerialTipo tipo){
        this.serialId = serialId;
        this.chave = chave;
        this.dataexpiracao = dataexpiracao;
        this.usuarioId = usuarioid;
        this.tipo = tipo;
    }

    public int getSerialId() {
        return serialId;
    }

    public void setSerialId(int serialId) {
        this.serialId = serialId;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Date getDataexpiracao() {
        return dataexpiracao;
    }

    public void setDataexpiracao(Date dataexpiracao) {
        this.dataexpiracao = dataexpiracao;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public ChaveSerialTipo getTipo() {
        return tipo;
    }

    public void setTipo(ChaveSerialTipo tipo) {
        this.tipo = tipo;
    }

    public enum ChaveSerialTipo {
        Gratuito,
        Pago
    }
}