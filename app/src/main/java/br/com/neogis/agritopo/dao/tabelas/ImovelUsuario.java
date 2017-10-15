package br.com.neogis.agritopo.dao.tabelas;

public class ImovelUsuario {
    private Imovel imovel;
    private Usuario usuario;
    private int permissao;

    public ImovelUsuario(Imovel imovel, Usuario usuario, int permissao) {
        this.imovel = imovel;
        this.usuario = usuario;
        this.permissao = permissao;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }
}
