package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface UsuarioDao {
    List<Usuario> getAll();

    Usuario get(int id);

    void insert(Usuario obj);

    void update(Usuario obj);

    void delete(Usuario obj);
}
