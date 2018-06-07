package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ImovelUsuarioDao {
    List<ImovelUsuario> getAll();

    ImovelUsuario get(int id);

    void insert(ImovelUsuario obj);

    void update(ImovelUsuario obj);

    void delete(ImovelUsuario obj);
}
