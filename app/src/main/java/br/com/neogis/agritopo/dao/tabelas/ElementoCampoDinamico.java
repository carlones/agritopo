package br.com.neogis.agritopo.dao.tabelas;

public class ElementoCampoDinamico {
    private CampoDinamico campoDinamico;
    private TipoElemento tipoElemento;
    private String valor;

    public ElementoCampoDinamico(CampoDinamico campoDinamico, TipoElemento tipoElemento, String valor) {
        this.campoDinamico = campoDinamico;
        this.tipoElemento = tipoElemento;
        this.valor = valor;
    }

    public CampoDinamico getCampoDinamico() {
        return campoDinamico;
    }

    public void setCampoDinamico(CampoDinamico campoDinamico) {
        this.campoDinamico = campoDinamico;
    }

    public TipoElemento getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(TipoElemento tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
