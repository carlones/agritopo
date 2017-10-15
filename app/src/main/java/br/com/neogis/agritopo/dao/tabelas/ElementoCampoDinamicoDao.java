package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ElementoCampoDinamicoDao {
    public List<ElementoCampoDinamico> getAll();

    public ElementoCampoDinamico get(int id);

    public void insert(ElementoCampoDinamico obj);

    public void update(ElementoCampoDinamico obj);

    public void delete(ElementoCampoDinamico obj);
}
