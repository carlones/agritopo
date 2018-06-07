package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ElementoDao {
    List<Elemento> getAll();

    List<Elemento> getByClasse(int classeid);

    Elemento get(int id);

    void save(Elemento obj);

    void insert(Elemento obj);

    void update(Elemento obj);

    void delete(Elemento obj);
}
