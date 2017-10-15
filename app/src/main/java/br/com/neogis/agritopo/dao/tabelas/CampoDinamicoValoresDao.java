package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface CampoDinamicoValoresDao {
    public List<CampoDinamicoValores> getAll();

    public CampoDinamicoValores get(int id);

    public void insert(CampoDinamicoValores obj);

    public void update(CampoDinamicoValores obj);

    public void delete(CampoDinamicoValores obj);
}
