package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ImagemDao {
    List<Imagem> getAll();

    Imagem get(int id);

    int insert(Imagem obj);

    void update(Imagem obj);

    void delete(Imagem obj);
}
