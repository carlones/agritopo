package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface CampoDinamicoDao {
    List<CampoDinamico> getAll();

    CampoDinamico get(int id);

    void insert(CampoDinamico obj);

    void update(CampoDinamico obj);

    void delete(CampoDinamico obj);
}
