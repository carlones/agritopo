package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface CampoDinamicoValoresDao {
    List<CampoDinamicoValores> getAll();

    CampoDinamicoValores get(int id);

    void insert(CampoDinamicoValores obj);

    void update(CampoDinamicoValores obj);

    void delete(CampoDinamicoValores obj);
}
