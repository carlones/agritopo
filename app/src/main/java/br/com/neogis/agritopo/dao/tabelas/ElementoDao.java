package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ElementoDao {
    public List<Elemento> getAll();

    public List<Elemento> getByClasse(int classeid);

    public Elemento get(int id);

    public void save(Elemento obj);

    public void insert(Elemento obj);

    public void update(Elemento obj);

    public void delete(Elemento obj);
}
