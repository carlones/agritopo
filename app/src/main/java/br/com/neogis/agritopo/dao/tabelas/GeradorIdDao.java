package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface GeradorIdDao {
    List<GeradorId> getAll();

    GeradorId get(int id);

    void insert(GeradorId obj);

    void update(GeradorId obj);

    void delete(GeradorId obj);
}
