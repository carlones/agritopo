package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface MapaDao {
    List<Mapa> getAll();

    Mapa get(int id);

    void insert(Mapa obj);

    void update(Mapa obj);

    void delete(Mapa obj);
}
