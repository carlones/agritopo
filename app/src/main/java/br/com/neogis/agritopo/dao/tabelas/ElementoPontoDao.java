package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ElementoPontoDao {
    public List<ElementoPonto> getAll();

    public ElementoPonto get(int id);

    public void insert(ElementoPonto obj);

    public void update(ElementoPonto obj);

    public void delete(ElementoPonto obj);
}
