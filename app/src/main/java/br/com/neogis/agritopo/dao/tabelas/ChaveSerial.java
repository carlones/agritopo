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
    private int proprietarioId;
    private LicencaTipo tipo;

    public ChaveSerial(){}

    public ChaveSerial(int serialId, String chave, Date dataexpiracao, int proprietarioid, int usuarioid, LicencaTipo tipo) {
        this.serialId = serialId;
        this.chave = chave;
        this.dataexpiracao = dataexpiracao;
        this.usuarioId = usuarioid;
        this.proprietarioId = proprietarioid;
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

    public int getProprietarioId() {
        return proprietarioId;
    }

    public void setProprietarioId(int proprietarioId) {
        this.proprietarioId = proprietarioId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LicencaTipo getTipo() {
        return tipo;
    }

    public void setTipo(LicencaTipo tipo) {
        this.tipo = tipo;
    }

    public enum LicencaTipo {
        Gratuito, //Gratuito com limitações
        Trial, //Gratuito sem limitações por 15 dias
        Pago_Standalone, //Pago com uma única vez
        Pago //Pago com mensalidade/anuidade
    }
}

