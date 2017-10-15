package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ImovelUsuarioDao {
    public List<ImovelUsuario> getAll();

    public ImovelUsuario get(int id);

    public void insert(ImovelUsuario obj);

    public void update(ImovelUsuario obj);

    public void delete(ImovelUsuario obj);
}
