package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ImovelMapaDao {
    List<ImovelMapa> getAll();

    ImovelMapa get(int id);

    void insert(ImovelMapa obj);

    void update(ImovelMapa obj);

    void delete(ImovelMapa obj);
}
