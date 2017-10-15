package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface CampoDinamicoDao {
    public List<CampoDinamico> getAll();

    public CampoDinamico get(int id);

    public void insert(CampoDinamico obj);

    public void update(CampoDinamico obj);

    public void delete(CampoDinamico obj);
}
