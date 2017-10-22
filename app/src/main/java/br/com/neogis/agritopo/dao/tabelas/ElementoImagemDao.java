package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ElementoImagemDao {
    public List<ElementoImagem> getAll();

    public ElementoImagem get(int id);

    public void insert(ElementoImagem obj);

    public void update(ElementoImagem obj);

    public void delete(ElementoImagem obj);
}
