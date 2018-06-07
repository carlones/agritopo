package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ElementoCampoDinamicoDao {
    List<ElementoCampoDinamico> getAll();

    ElementoCampoDinamico get(int id);

    void insert(ElementoCampoDinamico obj);

    void update(ElementoCampoDinamico obj);

    void delete(ElementoCampoDinamico obj);
}
