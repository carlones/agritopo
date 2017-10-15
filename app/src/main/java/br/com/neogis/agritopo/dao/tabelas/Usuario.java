package br.com.neogis.agritopo.dao.tabelas;

public class Usuario {

    private int usuarioid;

    private String email;

    private String senha;

    private int estado;

    public Usuario(int usuarioid, String email, String senha, int estado) {
        this.usuarioid = usuarioid;
        this.email = email;
        this.senha = senha;
        this.estado = estado;
    }

    public int getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(int usuarioid) {
        this.usuarioid = usuarioid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
