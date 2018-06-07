package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ImovelElementoDao {
    List<ImovelElemento> getAll();

    ImovelElemento get(int id);

    void insert(ImovelElemento obj);

    void update(ImovelElemento obj);

    void delete(ImovelElemento obj);
}
