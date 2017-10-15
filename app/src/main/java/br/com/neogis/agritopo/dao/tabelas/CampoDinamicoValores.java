package br.com.neogis.agritopo.dao.tabelas;

public class CampoDinamicoValores {
    private CampoDinamico campoDinamico;

    private TipoElemento tipoElemento;

    private String valorpermitido;

    public CampoDinamicoValores(CampoDinamico campoDinamico, TipoElemento tipoElemento, String valorpermitido) {
        this.campoDinamico = campoDinamico;
        this.tipoElemento = tipoElemento;
        this.valorpermitido = valorpermitido;
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

    public String getValorpermitido() {
        return valorpermitido;
    }

    public void setValorpermitido(String valorpermitido) {
        this.valorpermitido = valorpermitido;
    }
}
