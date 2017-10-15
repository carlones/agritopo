package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ClasseDao {
    public List<Classe> getAll();

    public Classe get(int id);

    public void insert(Classe obj);

    public void update(Classe obj);

    public void delete(Classe obj);
}
