package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ElementoImagemDao {
    List<ElementoImagem> getAll();

    List<ElementoImagem> getByElemento(int id);

    ElementoImagem get(int id);

    void insert(ElementoImagem obj);

    void update(ElementoImagem obj);

    void delete(ElementoImagem obj);
}
