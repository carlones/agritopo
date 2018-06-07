package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ClasseDao {
    List<Classe> getAll();

    Classe get(int id);

    void insert(Classe obj);

    void update(Classe obj);

    void delete(Classe obj);
}
