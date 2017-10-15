package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface UsuarioDao {
    public List<Usuario> getAll();

    public Usuario get(int id);

    public void insert(Usuario obj);

    public void update(Usuario obj);

    public void delete(Usuario obj);
}
