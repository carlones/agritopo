package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ImovelDao {
    List<Imovel> getAll();

    Imovel get(int id);

    void insert(Imovel obj);

    void update(Imovel obj);

    void delete(Imovel obj);
}
