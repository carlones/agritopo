package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ImovelMapaDao {
    public List<ImovelMapa> getAll();

    public ImovelMapa get(int id);

    public void insert(ImovelMapa obj);

    public void update(ImovelMapa obj);

    public void delete(ImovelMapa obj);
}
